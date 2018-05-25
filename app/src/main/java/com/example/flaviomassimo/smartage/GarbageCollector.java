package com.example.flaviomassimo.smartage;

public class GarbageCollector {


    private static String Name;
    private static float Latitude,Longitude;
    private static long Empty;
    private static long Soil;
    private static long value;
    private static boolean notificated=false;
    public GarbageCollector(String name,int empty,int soil){
        Name=name;
        Empty=empty;
        Soil=soil;

    }

    public static String getName(){

        return Name;
    }

    public static long getEmptyValue(){

        return Empty;
    }
    public static long getSoil(){

        return Soil;
    }



    public void setPosition(float latitude,float longitude){

        Latitude=latitude;
        Longitude=longitude;
    }
    public static String getPosition(){

        return "Latitude: "+Latitude+", Longitude: "+Longitude;
    }
    public void setValue(long val){
        value=val;

    }
    public float getFullPercentage(){

        if(value!=0 && value<Empty){
            return (Empty-value)/Empty;
        }
        else return 0;
    }
    public long getValue(){

        return value;
    }

    public void setNotificated(Boolean bol){notificated=bol;}

    public boolean getNotificated(){return notificated;}
}
