/*
Nombre: Patrick Eduardo Villatoro Ic
Codigo Tecnico: IN5AV
Carnet: 2021626
Fecha Creacion:05-04-2022
Modificaciones:
 */
package org.patrickvillatoro.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.patrickvillatoro.controller.CitaController;
import org.patrickvillatoro.controller.DetalleRecetaController;
import org.patrickvillatoro.controller.DoctoresController;
import org.patrickvillatoro.controller.EspecialidadController;
import org.patrickvillatoro.controller.LoginController;
import org.patrickvillatoro.controller.MedicamentosController;
import org.patrickvillatoro.controller.MenuPrincipalController;
import org.patrickvillatoro.controller.PacienteController;
import org.patrickvillatoro.controller.ProgramadorController;
import org.patrickvillatoro.controller.RecetaController;
import org.patrickvillatoro.controller.UsuarioController;


public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA = "/org/patrickvillatoro/view/";
    
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Muelitas");
        escenarioPrincipal.getIcons().add(new Image("/org/patrickvillatoro/image/logomuelitas.jpeg"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/patrickvillatoro/view/MenuPrincipalView.fxml"));
//        Scene escena = new Scene(root);
//        escenarioPrincipal.setScene(escena);
        ventanaLogin();
        escenarioPrincipal.show();
    }
    
    public void ventanaLogin(){
        try{
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml",539,380);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController) cambiarEscena("UsuarioView.fxml",636,302);
            usuario.setEsenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",481,400);
            ventanaMenu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void programador(){
        try {
            ProgramadorController ventanaProgramador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml",600,400);
            ventanaProgramador.setEsenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaPaciente(){
        try{
            PacienteController vistaPaciente = (PacienteController) cambiarEscena("PacientesView.fxml",1100,400);
            vistaPaciente.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidad(){
        try {
            EspecialidadController vistaEspecialidad = (EspecialidadController) cambiarEscena("EspecialidadesView.fxml", 779, 403);
            vistaEspecialidad.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicamento(){
        try {
            MedicamentosController vistaMedicamentos = (MedicamentosController) cambiarEscena("MedicamentosView.fxml", 863, 403);
            vistaMedicamentos.setEsenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaDoctores(){
        try {
            DoctoresController vistaDoctores = (DoctoresController) cambiarEscena("DoctoresView.fxml", 884, 400);
            vistaDoctores.setEsenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ventanaReceta(){
        try {
            RecetaController vistaReceta = (RecetaController) cambiarEscena("RecetasView.fxml", 732, 400);
            vistaReceta.setEsenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        public void ventanaDetalleReceta(){
        try {
            DetalleRecetaController vistaDetalleReceta = (DetalleRecetaController) cambiarEscena("DetalleRecetaView.fxml", 921, 400);
            vistaDetalleReceta.setEsenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public void ventanaCita(){
        try{
            CitaController vistaCita = (CitaController) cambiarEscena("CitasView.fxml", 1100, 400);
            vistaCita.setEsenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml,int ancho,int alto) throws Exception{
        
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
}
    
}
