package com.smarttersstudio.jewellaryadmin.Pojo;

public class Enquiry {
    String name,number,gos,desc,type,id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGos() {
        return gos;
    }

    public void setGos(String gos) {
        this.gos = gos;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Enquiry() {

    }

    public Enquiry(String name, String number, String gos, String desc, String type, String id) {

        this.name = name;
        this.number = number;
        this.gos = gos;
        this.desc = desc;
        this.type = type;
        this.id = id;
    }
}
