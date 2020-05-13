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
    /**
     * Changes data path.
     * @param path string
     * @return True if change is done.
     */
    public boolean changePathData(String path) {
        return (this.fm.changePath(path));
    }

    public String testDataFile(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        File file = this.fm.getFileFromPath();
        System.out.println("./resultats/"+dateFormat.format(date)+".csv");
        File resultFile = new File("./resultats/"+dateFormat.format(date)+".csv");
        FileWriter fw;
        try {
            fw = new FileWriter(resultFile);
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
    
}
