/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.test_data;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author robin.jesson
 */
public class JsonCVReader {
    public static LinkedList<Result> readJsonFIle(String file){
        Gson gson = new Gson();
        try {
            JsonCV jsonCV = gson.fromJson(new FileReader(file), JsonCV.class);
            return jsonCV.toLinkedList();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JsonCVReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
