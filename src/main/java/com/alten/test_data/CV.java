/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.test_data;

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
    
    public static LinkedList<CV> readCVFromFile(String file){
        LinkedList<CV> cvs = new LinkedList<>();
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
    
    private static String findAnswerByField(LinkedList<Result> res, String field){
        for(Result r : res){
            if(r.getFieldName().equals(field))
                return r.getAnswer();
        }
        return "";
    }   
    
    private String createCSVLine(String light, String format, int numberPages, LinkedList<Result> ocrRes){
        //cv_name;format;lighting;lname_waited;lname_found;lname_common_percentage;fname_wated;...;tel_waited;...;mail_waited;... \n
        
        String line = this.name + ";" + format + ";" + light + ";" + numberPages + ";";
        line += this.createCSVPercentageFor("lastname", ocrRes);
        line += this.createCSVPercentageFor("firstname", ocrRes);
        line += this.createCSVPercentageFor("email", ocrRes);
        line += this.createCSVPercentageFor("tel", ocrRes);
        return line;
    }
    
    private String createCSVPercentageFor(String field, LinkedList<Result> ocrRes){
        String awaited_res = findAnswerByField(this.results,field);
        String found_res = findAnswerByField(ocrRes,field);
        
        if(awaited_res.isEmpty() || found_res.isEmpty())
            return "none;none;0;";
        
        int commonLetters = CV.countCommonLetters(awaited_res, found_res);
        double percentageCommonLetters = commonLetters / (double) awaited_res.length() * 100;
        return awaited_res + ";" + found_res + ";" + Math.round(percentageCommonLetters) + ";";
    }
    
    private static int countCommonLetters(String reference, String toTest){
        if(reference.isEmpty() || toTest.isEmpty())
            return 0;
        
        reference = reference.toLowerCase();
        toTest = toTest.toLowerCase();
        int res = 0;
        int minLength = Math.min(reference.length(),toTest.length());
        for(int i=0;i<minLength;i++){
            if(reference.charAt(i) == toTest.charAt(i) ){
                res++;
            }
        }
        return res;
    }
    
    private String getCsvLineForTypeAndLight(String format, String light){
        ArrayList<String> pages = (ArrayList<String>) this.filesName.clone();
        pages.removeIf(file -> !file.contains(format));
        pages.removeIf(file -> !file.contains(light));
        if(pages.size() == 0)
            return "";
        else{
            LinkedList<Result> resFromOCR = new LinkedList<>();
            //TODO resFromOCR = ocr(pages), json to linkedlist
            resFromOCR.add(new Result("lastname","Jesson"));
            resFromOCR.add(new Result("firstname","Robin"));
            resFromOCR.add(new Result("email","robin.jessan@utbm.fr"));
            return this.createCSVLine(light, format, pages.size(), resFromOCR);
        }
    }
    
    public LinkedList<String> getCsvLines(){
        LinkedList<String> lines = new LinkedList<>();
        
        String pnglow = this.getCsvLineForTypeAndLight("png", "low");
        String pngamb = this.getCsvLineForTypeAndLight("png", "amb");
        String pnghig = this.getCsvLineForTypeAndLight("png", "hig");
        String heiflow = this.getCsvLineForTypeAndLight("heif", "low");
        String heifamb = this.getCsvLineForTypeAndLight("heif", "amb");
        String heifhig = this.getCsvLineForTypeAndLight("heif", "hig");
        String jpglow = this.getCsvLineForTypeAndLight("jpg", "low");
        String jpgamb = this.getCsvLineForTypeAndLight("jpg", "amb");
        String jpghig = this.getCsvLineForTypeAndLight("jpg", "hig");
        String pdflow = this.getCsvLineForTypeAndLight("pdf", "low");
        String pdfamb = this.getCsvLineForTypeAndLight("pdf", "amb");
        String pdfhig = this.getCsvLineForTypeAndLight("pdf", "hig");
        String pdfsca = this.getCsvLineForTypeAndLight("pdf", "sca");
        
        if(!pnglow.isEmpty()) lines.add(pnglow);
        if(!pngamb.isEmpty()) lines.add(pngamb);
        if(!pnghig.isEmpty()) lines.add(pnghig);
        
        if(!heiflow.isEmpty()) lines.add(heiflow);
        if(!heifamb.isEmpty()) lines.add(heifamb);
        if(!heifhig.isEmpty()) lines.add(heifhig);
        
        if(!jpglow.isEmpty()) lines.add(jpglow);
        if(!jpgamb.isEmpty()) lines.add(jpgamb);
        if(!jpghig.isEmpty()) lines.add(jpghig);
        
        if(!pdflow.isEmpty()) lines.add(pdflow);
        if(!pdfamb.isEmpty()) lines.add(pdfamb);
        if(!pdfhig.isEmpty()) lines.add(pdfhig);
        
        if(!pdfsca.isEmpty()) lines.add(pdfsca);
        
        return lines;
    }
}
