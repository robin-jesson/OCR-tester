/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.testData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author cdeturckheim
 */
public class CV {
    
    private String name;
    private ArrayList<String> filesName;
    private LinkedList<Result> results;

    public CV() {
        this.filesName = new ArrayList<>();
        this.results = new LinkedList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilesName(ArrayList<String> filesName) {
        this.filesName = filesName;
    }

    public void setResults(LinkedList<Result> results) {
        this.results = results;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFilesName() {
        return filesName;
    }

    public LinkedList<Result> getResults() {
        return results;
    }
    
    
    
    

    @Override
    public String toString() {
        String str = "";
        str +=  "#CV:"+this.name+"#";
        
        str += "filesName:[";
        boolean first = true;
        for (String fileName : this.filesName) {
            if (first) {
                first = false;
            } else {
                str += "|";
            }
            str += fileName;
        }
        str += "]#";
        
        str += "result:[";
        first = true;
        for(Result result : this.results){
            if(first)
                first = false;
            else
                str += "|";
            str += result;
        }
        str += "]";
        
        //split 
        // struc fichier : #key:value
        // if array : #key:[val1|val2|val3]
        return str;
    }
    
    public static CV fillFromString2(String str){
        //récupérer les 3 composantes d'un cv en un tableau 
        //["", "CV:_", "filesName:_", "results:_"]
        CV cv = new CV();
        String[] elements = str.split("#");
        
        for(int i=0;i<elements.length;i++){
            String[] element = elements[i].split(":");
            if(element.length==2){
                switch(element[0]){
                    case "CV":
                        cv.name = element[1];
                        break;
                    case "filesName":
                        String files = element[1].replaceAll("[\\[\\]]", "");
                        for(String file : files.split("\\|")){
                            cv.filesName.add(file);
                        }
                        break;
                    case "result": 
                        String results = element[1].replaceAll("[\\[\\]]", "");
                        for(String result : results.split("\\|")){
                            if(result.split("=").length==2){
                                String fieldName = result.split("=")[0];
                                String answer = result.split("=")[1];
                                cv.results.add(new Result(fieldName,answer));
                            }
                        }
                        break;
                }
            }
        }
        return cv;
    }
    
    public static ArrayList<CV> readCVFromFile(String file){
        ArrayList<CV> cvs = new ArrayList<>();
        Path path = Paths.get(file);
        try{
            for(String line : Files.readAllLines(path, StandardCharsets.UTF_8)){
                CV cv = CV.fillFromString2(line);
                cvs.add(cv);
            }
        }
        catch(Exception e){
            System.err.println(e);
        }
        return cvs;
    }

    public double compareResults(LinkedList<Result> resultsToTest){
        double sum = 0.0;
        if(CV.findAnswerByField(this.results,"lastname").equals(CV.findAnswerByField(resultsToTest,"lastname"))
                && !CV.findAnswerByField(this.results,"lastname").isEmpty()
                && !CV.findAnswerByField(resultsToTest,"lastname").isEmpty())
            sum++;
        if(CV.findAnswerByField(this.results,"firstname").equals(CV.findAnswerByField(resultsToTest,"firstname"))
                && !CV.findAnswerByField(this.results,"firstname").isEmpty()
                && !CV.findAnswerByField(resultsToTest,"firstname").isEmpty())
            sum++;
        if(CV.findAnswerByField(this.results,"email").equals(CV.findAnswerByField(resultsToTest,"email"))
                && !CV.findAnswerByField(this.results,"email").isEmpty()
                && !CV.findAnswerByField(resultsToTest,"email").isEmpty())
            sum++;
        if(CV.findAnswerByField(this.results,"telephone").equals(CV.findAnswerByField(resultsToTest,"telephone"))
                && !CV.findAnswerByField(this.results,"telephone").isEmpty()
                && !CV.findAnswerByField(resultsToTest,"telephone").isEmpty())
            sum++;
        return sum/ this.results.size();
    }
    
    private static String findAnswerByField(LinkedList<Result> res,String field){
        for(Result r : res){
            if(r.getFieldName().equals(field))
                return r.getAnswer();
        }
        return "";
    }
    
    public double getOCRResultsForTypeAndLight(String type, String light){
        System.out.print(type+" "+light+" : ");
        double d = -1.0;
        ArrayList<String> pages = (ArrayList<String>) this.filesName.clone();
        pages.removeIf(file -> !file.contains(type));
        pages.removeIf(file -> !file.contains(light));
        System.out.println(pages);
        if(pages.isEmpty())
            return -1;
        //resFromOCR = OCR(pages) //JSON -> linkedlist
        LinkedList<Result> resFromOCR = new LinkedList<>();
        resFromOCR.add(new Result("lastname","Jesson"));
        resFromOCR.add(new Result("firstname","Robin"));
        resFromOCR.add(new Result("email","robin.jessan@utbm.fr"));
        return this.compareResults(resFromOCR);
    }
    
    public double[][] getAllOCRResults(){
        double[][] res = new double[4][4];
        
//         PNG HEIF JPG PDF    
//     LOW|0-0|0--1|0-2|0-3|     
//     AMB|1-0|1--1|1-2|1-3|   
//     HIG|2-0|2--1|2-2|2-3|  
//     SCA|===|====|===|3-3|
        
        res[0][0] = this.getOCRResultsForTypeAndLight("png", "low");
        res[1][0] = this.getOCRResultsForTypeAndLight("png", "amb");
        res[2][0] = this.getOCRResultsForTypeAndLight("png", "hig");
        res[0][1] = this.getOCRResultsForTypeAndLight("heif", "low");
        res[1][1] = this.getOCRResultsForTypeAndLight("heif", "amb");
        res[2][1] = this.getOCRResultsForTypeAndLight("heif", "hig");
        res[0][2] = this.getOCRResultsForTypeAndLight("jpg", "low");
        res[1][2] = this.getOCRResultsForTypeAndLight("jpg", "amb");
        res[2][2] = this.getOCRResultsForTypeAndLight("jpg", "hig");
        res[0][3] = this.getOCRResultsForTypeAndLight("pdf", "low");
        res[1][3] = this.getOCRResultsForTypeAndLight("pdf", "amb");
        res[2][3] = this.getOCRResultsForTypeAndLight("pdf", "hig");
        
        res[3][3] = this.getOCRResultsForTypeAndLight("pdf", "sca");
        
        return res;
    }
}
