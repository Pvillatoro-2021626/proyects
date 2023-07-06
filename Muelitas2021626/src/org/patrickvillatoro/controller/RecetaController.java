package org.patrickvillatoro.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.patrickvillatoro.bean.Doctor;
import org.patrickvillatoro.bean.Receta;
import org.patrickvillatoro.db.Conexion;
import org.patrickvillatoro.report.GenerarReporte;
import org.patrickvillatoro.system.Principal;

public class RecetaController implements Initializable{
    private Principal esenarioPrincipal;
    private enum operaciones{NUEVO,ACTUALIZAR,ELIMINAR,EDITAR,CANCELAR,NINGUNO,GUARDAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Receta> listaReceta;
    private ObservableList<Doctor> listaDoctor;
    private DatePicker fReceta;
    
    @FXML private TextField txtCodigoReceta;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private GridPane grpFecha;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumeroColegiado;
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
        fReceta = new DatePicker(Locale.ENGLISH);
        fReceta.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fReceta.getCalendarView().setShowWeeks(false);
        grpFecha.add(fReceta, 3, 0);
        fReceta.getStylesheets().add("/org/patrickvillatoro/resource/DatePicker.css");
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(false);
        
    }
    
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta,Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta,Date>("FechaReceta"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Receta,Integer>("numeroColegiado"));
    }
    
    public void seleccionarElemento(){
        txtCodigoReceta.setText(String.valueOf(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
        fReceta.selectedDateProperty().set(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getFechaReceta());
        cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
    }
    
    public Doctor buscarDoctor(int NumeroColegiado){
        Doctor resultado = null;
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctores(?)}");
        procedimiento.setInt(1, NumeroColegiado);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new Doctor(registro.getInt("NumeroColegiado"),
                                    registro.getString("nombresDoctor"),
                                    registro.getString("apellidosDoctor"),
                                    registro.getString("telefonoContacto"),
                                    registro.getInt("codigoEspecialidad"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
                txtCodigoReceta.setDisable(true);
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
        Receta registro = new Receta();
        registro.setFechaReceta(fReceta.getSelectedDate());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarRecetas(?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.setInt(2, registro.getNumeroColegiado());
            procedimiento.execute();
            listaReceta.add(registro);
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
                if(tblRecetas.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el dato?", "EliminarReceta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                            procedimiento.setInt(1,((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                            listaReceta.remove(tblRecetas.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                        }catch(Exception e){
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
                if(tblRecetas.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnNuevo.setDisable(true);
                    imgEditar.setImage(new Image("/org/patrickvillatoro/image/Guardar.png"));
                    imgReporte.setImage(new Image("/org/patrickvillatoro/image/Cancelar.png"));
                    activarControles();
                    cmbNumeroColegiado.setDisable(true);
                    txtCodigoReceta.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarReceta(?,?)}");
            Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();
            registro.setFechaReceta(fReceta.getSelectedDate());
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                break;
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
        Map parametros = new HashMap();
        int colReceta = ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta();
        parametros.put("colReceta",colReceta);
        GenerarReporte.mostrarReporte("ReporteReceta.jasper", "Receta del Paciente", parametros);
    }
    
    public void activarControles(){
        txtCodigoReceta.setEditable(true);
        cmbNumeroColegiado.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCodigoReceta.setEditable(false);
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void limpiarControles(){
        txtCodigoReceta.clear();
        cmbNumeroColegiado.getSelectionModel().clearSelection();
        tblRecetas.getSelectionModel().clearSelection();
        fReceta.setSelectedDate(null);
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
