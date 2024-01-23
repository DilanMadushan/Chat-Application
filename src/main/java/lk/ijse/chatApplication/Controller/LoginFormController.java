package lk.ijse.chatApplication.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.chatApplication.ClientHandler.ClientHandler;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class LoginFormController {

    public AnchorPane rootNode;
    public ImageView Profile;
    @FXML
    private JFXTextField txtName;

    private ServerSocket serverSocket;
    static String name;
    static String filePath;

     public void initialize(){
         try {
             serverSocket = new ServerSocket(999);
         } catch (IOException e) {
             throw new RuntimeException(e);
         }

         new Thread(new Runnable() {
             @Override
             public void run() {
                 while(!serverSocket.isClosed()){
                     try{
                         Socket socket = serverSocket.accept();
                         System.out.println("New user connected");
                         ClientHandler clientHandler = new ClientHandler(socket);
                         clientHandler.listenMessage();

                     }catch(IOException e){

                     }


                 }
             }
         }).start();

     }

    @FXML
    void changeAvtarOnAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File file = fileChooser.showOpenDialog(rootNode.getScene().getWindow());

        if (file!=null) {
            filePath = file.getAbsolutePath();
            Profile.setImage(new Image(filePath));
        }

    }

    @FXML
    void joinOnAction(ActionEvent event) throws IOException {
        name = txtName.getText();

        if(name.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Please enter your user name").show();
            return;
        }

        txtName.clear();
        Profile.setImage(new Image("E:\\ijse Projects\\Chat Application\\Chat Application\\src\\main\\resources" +
                "\\images\\pngwing.com (1).png"));


        Parent rootNode = FXMLLoader.load(getClass().getResource("/view/ChatRoomForm.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(name);
        stage.centerOnScreen();
        stage.show();
    }

}
