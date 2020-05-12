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
import java.util.ArrayList;
import java.util.Arrays;
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
    
    /*public void testDataFile(){
        File file = this.fm.getFileFromPath();
        try{
            ArrayList<CV> cvs = CV.readCVFromFile(file.getPath());
            for(CV cv : cvs){
                System.out.println("Résultats de " + cv.getName() + " : ");
                System.out.println("");
                System.out.println(
                        Arrays.deepToString(cv.getAllOCRResults())
                                .replace("], ", "]\n")
                                .replace("[[", "[")
                                .replace("]]", "]"));
                System.out.println("");
            }
        }
        catch(NullPointerException npe){
            System.err.println("Le fichier est incorrect.");
        }
    }*/

    public void testDataFile(){
        File file = this.fm.getFileFromPath();
        FileWriter fw;
        try {
            fw = new FileWriter(new File("resultats.csv"));
            try{
                LinkedList<CV> cvs = CV.readCVFromFile(file.getPath());
                for(CV cv : cvs){
                    for(String line : cv.getCsvLines())
                        fw.write(line + '\n');
                    
                }
                fw.close();
                System.out.println("Les résultats sont trouvables dans le fichier resultats.csv.");
            }
            catch(NullPointerException npe){
                System.err.println("Le fichier est incorrect.");
            }
        } catch (IOException ex) {
            Logger.getLogger(UserIOAppInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
