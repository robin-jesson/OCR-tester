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
 * Objet test d'un CV. <br> 
 * Contient un nom, une liste de fichiers labellisés (TYPE_PAGE.EXTENSION) et une liste chainée des résultats attendus.
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
    
    /**
     * Sérialise l'object CV sous ce format :<br>
     * <code>#CV:nom#filesName:[.|.|...]#result:[.=.|.=.|.=.|...]</code>
     * @return le CV sérialisé
     */
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
    
    /**
     * Construit un objet cv de test à partir de la sérialisation.<br>
     * Inverse du toString.
     * @see #toString &nbsp; pour la sérialisation
     * @param str 
     * @return objet CV
     */
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
    
    /**
     * Construit une liste de CV de test à partir de l'adresse d'un fichier.<br>
     * Celui-ci est alors composé de plusieurs lignes de CV sérialisés.
     * @see #fillFromString2
     * @param file
     * @return liste d'objet CV
     */
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
    
    /**
     * Récupère la réponse d'un champ des résultats
     * @param res
     * @param field
     * @return réponse du champ
     */
    private static String findAnswerByField(LinkedList<Result> res, String field){
        for(Result r : res){
            if(r.getFieldName().equals(field))
                return r.getAnswer();
        }
        return "";
    }   
    
    /**
     * Ecrit une ligne CSV.<br>
     * <code>cv_name;format;lighting;nb pages;lname_waited;lname_found;
     * lname_common_percentage;fname_waited;...;tel_waited;...;mail_waited;...</code>
     * @see #createCSVPercentageFor
     * @param light
     * @param format
     * @param numberPages
     * @param ocrRes
     * @return 
     */
    private String createCSVLine(String light, String format, int numberPages, LinkedList<Result> ocrRes){
        String line = this.name + ";" + format + ";" + light + ";" + numberPages + ";";
        line += this.createCSVPercentageFor("lastname", ocrRes);
        line += this.createCSVPercentageFor("firstname", ocrRes);
        line += this.createCSVPercentageFor("email", ocrRes);
        line += this.createCSVPercentageFor("tel", ocrRes);
        return line;
    }
    
    /**
     * Retourne la partie d'une ligne CV correspondant au comparatif d'un champ du résutlat.<br>
     * Calcul le pourcentage de lettres communes entre ce qui est attendu et ce qui est trouvé.<br>
     * <code>response_waited;response_found;percentage;</code>
     * @see #countCommonLetters
     * @see #findAnswerByField
     * @param field
     * @param ocrRes
     * @return 
     */
    private String createCSVPercentageFor(String field, LinkedList<Result> ocrRes){
        String awaited_res = findAnswerByField(this.results,field);
        String found_res = findAnswerByField(ocrRes,field);
        
        if(awaited_res.isEmpty() && !found_res.isEmpty())
            return "none;"+found_res+";0;";
        else if(!awaited_res.isEmpty() && found_res.isEmpty())
            return awaited_res+";not_found;0;";
        else if (!awaited_res.isEmpty() && !found_res.isEmpty()){
            int commonLetters = CV.countCommonLetters(awaited_res, found_res);
            double percentageCommonLetters = commonLetters / (double) awaited_res.length() * 100;
            return awaited_res + ";" + found_res + ";" + Math.round(percentageCommonLetters) + ";";
        }
        else{
            System.out.println(field +" "+ awaited_res + " "+found_res);
            return "none;none;0;";
        }
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
    
    /**
     * Retourne la lisge complète en CSV correspondant au format et à la lumière donnés en paramètres.<br>
     * La fonction récupère dans le liste des fichiers ceux qui correspondent.<br>
     * L'ocr sera exécutée sur le/les fichiers trouvés. Ainsi un ficher json est crée avec le résultar de l'OCR.<br>
     * La ligne résultat pour le type et la lumière est créée à partir du json trouvé.
     * @see #createCSVLine
     * @param format
     * @param light
     * @return 
     */
    private String getCsvLineForTypeAndLight(String format, String light){
        ArrayList<String> pages = (ArrayList<String>) this.filesName.clone();
        pages.removeIf(file -> !file.contains(format));
        pages.removeIf(file -> !file.contains(light));
        if(pages.size() == 0)
            return "";
        else{
            //TODO programme ocr doit créer un fichier json dans le dossier json puis
            // récupérer le nom de ce fichier
            LinkedList<Result> resFromOCR = JsonCVReader.readJsonFIle("json/robin_jesson.json");
            return this.createCSVLine(light, format, pages.size(), resFromOCR);
        }
    }
    
    /**
     * Créer toutes les lignes CSV pour l'objet CV.
     * @return 
     */
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
