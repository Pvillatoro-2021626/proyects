package org.patrickvillatoro.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.patrickvillatoro.bean.DetalleReceta;
import org.patrickvillatoro.bean.Medicamento;
import org.patrickvillatoro.bean.Receta;
import org.patrickvillatoro.db.Conexion;
import org.patrickvillatoro.system.Principal;

public class DetalleRecetaController implements Initializable{
    private Principal esenarioPrincipal;
    private enum operaciones{NUEVO,ACTUALIZAR,ELIMINAR,EDITAR,CANCELAR,NINGUNO,GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleReceta> listaDetalleReceta;
    private ObservableList<Medicamento> listaMedicamento;
    private ObservableList<Receta> listaReceta;  
    
    @FXML private TextField txtCodigoDetalleReceta;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbCodigoReceta;
    @FXML private ComboBox cmbCodigoMedicamento;
    @FXML private TableView tblDetalleReceta;
    @FXML private TableColumn colCodigoDetalleReceta;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colDosis;
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
        cmbCodigoReceta.setItems(getReceta());
        cmbCodigoReceta.setDisable(false);
        cmbCodigoMedicamento.setItems(getMedicamento());
        cmbCodigoMedicamento.setDisable(false);
    }
    
    public void cargarDatos(){
        tblDetalleReceta.setItems(getDetalleReceta());
        colCodigoDetalleReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta,String>("dosis"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoReceta"));
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta,Integer>("codigoMedicamento"));
    }
    
    public void seleccionarElemento(){
        txtCodigoDetalleReceta.setText(String.valueOf(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
        txtDosis.setText(String.valueOf(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getDosis()));
        cmbCodigoMedicamento.getSelectionModel().select(buscarMedicamento(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
        cmbCodigoReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoReceta()));    
    }
    
    public Receta buscarReceta(int codigoReceta){
        Receta resultado = null;
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarReceta(?)}");
        procedimiento.setInt(1, codigoReceta);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new Receta(registro.getInt("codigoReceta"),
                                    registro.getDate("fechaReceta"),
                                    registro.getInt("numeroColegiado"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Medicamento buscarMedicamento(int codigoMedicamento){
        Medicamento resultado = null;
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarMedicamentos(?)}");
        procedimiento.setInt(1, codigoMedicamento);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new Medicamento(registro.getInt("codigoMedicamento"),
                                            registro.getString("nombresMedicamento"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<DetalleReceta> getDetalleReceta(){
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalle()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleReceta(resultado.getInt("codigoDetalleReceta"),
                                            resultado.getString("dosis"),
                                            resultado.getInt("codigoReceta"),
                                            resultado.getInt("codigoMedicamento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaDetalleReceta = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Receta> getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarReceta()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Receta(resultado.getInt("codigoReceta"),
                                        resultado.getDate("fechaReceta"),
                                        resultado.getInt("numeroColegiado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaReceta = FXCollections.observableArrayList(lista);
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
                txtCodigoDetalleReceta.setDisable(true);
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
        DetalleReceta registro = new DetalleReceta();
       // registro.setCodigoDetalleReceta(Integer.parseInt(txtCodigoDetalleReceta.getText()));
        registro.setDosis(txtDosis.getText());
        registro.setCodigoMedicamento(((Medicamento)cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        registro.setCodigoReceta(((Receta)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalle(?,?,?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
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
                    if(tblDetalleReceta.getSelectionModel().getSelectedItem()!=null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro de eliminarlo?");
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDetalle(?)}");
                                procedimiento.setInt(1, ((DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta());
                                listaDetalleReceta.remove(tblDetalleReceta.getSelectionModel().getSelectedIndex());
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
                if(tblDetalleReceta.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnNuevo.setDisable(true);
                    imgEditar.setImage(new Image("/org/patrickvillatoro/image/Guardar.png"));
                    imgReporte.setImage(new Image("/org/patrickvillatoro/image/Cancelar.png"));
                    activarControles();
                    cmbCodigoReceta.setDisable(true);
                    cmbCodigoMedicamento.setDisable(true);
                    txtCodigoDetalleReceta.setEditable(false);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento!!");
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDetalle(?,?)}");
            DetalleReceta registro = (DetalleReceta)tblDetalleReceta.getSelectionModel().getSelectedItem();
            registro.setDosis(txtDosis.getText());
            procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.setString(2,registro.getDosis());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
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
    
    public void activarControles(){
        txtCodigoDetalleReceta.setEditable(true);
        txtDosis.setEditable(true);
        cmbCodigoMedicamento.setDisable(false);
        cmbCodigoReceta.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoDetalleReceta.setEditable(false);
        txtDosis.setEditable(false);
        cmbCodigoMedicamento.setDisable(true);
        cmbCodigoReceta.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoDetalleReceta.clear();
        txtDosis.clear();
        cmbCodigoMedicamento.getSelectionModel().clearSelection();
        cmbCodigoReceta.getSelectionModel().clearSelection();
        tblDetalleReceta.getSelectionModel().clearSelection();
    }
        
    
    
    public Principal getEsenarioPrincipal() {
        return esenarioPrincipal;
    }

    public void setEsenarioPrincipal(Principal esenarioPrincipal) {
        this.esenarioPrincipal = esenarioPrincipal;
    }
    
    public void menuPrincipal(){
        esenarioPrincipal.menuPrincipal();
    }
    
    
}
