/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.ocrtester;

import com.alten.ocrtester.preferences.AppPreferences;
import com.alten.testData.CV;
import com.alten.userIO.UserIO;

/**
 *
 * @author cdeturckheim
 */
public class TerminalMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Chargement des préférences
        
        //Affichage du menu
        UserIO uio = new UserIO();
        uio.mainMenu();
    }
    
}
