/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.ocrtester;

import com.alten.ocrtester.preferences.AppPreferences;
import com.alten.testData.CV;
import com.alten.testData.Result;
import com.alten.userIO.UserIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author cdeturckheim
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        
        System.out.println("Cas simple");
        CV cvOK = TestClass.createOnCV();
        System.out.println(cvOK.toString());
        CV cv2 = CV.fillFromString2(cvOK.toString());
        System.out.println(cv2.toString());
        
        System.out.println("Cas sans result");
        CV cvNoResults = TestClass.createOnCVWithoutResult();
        System.out.println(cvNoResults.toString());
        CV cv4 = CV.fillFromString2(cvNoResults.toString());
        System.out.println(cv4.toString());
        
        
        System.out.println("Cas sans Filename");
        CV cvNoFiles = TestClass.createOnCVWithoutFilesname();
        System.out.println(cvNoFiles.toString());
        CV cv5 = CV.fillFromString2(cvNoFiles.toString());
        System.out.println(cv5.toString());
        
        
        System.out.println("Cas sans Filename ni result");
        CV cvNoFilesNorResults = TestClass.createOnCVWithoutFilesnameNorResults();
        System.out.println(cvNoFilesNorResults.toString());
        
        CV cv6 = CV.fillFromString2(cvNoFilesNorResults.toString());
        System.out.println(cv6.toString());
       
        /*Path path = Paths.get("test.txt");
        System.out.println(Files.exists(path));*/
        
        /*AppPreferences pref = AppPreferences.getInstance();
        System.out.println(pref.getPathToData());
        pref.setPathToData("/user/robin");
        System.out.println(pref.getPathToData());*/
        
        /*File f = new File(".");
        System.out.println(f.getAbsolutePath());*/
    }
    
    private static CV createOnCVWithoutResult() {
        CV cv = new CV();
        cv.setName("Test 1");
        
        ArrayList<String> a = new ArrayList<>();
        a.add("file1.txt");
        a.add("file1.png");
        a.add("file1.jpeg");
        
        cv.setFilesName(a);
        
        return cv;
    }
    
    private static CV createOnCV() {
        CV cv = new CV();
        cv.setName("Test 1");
        
        ArrayList<String> a = new ArrayList<>();
        a.add("file1.txt");
        a.add("file1.png");
        a.add("file1.jpeg");
        
        cv.setFilesName(a);
        LinkedList<Result> r = new LinkedList<>();
        r.add(new Result("A", "A"));
        r.add(new Result("B", "B"));
        r.add(new Result("C", "C"));
        r.add(new Result("D", "D"));
        r.add(new Result("E", "E"));
        cv.setResults(r);
        
        return cv;
    }
    
    private static CV createOnCVWithoutFilesname() {
        CV cv = new CV();
        cv.setName("Test 1");
        
        LinkedList<Result> r = new LinkedList<>();
        r.add(new Result("A", "A"));
        r.add(new Result("B", "B"));
        r.add(new Result("C", "C"));
        r.add(new Result("D", "D"));
        r.add(new Result("E", "E"));
        cv.setResults(r);
        
        return cv;
    }
    
    private static CV createOnCVWithoutFilesnameNorResults() {
        CV cv = new CV();
        cv.setName("Test 1");
        
        return cv;
    }
    
    private static LinkedList<CV> testConstructCV() {
        CV cv = new CV();
        LinkedList<CV> l=  new LinkedList<CV>();
        
        return l;
    }
    
}
