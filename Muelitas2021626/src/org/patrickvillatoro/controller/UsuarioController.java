package org.patrickvillatoro.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.patrickvillatoro.bean.Usuario;
import org.patrickvillatoro.db.Conexion;
import org.patrickvillatoro.system.Principal;

public class UsuarioController implements Initializable {
    private Principal esenarioPrincipal;
    private enum operaciones{NINGUNO,GUARDAR};
    private operaciones tipoDeOperacion =operaciones.NINGUNO;
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword;
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgCancelar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCancelar.setDisable(true);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnCancelar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/patrickvillatoro/image/Guardar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnCancelar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/patrickvillatoro/image/Agregar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtPassword.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null,"Datos Ingresados Correctamente");
            ventanaLogin();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cancelar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnCancelar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/patrickvillatoro/image/Agregar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                ventanaLogin();
        }
    }

    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
        btnCancelar.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        btnCancelar.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
    }
    
    public Principal getEsenarioPrincipal() {
        return esenarioPrincipal;
    }

    public void setEsenarioPrincipal(Principal esenarioPrincipal) {
        this.esenarioPrincipal = esenarioPrincipal;
    }
    
    public void ventanaLogin(){
        esenarioPrincipal.ventanaLogin();
    }
}
