package com.example.flaviomassimo.smartage;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SharingValues {


    private static boolean logout=false;
    private static FirebaseUser user;
    private static FirebaseAuth auth;
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

}
