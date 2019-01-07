package com.example.lenovo.myapplicationdemo.Model;

import io.realm.RealmObject;

public class Users extends RealmObject {

    private String grade;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
