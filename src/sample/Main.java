package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        ClientApi api = new ClientApi();
        if(!api.isConnected()){
            JOptionPane.showMessageDialog(null, "Please launch the LOL first!");
            return;
        }else{
            int[] perks = new int[]{
                    8005,
                    9111,
                    9104,
                    8014,
                    8233,
                    8236,
                    5005,
                    5008,
                    5001
            };
            api.changeRunePage("crystal test", 8000, perks,8200);
            //api.getCurrentChamp();
            //api.autoAccept();
            launch(args);
        }
    }
}
