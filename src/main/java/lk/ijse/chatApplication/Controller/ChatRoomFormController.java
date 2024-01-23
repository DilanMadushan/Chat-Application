package lk.ijse.chatApplication.Controller;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_GREENPeer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

public class ChatRoomFormController {
    @FXML
    public TextField txtMessage;
    public TextArea txtDisplay;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private Text txtName;

    @FXML
    private VBox vBox;
    private String path ="";
    private Socket socket;
    private String name;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;



    public void initialize(){
        name = LoginFormController.name;

        if (LoginFormController.filePath !=null) {
            imgAvatar.setImage(new Image(LoginFormController.filePath));
        }

        try{
            socket = new Socket("localhost",999);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(name);
            dataOutputStream.flush();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    listenMessage();
                }
            }).start();

        }catch (IOException e){

        }
    }

    private void listenMessage() {
        while (socket.isConnected()){
            try {
                String message = dataInputStream.readUTF();

                if (message.endsWith("png")||message.endsWith("jpg")){

                    String[] path = message.split(" : ");

                    Platform.runLater(() -> {

                        ImageView imageView = new ImageView(path[1]);
                        imageView.setStyle("-fx-padding: 10px;");
                        imageView.setFitHeight(180);
                        imageView.setFitWidth(100);

                        HBox hBox = new HBox(imageView);
                        hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");
                        vBox.getChildren().add(hBox);

                    });

                }else{
                    Platform.runLater(() -> {
                        Label text = new Label(message);
                        text.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
                        HBox hBox = new HBox(text);
                        hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-left;");
                        vBox.getChildren().add(hBox);
                    });
//
                    System.out.println(name+" : "+message);


                }



            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void sendOnAction(MouseEvent event) {
        try {
            if (path==""){
                String message = txtMessage.getText();
                if (message==null){
                    new Alert(Alert.AlertType.INFORMATION,"Message is empty").show();
                }
                txtMessage.clear();

                dataOutputStream.writeUTF(name+" : "+message);
                dataOutputStream.flush();

                //create a label
                Label label = new Label(message);
                label.setStyle("-fx-background-color:   #F9B3A8;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

                //create a HBox
                HBox hBox = new HBox(label);
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");

                //set HBox in to VBox
                vBox.getChildren().add(hBox);
            }else {
                dataOutputStream.writeUTF(name+" : "+path);
                dataOutputStream.flush();

                ImageView imageView = new ImageView(path);
                imageView.setStyle("-fx-padding: 10px;");
                imageView.setFitHeight(180);
                imageView.setFitWidth(100);

                HBox hBox = new HBox(imageView);
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");
                vBox.getChildren().add(hBox);
                path="";

            }




        }catch (IOException e){

        }
    }

    @FXML
    void attachOnAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(rootNode.getScene().getWindow());

        if(file!=null){
            path = file.getAbsolutePath();
        }
    }

    @FXML
    void emojiOnAction(MouseEvent event) {


    }



}
