package com.project.capstone_design.billcode.login;

public class LoggedInData {
    private String id;
    private String pw;

    public LoggedInData(String id, String pw){
        this.id = id;
        this.pw = pw;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }
}
