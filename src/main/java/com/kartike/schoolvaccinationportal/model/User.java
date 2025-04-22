package com.kartike.schoolvaccinationportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "users_data")
@Data
public class User {

    @Id
    private String useridd;
    private String username;
    public String password;
    public String emailId; 
    public String contactNo; 
    public User() {
    }


    public User(String username, String password, String emailId,String contactNo) {
        this.username = username;
        this.password = password;
        this.emailId = emailId;
        this.contactNo = contactNo;
    }
}
