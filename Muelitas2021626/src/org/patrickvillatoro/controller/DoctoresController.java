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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.patrickvillatoro.bean.Doctor;
import org.patrickvillatoro.bean.Especialidad;
import org.patrickvillatoro.db.Conexion;
import org.patrickvillatoro.report.GenerarReporte;
import org.patrickvillatoro.system.Principal;

public class DoctoresController implements Initializable{
    private Principal esenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Doctor> listaDoctor;
    private ObservableList<Especialidad> listaEspecialidad;
    @FXML private TextField txtNumeroColegiado;
    @FXML private TextField txtNombresDoctor;
    @FXML private TextField txtApellidosDoctor;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private TableView tblDoctores;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private TableColumn colNombresDoctor;
    @FXML private TableColumn colApellidosDoctor;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgReporte;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos(); 
        cmbCodigoEspecialidad.setItems(getEspecialidad());
        cmbCodigoEspecialidad.setDisable(true);
    }
    
    public void cargarDatos(){
       tblDoctores.setItems(getDoctor()); 
       colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Doctor,Integer>("numeroColegiado"));
       colNombresDoctor.setCellValueFactory(new PropertyValueFactory<Doctor,String>("nombresDoctor"));
       colApellidosDoctor.setCellValueFactory(new PropertyValueFactory<Doctor,String>("apellidosDoctor"));
       colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Doctor,String>("telefonoContacto"));
       colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory<Doctor,Integer>("codigoEspecialidad"));
    }
    
    public void seleccionarElemento(){
        txtNumeroColegiado.setText(String.valueOf(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
        txtNombresDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNombresDoctor());
        txtApellidosDoctor.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getApellidosDoctor());
        txtTelefonoContacto.setText(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        cmbCodigoEspecialidad.getSelectionModel().select(buscarEspecialidad(((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
    }
    
    public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEspecialidad(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Especialidad(registro.getInt("codigoEspecialidad"),
                                            registro.getString("descripcion"));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctor()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                        resultado.getString("nombresDoctor"),
                                        resultado.getString("apellidosDoctor"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
    }
    public ObservableList<Especialidad> getEspecialidad(){
        ArrayList<Especialidad> lista= new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidades()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                            resultado.getString("descripcion")
                                            ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
                
        return listaEspecialidad = FXCollections.observableArrayList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/patrickvillatoro/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/patrickvillatoro/image/Cancelar.png"));
                tipoDeOperacion= operaciones.GUARDAR;
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
        Doctor registro = new Doctor();
        registro.setNumeroColegiado(Integer.parseInt(txtNumeroColegiado.getText()));
        registro.setNombresDoctor(txtNombresDoctor.getText());
        registro.setApellidosDoctor(txtApellidosDoctor.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDoctores(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2,registro.getNombresDoctor());
            procedimiento.setString(3,registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoContacto());
            procedimiento.setInt(5, registro.getCodigoEspecialidad());
            procedimiento.execute();
            listaDoctor.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/patrickvillatoro/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/patrickvillatoro/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblDoctores.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro de eliminar el dato?","EliminarDoctor",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta ==JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_eliminarDoctores(?)}");
                            procedimiento.setInt(1, ((Doctor)tblDoctores.getSelectionModel().getSelectedItem()).getNumeroColegiado());
                            listaDoctor.remove(tblDoctores.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else 
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un dato");
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblDoctores.getSelectionModel().getSelectedItem()!=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/patrickvillatoro/image/Guardar.png"));
                    imgReporte.setImage(new Image("/org/patrickvillatoro/image/Cancelar.png"));
                    activarControles();
                    txtNumeroColegiado.setEditable(false);
                    cmbCodigoEspecialidad.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;   
                }else 
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento!!");
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
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDoctores(?,?,?,?)}");
            Doctor registro = (Doctor)tblDoctores.getSelectionModel().getSelectedItem();
            registro.setNombresDoctor(txtNombresDoctor.getText());
            registro.setApellidosDoctor(txtApellidosDoctor.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            procedimiento.setInt(1, registro.getNumeroColegiado());
            procedimiento.setString(2, registro.getNombresDoctor());
            procedimiento.setString(3, registro.getApellidosDoctor());
            procedimiento.setString(4, registro.getTelefonoContacto());
           // procedimiento.setInt(5, registro.getCodigoEspecialidad());
            procedimiento.execute();
        } catch (Exception e) {
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
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        
        }
    }
    
    public void imprimirReporte(){
        Map parametro = new HashMap();
        parametro.put("numeroColegiado", null);
        GenerarReporte.mostrarReporte("ReporteDoctores.jasper","Reporte de Doctor", parametro);
    }
    
    public void desactivarControles(){
        txtNumeroColegiado.setEditable(false);
        txtNombresDoctor.setEditable(false);
        txtApellidosDoctor.setEditable(false);
        txtTelefonoContacto.setEditable(false);
        cmbCodigoEspecialidad.setDisable(true);
    }
    public void activarControles(){
        txtNumeroColegiado.setEditable(true);
        txtNombresDoctor.setEditable(true);
        txtApellidosDoctor.setEditable(true);
        txtTelefonoContacto.setEditable(true);
        cmbCodigoEspecialidad.setDisable(false);
    }
    public void limpiarControles(){
        txtNumeroColegiado.clear();
        txtNombresDoctor.clear();
        txtApellidosDoctor.clear();
        txtTelefonoContacto.clear();
        cmbCodigoEspecialidad.getSelectionModel().clearSelection();
        tblDoctores.getSelectionModel().clearSelection();
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
