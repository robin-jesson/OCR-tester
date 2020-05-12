/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.user_io;

import com.alten.ocrtester.file_io.FileManager;
import java.io.File;

/**
 *
 * @author cdeturckheim
 */
public class UserIOAppInterface {
    
    /**
     * Changes data path.
     * @param path string
     * @return True if change is done.
     */
    public boolean changePathData(String path) {
        FileManager fm = new FileManager();
        return (fm.changePath(path));
    }
    
    public File getDataFile(){
        FileManager fm = new FileManager();
        return fm.getFileFromPath();
    }
    
}
