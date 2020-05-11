/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.ocrtester.preferences;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author cdeturckheim
 */
public class AppPreferences {

    private static AppPreferences instance = null;
    
    private final String preferenceFile = "config.app";
    private static Properties properties = new Properties();

    /* Propriétées */
    private String pathToData; // chemin absolue vers la racine du jeu de test
    
    private AppPreferences() {
        load();
    }
    
    public static AppPreferences getInstance() {
        if(AppPreferences.instance==null){
            AppPreferences.instance = new AppPreferences();
        }
        return AppPreferences.instance;
    }

    public void load() {
        
        try {
            properties.load(new FileInputStream(preferenceFile));
            this.pathToData = loadProperty("pathToData", properties);
        } catch (IOException ioe) {
            System.err.println("load "+ioe);
        }
    }

    public void savePropertie() {
        try {
            properties.setProperty("pathToData", pathToData);
            properties.store(new FileOutputStream(preferenceFile), null);
        } catch (IOException ioe) {
            System.err.println("savePropertie "+ioe);
        }
    }

    
    private void changeProperty(String propertyName, String propertyValue) {
        try {
            properties.setProperty(propertyName, propertyValue);
            properties.store(new FileOutputStream(preferenceFile), null);
        } catch (IOException ioe) {
            System.err.println("savePropertie "+ioe);
        }
    }
    
    private String loadProperty(String propertyName, Properties properties) {
        try {
            return (String) properties.get(propertyName);
        } catch (NullPointerException npe) {
            System.err.println("loadProperty "+npe);
            return null;
        }
    }

    public String getPathToData() {
        return pathToData;
    }

    public void setPathToData(String pathToData) {
        changeProperty("pathToData", pathToData);
        this.load();
    }
    
    
    

}
