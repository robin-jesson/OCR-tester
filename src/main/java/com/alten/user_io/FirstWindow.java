/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.user_io;

import com.alten.ocrtester.preferences.AppPreferences;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.JLabel;

/**
 *
 * @author robin.jesson
 */
public class FirstWindow extends JFrame {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 200; 

    public FirstWindow() {
        UserIOAppInterface uioInterface = new UserIOAppInterface();
        AppPreferences pref = AppPreferences.getInstance();
        
        JLabel pathDataLabel = new JLabel("chemin");
        pathDataLabel.setBounds(200, 0, 100, 100);
        this.add(pathDataLabel);
        
        JButton findTestFileBtn = new JButton("Définir le fichier de jeu");
        findTestFileBtn.setBounds(0, 0, 200, 50);
        findTestFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                File workingDirectory = new File(System.getProperty("user.dir"));
                fileChooser.setCurrentDirectory(workingDirectory);
                int val = fileChooser.showOpenDialog(null);
                if (val == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    
                    uioInterface.changePathData(file.getAbsolutePath());
                }
            }
        });
        this.add(findTestFileBtn);
        
        

        JButton partialTestBtn = new JButton("Lancer un test partiel");
        partialTestBtn.setBounds(0, 50, 200, 50);
        this.add(partialTestBtn);

        JButton fullTestBtn = new JButton("Lancer un test complet");
        fullTestBtn.setBounds(0, 100, 200, 50);
        fullTestBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fullTestBtn.setText("Welcome to Javatpoint.");
            }
        });
        this.add(fullTestBtn);

        this.setSize(600, 300);
        this.setLayout(null);
        this.setVisible(true);
    }
}
