package lk.ijse.chatApplication.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;


public class ChatRoomFormController {
    @FXML
    public TextField txtMessage;
    @FXML
    public JFXButton emoji1;
    @FXML
    public JFXButton emoji2;
    @FXML
    public JFXButton emoji3;
    @FXML
    public JFXButton emoji4;
    @FXML
    public JFXButton emoji5;
    @FXML
    public JFXButton emoji6;
    @FXML
    public Pane emojiPane;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private Text txtName;

    @FXML
    private VBox vBox;

    private boolean pane;
    private String path ="";
    private Socket socket;
    private String name;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private String[] emoji = {
            "\uD83D\uDE00", // 😀
            "\uD83D\uDE02", // 😂
            "\uD83D\uDE05", // 😅
            "\uD83D\uDE08", // 😈
            "\uD83D\uDE0E", // 😎
            "\uD83D\uDE09", // 😉
    };



    public void initialize(){
        name = LoginFormController.name;

        if (LoginFormController.filePath !=null) {
            imgAvatar.setImage(new Image(LoginFormController.filePath));
        }

        emojiPane.setVisible(false);
        pane = false;
        setAction();

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

    private void setAction() {
        emoji1.setOnAction(event -> {
            String message = txtMessage.getText();
            txtMessage.setText(message+emoji[0]);
        });

        emoji2.setOnAction(event -> {
            String message = txtMessage.getText();
            txtMessage.setText(message+emoji[1]);
        });

        emoji3.setOnAction(event -> {
            String message = txtMessage.getText();
            txtMessage.setText(message+emoji[2]);
        });

        emoji4.setOnAction(event -> {
            String message = txtMessage.getText();
            txtMessage.setText(message+emoji[3]);
        });

        emoji5.setOnAction(event -> {
            String message = txtMessage.getText();
            txtMessage.setText(message+emoji[4]);
        });

        emoji6.setOnAction(event -> {
            String message = txtMessage.getText();
            txtMessage.setText(message+emoji[5]);
        });

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

                        String[] sender = message.split(" : ");

                        if(sender[0].equals("Server")){
                            Label text = new Label(message);
                            text.setStyle("-fx-background-color: #F9B3A8;-fx-background-radius:15;-fx-font-size: 16;-fx-font-weight: normal;-fx-text-fill: black;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
                            HBox hBox = new HBox(text);
                            hBox.setStyle("-fx-fill-height: true; -fx-min-height: 40; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center;");
                            vBox.getChildren().add(hBox);
                        }else{

                            Label text = new Label(message);
                            text.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
                            HBox hBox = new HBox(text);
                            hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-left;");
                            vBox.getChildren().add(hBox);

                        }


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
    void sendOnAction(ActionEvent event) {
        try {
            if (path==""){
                String message = txtMessage.getText();
                if (message.isEmpty()){
                    new Alert(Alert.AlertType.ERROR,"Message is empty").show();
                    return;
                }
                txtMessage.clear();

                dataOutputStream.writeUTF(name+" : "+message);
                dataOutputStream.flush();

                //create a label
                Label label = new Label(message);
                label.setStyle("-fx-background-color:  #1dd1a1;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

                //create a HBox
                HBox hBox = new HBox(label);
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");

                //set HBox in to VBox
                vBox.getChildren().add(hBox);
            }else {
                dataOutputStream.writeUTF(name+" : "+path);
                dataOutputStream.flush();

                ImageView imageView = new ImageView(path);
                imageView.setStyle("-fx-padding: 100px;");
                imageView.setFitHeight(180);
                imageView.setFitWidth(100);

                HBox hBox = new HBox(imageView);
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 100; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");
                vBox.getChildren().add(hBox);
                path="";

            }


        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @FXML
    void attachOnAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        File file = fileChooser.showOpenDialog(rootNode.getScene().getWindow());

        if(file!=null){
            path = file.getAbsolutePath();

        }
    }

    @FXML
    void emojiOnAction(MouseEvent event) {
        if (pane==false){
            emojiPane.setVisible(true);
            pane=true;

        }else{
            emojiPane.setVisible(false);
            pane=false;
        }
    }

    @FXML
    public void messageOnAction(ActionEvent event) {
        sendOnAction(event);
    }
}
