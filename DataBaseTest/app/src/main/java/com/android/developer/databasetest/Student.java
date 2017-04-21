package com.android.developer.databasetest;

/**
 * Created by rodri on 08-Apr-17.
 */

public class Student {
    public String id;
    public String name;
    public String surname;
    public String mark;

    public Student(){

    }
    public Student(String id, String name, String surname, String mark){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.mark = mark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
