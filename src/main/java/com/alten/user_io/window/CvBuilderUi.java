/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.user_io.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author robin.jesson
 */
public class CvBuilderUi extends JFrame{
    private JTextField nameInput = new JTextField("Nom du CV ici");
    private JPanel filesPanel = new JPanel();
    private List<JTextField> filesList = new ArrayList<>();
    private List<JTextField> resultList = new ArrayList<>();
    
    public CvBuilderUi(){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.filesList.add(new JTextField("URL ici"));
        
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.add(this.cvNamePanel(),BorderLayout.NORTH);
        contentPane.add(this.cvFilesPanel(),BorderLayout.CENTER);
    }
    
    private JPanel cvNamePanel() {
        JPanel bottom = new JPanel();
        bottom.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottom.add(this.nameInput);
        return bottom;
    }
    
    private JPanel cvFilesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.updateFilesPanel();
        panel.add(this.filesPanel);
        JButton btnAdd = new JButton("+");
        btnAdd.addActionListener((e)->this.addFilesListener(e));
        panel.add(btnAdd);
        JButton btnRem = new JButton("-");
        btnRem.addActionListener((e)->this.remFilesListener(e));
        panel.add(btnRem);
        return panel;
    }
    
    private void updateFilesPanel(){
        this.filesPanel.removeAll();
        for(int i=0;i<this.filesList.size();i++){
            this.filesPanel.add(this.filesList.get(i));
        }
        this.revalidate();
        this.repaint();
    }
    
    private void addFilesListener(ActionEvent e) {
        this.filesList.add(new JTextField("URL ici"));
        this.updateFilesPanel();
    }
    
    private void remFilesListener(ActionEvent e) {
        if(this.filesList.size()>=2){
            this.filesList.remove(this.filesList.size()-1);
            this.updateFilesPanel();
        }
    }
    
}
