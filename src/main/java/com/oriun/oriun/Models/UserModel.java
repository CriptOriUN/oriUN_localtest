package com.oriun.oriun.Models;

import java.sql.Date;
import java.util.Collection;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "user")
public class UserModel {

    @Id
    @Column(columnDefinition="text", unique = true, nullable = false)
    private String USER_NAME;

    @Column(columnDefinition="text")
    private String ROL_NAME;
    @Column(columnDefinition="text")
    private String PASSWORD;

    private String EMAIL;

    private boolean ENABLED;

    private int NBANNED;

    private Date LAST_BAN;

    public String getEMAIL() {
        return this.EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public boolean isENABLED() {
        return this.ENABLED;
    }

    public void setENABLED(boolean ENABLED) {
        this.ENABLED = ENABLED;
    }

    //private String TOKEN;

    public UserModel() {
    }

    public UserModel(String user_name,String rol_name, String password) {
        this.USER_NAME = user_name;
        this.ROL_NAME = rol_name;
        this.PASSWORD = password;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getROL_NAME() {
        return ROL_NAME;
    }

    public void setROL_NAME(String ROL_NAME) {
        this.ROL_NAME = ROL_NAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public int getNBANNED() {
        return NBANNED;
    }

    public void setNBANNED(int NBANNED) {
        this.NBANNED = NBANNED;
    }

    public Date getLAST_BAN() {
        return LAST_BAN;
    }

    public void setLAST_BAN(Date LAST_BAN) {
        this.LAST_BAN = LAST_BAN;
    }
}