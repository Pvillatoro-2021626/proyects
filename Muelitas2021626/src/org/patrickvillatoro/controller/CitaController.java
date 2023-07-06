package org.patrickvillatoro.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.patrickvillatoro.bean.Cita;
import org.patrickvillatoro.bean.Doctor;
import org.patrickvillatoro.bean.Paciente;
import org.patrickvillatoro.db.Conexion;
import org.patrickvillatoro.system.Principal;

public class CitaController implements Initializable {
    private Principal EsenarioPrincipal; 
    private enum operaciones{NUEVO,ELIMINAR,ACTUALIZAR,CANCELAR,EDITAR,NINGUNO,GUARDAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cita> listaCita;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<Doctor> listaDoctor;
    private DatePicker fCita;
    
    @FXML private TextField txtCodigoCita;
    @FXML private TextField txtCondicionPaciente;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtHoraCita;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private GridPane grpFecha; 
    @FXML private TableView tblCitas;
    @FXML private TableColumn colCodigoCita;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colHoraCita;
    @FXML private TableColumn colTratamiento;
    @FXML private TableColumn colCondicionActual;
    @FXML private TableColumn colCodigoPaciente;
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
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFecha.add(fCita, 3, 0);
        fCita.getStylesheets().add("/org/patrickvillatoro/resource/DatePicker.css");
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(false);
        cmbCodigoPaciente.setItems(getPaciente());
        cmbCodigoPaciente.setDisable(false);
    }
    
    public void cargarDatos(){
       tblCitas.setItems(getCita());
       colCodigoCita.setCellValueFactory(new PropertyValueFactory<Cita,Integer>("codigoCita"));
       colFechaCita.setCellValueFactory(new PropertyValueFactory<Cita,Date>("fechaCita"));
       colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita,Time>("horaCita"));
       colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita,String>("tratamiento"));
       colCondicionActual.setCellValueFactory(new PropertyValueFactory<Cita,String>("descripCondActual"));
       colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Cita,Integer>("codigoPaciente"));
       colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Cita,Integer>("numeroColegiado"));
       
    }
    
    public void seleccionarElemento(){
      if(tblCitas.getSelectionModel().getSelectedItem()!=null){
        txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
        fCita.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
        txtHoraCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
        txtTratamiento.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento());
        txtCondicionPaciente.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescripCondActual());
        cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
      }else{
          JOptionPane.showMessageDialog(null,"Seleccione un dato!!!");
      }
      
      
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
    
    public Paciente buscarPaciente(int CodigoPaciente){
        Paciente resultado = null;
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
        procedimiento.setInt(1, CodigoPaciente);
        ResultSet registro = procedimiento.executeQuery();
        while(registro.next()){
            resultado = new Paciente(registro.getInt("codigoPaciente"),
                                    registro.getString("nombresPaciente"),
                                    registro.getString("apellidosPaciente"),
                                    registro.getString("sexo"),
                                    registro.getDate("fechaNacimiento"),
                                    registro.getString("direccionPaciente"),
                                    registro.getString("telefonoPersonal"),
                                    registro.getDate("fechaPrimeraVisita"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
     public ObservableList<Cita> getCita(){
        ArrayList<Cita> lista = new ArrayList<Cita>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCita()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cita(resultado.getInt("codigoCita"),
                                    resultado.getDate("fechaCita"),
                                    resultado.getTime("horaCita"),
                                    resultado.getString("tratamiento"),
                                    resultado.getString("descripCondActual"),
                                    resultado.getInt("codigoPaciente"),
                                    resultado.getInt("numeroColegiado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaCita = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPaciente}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                                    resultado.getString("nombresPaciente"),
                                    resultado.getString("apellidosPaciente"),
                                    resultado.getString("sexo"),
                                    resultado.getDate("fechaNacimiento"),
                                    resultado.getString("direccionPaciente"),
                                    resultado.getString("telefonoPersonal"),
                                    resultado.getDate("fechaPrimeraVisita")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
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
                txtCodigoCita.setEditable(false);
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
        Cita registro = new Cita();
        registro.setFechaCita(fCita.getSelectedDate());
        DateFormat formatoHora = new SimpleDateFormat("HH:mm");
            try {
                registro.setHoraCita(new Time(formatoHora.parse(txtHoraCita.getText()).getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        registro.setTratamiento(txtTratamiento.getText());
        registro.setDescripCondActual(txtCondicionPaciente.getText());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCita(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(2, registro.getHoraCita());
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripCondActual());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.execute();
            listaCita.add(registro);
        }catch(Exception e){
            System.out.print(e.getMessage());
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
                cmbNumeroColegiado.setDisable(true);
                cmbCodigoPaciente.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblCitas.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"Esta seguro de eliminar el registro?","EliminarCita",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCita(?)}");
                                procedimiento.setInt(1,((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                                listaCita.remove(tblCitas.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                        }catch(Exception e){
                                    e.printStackTrace();
                                }
                        }
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCitas.getSelectionModel().getSelectedItem()!= null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnEliminar.setDisable(true);
                    btnNuevo.setDisable(true);
                    imgEditar.setImage(new Image("/org/patrickvillatoro/image/Guardar.png"));
                    imgReporte.setImage(new Image("/org/patrickvillatoro/image/Cancelar.png"));
                    activarControles();
                    cmbNumeroColegiado.setDisable(true);
                    cmbCodigoPaciente.setDisable(true);
                    txtCodigoCita.setEditable(false);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCita(?,?,?,?,?)}");
            Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
            registro.setFechaCita(fCita.getSelectedDate());
            DateFormat formatoHora = new SimpleDateFormat("HH:mm");
            registro.setHoraCita(new Time(formatoHora.parse(txtHoraCita.getText()).getTime()));
            registro.setTratamiento(txtTratamiento.getText());
            registro.setDescripCondActual(txtCondicionPaciente.getText());
            
            procedimiento.setInt(1, registro.getCodigoCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(3, registro.getHoraCita());
            procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDescripCondActual());
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
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
        
    public void activarControles(){
        txtCodigoCita.setEditable(true);
        txtTratamiento.setEditable(true);
        txtCondicionPaciente.setEditable(true);
        txtHoraCita.setEditable(true);
        cmbCodigoPaciente.setDisable(false);
        cmbNumeroColegiado.setDisable(false);
    }
    public void desactivarControles(){
        txtCodigoCita.setEditable(false);
        txtTratamiento.setEditable(false);
        txtCondicionPaciente.setEditable(false);
        txtHoraCita.setEditable(false);
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setDisable(true);
    }
    public void limpiarControles(){
        txtCodigoCita.clear();
        txtTratamiento.clear();
        txtCondicionPaciente.clear();
        txtHoraCita.clear();
        cmbCodigoPaciente.getSelectionModel().clearSelection();
        cmbNumeroColegiado.getSelectionModel().clearSelection();
        tblCitas.getSelectionModel().clearSelection();
        fCita.setSelectedDate(null);
    }
    public Principal getEsenarioPrincipal() {
        return EsenarioPrincipal;
    }

    public void setEsenarioPrincipal(Principal EsenarioPrincipal) {
        this.EsenarioPrincipal = EsenarioPrincipal;
    }
    
   public void menuPrincipal(){
       EsenarioPrincipal.menuPrincipal();
   }
    
}
