/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.ocrtester.file_io;

import com.alten.ocrtester.preferences.AppPreferences;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author cdeturckheim
 */
public class FileManager {
    /**
     * Checks if path is ok then change the data path in preference file.
     * @param fileLoc
     * @return True if change is done.
     */
    public boolean changePath(String loc) {
        //Vérifier si le chemin est accessible
        File file = new File(loc);
        //Si oui =>
        if(file.exists()){
            
            AppPreferences.getInstance().setPathToData(file.getAbsolutePath());
            return true;
        }
        //si non =>
        else{
            return false;
        }
    }
    
    public File getFileFromPath(){
        File file = new File(AppPreferences.getInstance().getPathToData());
        if(file.exists())
            return file;
        return null;
    }
}
