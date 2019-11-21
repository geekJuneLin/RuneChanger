package sample;

import javax.swing.*;
import java.awt.*;

public class MainGUI {
    JPanel panel;
    JLabel champLabel;
    JTextField selectedChampTextFiled;
    GridBagConstraints gbc;

    public MainGUI(){
        initVar();
        setupUI();
    }

    private void initVar(){
        panel = new JPanel();

        gbc = new GridBagConstraints();

        champLabel = new JLabel("Selected Champion: ");

        selectedChampTextFiled = new JTextField("Please select a champ!");
        selectedChampTextFiled.setEditable(false);
    }

    private void setupUI(){
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(champLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(selectedChampTextFiled, gbc);
    }

    public JPanel getPanel(){
        return this.panel;
    }
}
