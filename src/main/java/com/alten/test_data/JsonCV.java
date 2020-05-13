/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alten.test_data;

import java.util.LinkedList;

/**
 *
 * @author robin.jesson
 */
public class JsonCV {
    private String lastname;
    private String firstname;
    private String tel;
    private String email;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public LinkedList<Result> toLinkedList(){
        LinkedList<Result> res = new LinkedList<>();
        if(this.lastname!=null) res.add(new Result("lastname",this.lastname));
        if(this.firstname!=null) res.add(new Result("firstname",this.firstname));
        if(this.tel!=null) res.add(new Result("tel",this.tel));
        if(this.email!=null) res.add(new Result("email",this.email));
        return res;
    }
    
    
}
