package com.example.flaviomassimo.smartage;

import java.util.LinkedList;

public class GarbageCollector {


    private  String Name;
    private double Latitude,Longitude;
    private double Empty;
    private double Soil;
    private double value=17;
    private boolean notificated=false;
    public GarbageCollector(String name,double empty,double soil){
        Name=name;
        Empty=empty;
        Soil=soil;

    }

    public  String getName(){

        return Name;
    }

    public  double getEmptyValue(){

        return Empty;
    }
    public  double getSoil(){

        return Soil;
    }



    public void setPosition(double latitude,double longitude){

        Latitude=latitude;
        Longitude=longitude;
    }
    public  String getPosition(){

        return "Latitude: "+Latitude+", Longitude: "+Longitude;
    }

    public  double getLatitude(){return Latitude;}
    public  double getLongitude(){return Longitude;}
    public void setValue(double val){
        value=val;

    }
    public double getFullPercentage(){

        if(value!=0 && value<Empty){

            double val=Empty-value;
            return (Empty-value)/Empty;
        }
        else return 0;
    }
    public double getValue(){

        return value;
    }

    public void setNotificated(Boolean bol){notificated=bol;}

    public boolean getNotificated(){return notificated;}


}
