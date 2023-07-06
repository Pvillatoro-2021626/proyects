package org.patrickvillatoro.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.patrickvillatoro.system.Principal;

public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
            
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void programador(){
        escenarioPrincipal.programador();
    }
    public void ventanaPaciente(){
       escenarioPrincipal.ventanaPaciente();
    }
    public void ventanaEspecialidad(){
       escenarioPrincipal.ventanaEspecialidad();
    }
    public void ventanaMedicamento(){
       escenarioPrincipal.ventanaMedicamento();
    }
    public void ventanaDoctores (){
       escenarioPrincipal.ventanaDoctores();
    }
    public void ventanaReceta(){
       escenarioPrincipal.ventanaReceta();
    }
    public void ventanaDetalleReceta(){
       escenarioPrincipal.ventanaDetalleReceta();
    }
    public void ventanaCita(){
       escenarioPrincipal.ventanaCita();
    }
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
}
