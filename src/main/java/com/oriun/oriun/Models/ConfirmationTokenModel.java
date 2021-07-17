package com.oriun.oriun.Models;

import java.sql.Date;
import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name = "confirmation_token")
public class ConfirmationTokenModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer TOKEN_ID;
    private String CONFIRMATION_TOKEN;
    private Date CREATE_DATE;
    private String USER_NAME;

    public Integer getTOKEN_ID() {
        return this.TOKEN_ID;
    }

    public void setTOKEN_ID(Integer TOKEN_ID) {
        this.TOKEN_ID = TOKEN_ID;
    }

    public String getCONFIRMATION_TOKEN() {
        return this.CONFIRMATION_TOKEN;
    }

    public void setCONFIRMATION_TOKEN(String CONFIRMATION_TOKEN) {
        this.CONFIRMATION_TOKEN = CONFIRMATION_TOKEN;
    }

    public Date getCREATE_DATE() {
        return this.CREATE_DATE;
    }

    public void setCREATE_DATE(Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public String getUSER_NAME() {
        return this.USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }
    public ConfirmationTokenModel() {
        
    }
    public ConfirmationTokenModel(String user) {
        this.USER_NAME = user;
        java.util.Date d1 = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(d1.getTime());
        this.CREATE_DATE = sqlDate;
        this.CONFIRMATION_TOKEN = UUID.randomUUID().toString();
    }
}
