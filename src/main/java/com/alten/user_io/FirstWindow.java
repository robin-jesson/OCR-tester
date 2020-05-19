/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.user_io;

import com.alten.ocrtester.preferences.AppPreferences;
import com.alten.test_data.CV;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author robin.jesson
 */
public class FirstWindow extends JFrame {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 200; 

    public FirstWindow() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
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

        JTable tableres = new JTable();
        tableres.setBounds(0, 200, 1000, 300);
        this.add(tableres);
        
        JLabel pathToRes = new JLabel();
        pathToRes.setBounds(210, 50, 100, 50);
        pathToRes.setText("xcsc");
        this.add(pathToRes);
        
        JButton fullTestBtn = new JButton("Lancer un test complet");
        fullTestBtn.setBounds(0, 100, 200, 50);
        fullTestBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                String res = uioInterface.testDataFile();
                if(res == null) {
                    JOptionPane.showMessageDialog(null, "Veuillez reconfigurer le fichier test.");
                    return;
                }
                System.out.println(res);
                JTable tablenew = getJTableFomCsv(res);
                
                tableres.setModel(tablenew.getModel());
            }
        });
        this.add(fullTestBtn);

        this.setSize(600, 300);
        this.setLayout(null);
        this.setVisible(true);
    }
    
    private static JTable getJTableFomCsv(String file){
        Path path = Paths.get(file);
        String header = "";
        Object[][] elts = null;
        Object[] head = null;
        try{
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            head = lines.remove(0).split(";");
            elts = new Object[lines.size()][head.length];
            for(int i=0;i<lines.size();i++){
                String[] eltLine = lines.get(i).split(";");
                for(int j=0;j<eltLine.length;j++){
                    elts[i][j] = eltLine[j];
                }
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
        JTable table = new JTable(elts,head);
        return table;
    }
}
