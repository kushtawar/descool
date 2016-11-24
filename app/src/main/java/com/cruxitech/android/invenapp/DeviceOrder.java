package com.cruxitech.android.invenapp;

/**
 * Created by kushtawar on 30/07/16.
 */
public class DeviceOrder {

    private String devuniqueid;
    private String devtype;
    private String devlocation;
    private String devmanufacturer;
    private String deviceno ;
    private String deviceowner;
    private String devicemodel;
    private String lastupdatedon;
    private String lastupdatedby;
    private String imgdevtype;

    public String getDevuniqueid() {
        return devuniqueid;
    }
    public void setDevuniqueid(String devuniqueid) {
        this.devuniqueid = devuniqueid;
    }


    public String getDevTypeImage() {
        return imgdevtype;
    }
    public void setDevTypeImage(String imgdevtype) {
        this.imgdevtype = imgdevtype;
    }


    public String getDevtype() {
       return devtype;
    }
    public void setDevtype(String devtype) {
        this.devtype = devtype;
    }

    public String getDevlocation() {
        return devlocation;
    }
    public void setDevlocation(String devlocation) {
        this.devlocation = devlocation;
    }

    public String getDevmanufacturer() {
        return devmanufacturer;
    }
    public void setDevmanufacturer(String devmanufacturer) {
        this.devmanufacturer = devmanufacturer;
    }


    public String getDevno() {
        return deviceno;
    }
    public void setDevno(String devno) {
        this.deviceno = devno;
    }

    public String getDevowner() {
        return deviceowner;
    }
    public void setDevowner(String devowner) {
        this.deviceowner = devowner;
    }

    public String getDevicemodel() {
        return devicemodel;
    }
    public void setDevicemodel(String devicemodel) {
        this.devicemodel = devicemodel;
    }

    public String getLastupdatedon()
    {
        return lastupdatedon;
    }
    public void setLastupdatedon(String lastupdatedon)
    {
        this.lastupdatedon = lastupdatedon;
    }

    public String getLastupdatedby()
    {
        return lastupdatedby;
    }
    public void setLastupdatedby(String lastupdatedby)
    {
        this.lastupdatedby = lastupdatedby;
    }





    public String toString() {
        return devtype + " : " + deviceno + " : " + deviceowner;
    }


}
