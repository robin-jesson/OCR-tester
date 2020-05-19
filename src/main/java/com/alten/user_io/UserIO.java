/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.user_io;

import com.alten.ocrtester.file_io.FileManager;
import com.alten.ocrtester.preferences.AppPreferences;
import com.alten.test_data.CV;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author cdeturckheim
 */
public class UserIO {
    
    private UserIOAppInterface uioInterface;
    
    public UserIO(){
        this.uioInterface = new UserIOAppInterface();
    }
    
    public void mainMenu() {
        
        int answer;
        
        System.out.println("**********************  MENU  *************************");
        System.out.println("");
        System.out.println("Choisissez l'action :");
        System.out.println("1: Définir le dossier du jeu de données"); //ChangeDataPath
        System.out.println("2: Lancer un test complet");
        System.out.println("3: Lancer un test partiel");
        System.out.println("4: Quitter");
        System.out.println("");
        
        // Get la réponse avec traitement d'erreur
        System.out.print("Votre choix : ");
        Scanner sc = new Scanner(System.in);
        int response = sc.nextInt();
        
        boolean leave = false;
        switch(response){
            case 1:
                this.setDataPath(false);
                break;
            case 2:
                this.fullTest();
                break;
            case 3:
                this.partialTest();
                break;
            case 4:
                System.out.println("Vous quittez l'application.");
                leave = true;
                break;
            default:
                System.out.println("Recommancer votre choix.");
                this.mainMenu();
                break;
        }
        
        if(leave) return;
        this.mainMenu();
    }
    
    
    private void setDataPath(boolean isFromError) {
        AppPreferences pref = AppPreferences.getInstance();
        if(!isFromError)
            System.out.println("**********************  Changer le répertoir  *************************");
        System.out.println("");
        System.out.print("Le chemin actuel est : ");
        System.out.println(pref.getPathToData());
        System.out.println("");
        //System.out.print("Indiquer le chemin absolue de la racine du jeu de données : ");
        System.out.print("Indiquer le chemin du jeu de données : ");
        
        //get
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        
        if(input.equals("q")) return;
        
        if (!this.uioInterface.changePathData(input)) {
            System.out.println("");
            System.out.println("Le chemin spécifié n'est pas atteignable.");
            System.out.println("Merci de recommencer.");
            this.setDataPath(true);
        }
        
        System.out.println("Vous avez choisi : " + pref.getPathToData());
    }
    
    private void fullTest(){
        AppPreferences pref = AppPreferences.getInstance();
        System.out.println("**********************  Lancement d'un test complet  *************************");
        System.out.println("");
        System.out.println("Le chemin du fichier est : " + pref.getPathToData());
        System.out.println("");
        String pathToResults = this.uioInterface.testDataFile();
        if(pathToResults!=null)
            System.out.println("Le chemin du fichier résutlat est : "+pathToResults);
        else
            System.out.println("Une erreur s'est produite.");
    }
    
    private void partialTest(){
        AppPreferences pref = AppPreferences.getInstance();
        System.out.println("**********************  Lancement d'un test partiel  *************************");
        System.out.println();
        System.out.println("Le chemin du fichier est : " + pref.getPathToData());
        System.out.println();
        System.out.println("Choisissez un CV parmi :");
        LinkedList<CV> cvs = this.uioInterface.getAllCv();
        for(int i=0; i<cvs.size(); i++){
            System.out.println(i+1 +" : "+cvs.get(i).getName());
        }
        System.out.print("Votre choix : ");
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        
        CV selectedCv = cvs.get(input - 1);
        System.out.println("Vous avez choisi : " + selectedCv.getName());
        System.out.println();
        String pathToResults = this.uioInterface.testOneCv(selectedCv);
        if(pathToResults!=null)
            System.out.println("Le chemin du fichier résutlat est : "+pathToResults);
        else
            System.out.println("Une erreur s'est produite.");
    }
    
}
