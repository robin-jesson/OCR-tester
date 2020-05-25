/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.user_io;

import com.alten.ocrtester.file_io.FileManager;
import com.alten.test_data.CV;
import com.alten.test_data.Result;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdeturckheim
 */
public class UserIOAppInterface {
    private FileManager fm = new FileManager();
    private final String rootRes = "resultats/";
    /**
     * Changes data path.
     * @param path string
     * @return True if change is done.
     */
    public boolean changePathData(String path) {
        return (this.fm.changePath(path));
    }

    public String testDataFile(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        Date date = new Date();
        File file = this.fm.getFileFromPath();
        File resultFile = new File(rootRes+dateFormat.format(date)+".csv");
        FileWriter fw;
        try {
            fw = new FileWriter(resultFile);
            String headerLine = "NOM_CV;FORMAT;LUMIERE;NB_PAGES;"
                    + "NOM_ATTENDU;NOM_TROUVE;NOM_RES;"
                    + "PRENOM_ATTENDU;PRENOM_TROUVE;PRENOM_RES;"
                    + "EMAIL_ATTENDU;EMAIL_TROUVE;EMAIL_RES;"
                    + "TEL_ATTENDU;TEL_TROUVE;TEL_RES";
            fw.write(headerLine+'\n');
            try{
                LinkedList<CV> cvs = CV.readCVFromFile(file.getPath());
                for(CV cv : cvs){
                    for(String line : cv.getCsvLines())
                        fw.write(line + '\n');
                    
                }
                fw.close();
            }
            catch(NullPointerException npe){
                return null;
            }
        } catch (IOException ex) {
            Logger.getLogger(UserIOAppInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultFile.getAbsolutePath();
        
    }
    
    public String testOneCv(CV cv){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");
        Date date = new Date();
        File file = this.fm.getFileFromPath();
        File resultFile = new File(rootRes+cv.getName()+"_"+dateFormat.format(date)+".csv");
        FileWriter fw;
        try {
            fw = new FileWriter(resultFile);
            String headerLine = "NOM_CV;FORMAT;LUMIERE;NB_PAGES;"
                    + "NOM_ATTENDU;NOM_TROUVE;NOM_RES;"
                    + "PRENOM_ATTENDU;PRENOM_TROUVE;PRENOM_RES;"
                    + "EMAIL_ATTENDU;EMAIL_TROUVE;EMAIL_RES;"
                    + "TEL_ATTENDU;TEL_TROUVE;TEL_RES";
            fw.write(headerLine+'\n');
            for(String line : cv.getCsvLines())
                fw.write(line + '\n');
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(UserIOAppInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultFile.getAbsolutePath();
        
    }
    
    public LinkedList<CV> getAllCv(){
        File file = this.fm.getFileFromPath();
        LinkedList<CV> cvs = CV.readCVFromFile(file.getPath());
        return cvs;
    }
    
    
    
}
