package org.patrickvillatoro.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.patrickvillatoro.bean.Medicamento;
import org.patrickvillatoro.db.Conexion;
import org.patrickvillatoro.report.GenerarReporte;
import org.patrickvillatoro.system.Principal;

public class MedicamentosController implements Initializable{
    private Principal esenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medicamento> listaMedicamento;
    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtNombreMedicamento;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colNombresMedicamento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblMedicamentos.setItems(getMedicamento());
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento,Integer>("codigoMedicamento"));
        colNombresMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento,String>("nombresMedicamento"));
    }
    public void seleccionar(){
        if(tblMedicamentos.getSelectionModel().getSelectedItem()!=null){
            txtCodigoMedicamento.setText(String.valueOf(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
            txtNombreMedicamento.setText(String.valueOf(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombresMedicamento()));
        }else
            JOptionPane.showMessageDialog(null, "Seleccione un dato !!!");
    }
    public ObservableList<Medicamento> getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamento()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                                            resultado.getString("nombresMedicamento")));
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                txtCodigoMedicamento.setDisable(true);
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/patrickvillatoro/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/patrickvillatoro/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/patrickvillatoro/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/patrickvillatoro/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void guardar(){
        Medicamento registro = new Medicamento();
    //  registro.setCodigoMedicamento(Integer.parseInt(txtCodigoMedicamento.getText()));
        registro.setNombresMedicamento(txtNombreMedicamento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamentos(?)}");
            procedimiento.setString(1, registro.getNombresMedicamento());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/patrickvillatoro/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/patrickvillatoro/image/Eliminar.png"));
                tipoDeOperacion=operaciones.NINGUNO;
                break;
                default:
                    if(tblMedicamentos.getSelectionModel().getSelectedItem()!=null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro de eliminarlo?");
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
                                procedimiento.setInt(1, ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                                listaMedicamento.remove(tblMedicamentos.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }            
                    }else
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento!!");
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblMedicamentos.getSelectionModel().getSelectedItem()!=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/patrickvillatoro/image/Guardar.png"));
                    imgReporte.setImage(new Image("/org/patrickvillatoro/image/Cancelar.png"));
                    activarControles();
                    txtCodigoMedicamento.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else 
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento!!");
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/patrickvillatoro/image/Editar.png"));
                imgReporte.setImage(new Image("/org/patrickvillatoro/image/Listar.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamentos(?,?)}");
            Medicamento registro = (Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem();
            registro.setNombresMedicamento(txtNombreMedicamento.getText());
            procedimiento.setInt(1,registro.getCodigoMedicamento());
            procedimiento.setString(2, registro.getNombresMedicamento());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/patrickvillatoro/image/Editar.png"));
                imgReporte.setImage(new Image("/org/patrickvillatoro/image/Listar.png"));
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametro = new HashMap();
        parametro.put("codigoMedicamento", null);
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper","Reporte de Doctor", parametro);
    }
    
    public void desactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtNombreMedicamento.setEditable(false);
    }
    public void activarControles(){
        txtCodigoMedicamento.setEditable(true);
        txtNombreMedicamento.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtNombreMedicamento.clear();
        tblMedicamentos.getSelectionModel().clearSelection();
    }
    public Principal getEsenarioPrincipal(){
        return esenarioPrincipal;
    }
    public void setEsenarioPrincipal(Principal esenarioPrincipal){
        this.esenarioPrincipal = esenarioPrincipal;
    }
    public void menuPrincipal(){
        esenarioPrincipal.menuPrincipal();
    }
}
