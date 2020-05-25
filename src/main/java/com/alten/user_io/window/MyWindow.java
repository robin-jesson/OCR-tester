/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.user_io.window;

import com.alten.ocrtester.preferences.AppPreferences;
import com.alten.test_data.CV;
import com.alten.user_io.UserIOAppInterface;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author robin.jesson
 */
public class MyWindow extends JFrame {

    private UserIOAppInterface uioInterface = new UserIOAppInterface();
    private AppPreferences pref = AppPreferences.getInstance();
    private JLabel pathDataLabel = new JLabel(pref.getPathToData());
    private JLabel pathResLabel = new JLabel("Résultats");
    private JTable tableRes = new JTable();

    public MyWindow() {
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.add(this.createToolBar(), BorderLayout.NORTH);
        JScrollPane csvComponent = new JScrollPane(this.tableRes);
        contentPane.add(csvComponent);
        contentPane.add(this.createBottomBar(), BorderLayout.SOUTH);

    }

    private JToolBar createToolBar() {
        LinkedList<CV> cvs = this.uioInterface.getAllCv();
        JComboBox cvListDrpDwn = new JComboBox(cvs.stream()
                .map((cv) -> cv.getName()).collect(Collectors.toList())
                .toArray());
        cvListDrpDwn.addActionListener((e) -> partTestListener(e,cvs));
        
        JButton findTestFileBtn = new JButton("Définir le fichier de jeu");
        findTestFileBtn.addActionListener((e) -> this.findTestFileListener(e));

        JButton fullTestBtn = new JButton("Lancer un test complet");
        fullTestBtn.addActionListener((e) -> this.fullTestListener(e));

        JToolBar tb = new JToolBar();
        tb.add(findTestFileBtn);
        tb.add(fullTestBtn);
        tb.add(cvListDrpDwn);
        return tb;
    }

    private JPanel createBottomBar() {
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottom.add(this.pathDataLabel);
        bottom.add(new JLabel("             "));
        bottom.add(this.pathResLabel);
        return bottom;
    }

    private void findTestFileListener(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);
        int val = fileChooser.showOpenDialog(null);
        if (val == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.pathDataLabel.setText(file.toString());
            this.uioInterface.changePathData(file.getAbsolutePath());
            this.pathDataLabel.setText(pref.getPathToData());
        }
    }

    private void fullTestListener(ActionEvent e) {
        String res = uioInterface.testDataFile();
        if (res == null || res.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez reconfigurer le fichier test.");
            return;
        }
        JTable tablenew = getJTableFomCsv(res);
        this.tableRes.setModel(tablenew.getModel());
        this.pathResLabel.setText(res);
    }
    
    private static JTable getJTableFomCsv(String file) {
        Path path = Paths.get(file);
        String header = "";
        Object[][] elts = null;
        Object[] head = null;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            head = lines.remove(0).split(";");
            elts = new Object[lines.size()][head.length];
            for (int i = 0; i < lines.size(); i++) {
                String[] eltLine = lines.get(i).split(";");
                for (int j = 0; j < eltLine.length; j++) {
                    elts[i][j] = eltLine[j];
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        JTable table = new JTable(elts, head);
        return table;
    }
    
    public void partTestListener(ActionEvent e, LinkedList<CV> cvs) {
        JComboBox cb = (JComboBox)e.getSource();
        CV cv = cvs.get(cb.getSelectedIndex());
        String pathToResults = this.uioInterface.testOneCv(cv);
        System.out.println(pathToResults);
        if (pathToResults == null || pathToResults.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez reconfigurer le fichier test.");
            return;
        }
        JTable tablenew = getJTableFomCsv(pathToResults);
        this.tableRes.setModel(tablenew.getModel());
        this.pathResLabel.setText(pathToResults);
    }
}
