package com.example.flaviomassimo.smartage;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;

public class SharingValues {

    private static String FullName="";
    private static boolean logout=false;
    private static FirebaseUser user;
    private static FirebaseAuth auth;
    private static LinkedList<GarbageCollector> CollectorList= new LinkedList<>();

    public static void setFullName(String name){

        FullName=name;
    }
    public static String getName(){
        return FullName;
    }
    public static void setCurrentUser(FirebaseUser u){

        user=u;
    }


    public static FirebaseAuth getCurrentUserAuth(){

        return auth;
    }


public static void setLogOut(Boolean b){

        logout=b;
}

public static boolean getLogOut(){

        return logout;
    }
    public static void setCurrentUserAuth(FirebaseAuth u){

        auth=u;
    }


    public static FirebaseUser getCurrentUser(){

        return user;
    }
public static LinkedList<GarbageCollector> getGarbageCollectors(){


        return CollectorList;
}


}
