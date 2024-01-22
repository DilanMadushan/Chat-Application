package lk.ijse.chatApplication.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private ImageView imgAvatar;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private Text txtName;

    @FXML
    private VBox vBox;
    private String path;
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
                Text text = new Text(message);
//                vBox.getChildren().add(text);
                System.out.println(name+" : "+message);

            }catch(IOException e){

            }
        }
    }

    @FXML
    void sendOnAction(MouseEvent event) {
        String message = txtMessage.getText();
        txtMessage.clear();
        try {
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();

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
