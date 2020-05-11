/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.testData;

/**
 *
 * @author cdeturckheim
 */
public class Result {
    
    private String fieldName;
    private String answer;

    public Result(String fieldName, String answer) {
        this.fieldName = fieldName;
        this.answer = answer;
    }
    
    public Result(){
        
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return fieldName+"="+answer;
    }
    
    
    
}
