package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        JFrame frame = new JFrame("Rune Changer Version - 1.0");
        frame.setContentPane(new MainGUI().getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(400, 250);
        frame.setIconImage(ImageIO.read(getClass().getResource("icon.jpg")));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
            launch(args);
        }
    }
}
