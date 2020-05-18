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
import java.nio.file.Path;
import java.nio.file.Paths;
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
        
        JLabel pathDataLabel = new JLabel(Paths.get(pref.getPathToData()).getFileName().toString());
        pathDataLabel.setBounds(210, 0, 200, 50);
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
                    pathDataLabel.setText(Paths.get(pref.getPathToData()).getFileName().toString());
                }
            }
        });
        this.add(findTestFileBtn);
        
        

        JButton partialTestBtn = new JButton("Lancer un test partiel");
        partialTestBtn.setBounds(0, 50, 200, 50);
        this.add(partialTestBtn);

        JLabel pathToRes = new JLabel();
        pathToRes.setBounds(210, 100, 200, 50);
        this.add(pathToRes);
        
        JButton fullTestBtn = new JButton("Lancer un test complet");
        fullTestBtn.setBounds(0, 100, 200, 50);
        fullTestBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String res = uioInterface.testDataFile();
                pathToRes.setText(Paths.get(res).getFileName().toString());
            }
        });
        this.add(fullTestBtn);

        this.setSize(600, 300);
        this.setLayout(null);
        this.setVisible(true);
    }
}
