package com.example.flaviomassimo.smartage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseUser user;
    private TextView t;
    LinkedList<GarbageCollector> list=SharingValues.getGarbageCollectors();
    private boolean created=false;
    private NotificationChannel channel;
    private NotificationManager mNotificationManager ;
    private NotificationCompat.Builder mBuilder;
    private Intent intentNotification;
    private PendingIntent pi;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    Thread GARBAGE_THREAD;
    private String position="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        t=(TextView) findViewById(R.id.distance);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("GCs");
        if(!created) {
            createChannel("GARBAGE_CHANNEL", "CHANNEL FOR FULL GARBAGE COLLECTOR NOTIFICATION");
            createNotification();


            ValueEventListener valEvent =(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Iterable<DataSnapshot> value = dataSnapshot.getChildren();
                    Iterator it= value.iterator();
                    System.out.println("pre iteratore");
                    while(it.hasNext()){
                        DataSnapshot data=(DataSnapshot) it.next();
                        GarbageCollector garbage= new GarbageCollector(data.getKey(),20,7);
                        garbage.setValue((long)data.child("distance").getValue());
                        System.out.println((long)data.child("distance").getValue());
                        if(!list.contains(garbage)){
                            list.add(garbage);
                            System.out.println("Added garbageCollector"+garbage);
                        }
                        else {

                            for(GarbageCollector g:list){
                                if(g.getName().equals(garbage.getName())){
                                    if(g.getValue()!=garbage.getValue()){
                                        g.setValue(garbage.getValue());
                                        System.out.println(g.getValue());

                                    }

                                }
                                System.out.println(garbage.getValue());
                                System.out.println(garbage.getFullPercentage()*100+"%");
                            }
                        }



                        if(garbage.getValue()>10){
                            garbage.setNotificated(false);
                        }

                        if(garbage.getValue()>=0 && garbage.getValue()<=7){
                            if(!garbage.getNotificated()){
                                position=garbage.getPosition();
                                createNotification("Warning!", "the garbage collector at position "+position+" is full!", R.drawable.cottontrash);
                                mNotificationManager.notify(0, mBuilder.build());
                                garbage.setNotificated(true);

                            }

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

            GARBAGE_THREAD = new Thread(new Garbage_Thread(channel, mNotificationManager, mBuilder, pi, valEvent));
            GARBAGE_THREAD.start();
            System.out.println("thread partito");
            created=true;

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent i = new Intent(MenuActivity.this,ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.map) {
            Intent i = new Intent(MenuActivity.this,MapsActivity.class);
            startActivity(i);
        } else if (id == R.id.path) {

        } else if (id == R.id.info) {
            Intent i = new Intent(MenuActivity.this,AboutActivity.class);
            startActivity(i);
        }
        else if (id == R.id.out) {
            SharingValues.setLogOut(true);
            Intent i = new Intent(MenuActivity.this,LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    public void createChannel(String title, String content) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        channel = new NotificationChannel("01", title, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(content);
        mNotificationManager.createNotificationChannel(channel);
        System.out.println("Canale creato");
    }


    public void createNotification(){


        System.out.println("ENTRATO NEL BUILDER");
        mBuilder = new NotificationCompat.Builder(this,channel.getId() );
        intentNotification = new Intent(getApplicationContext(), MenuActivity.class);
        pi = PendingIntent.getActivity(this, 0, intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);



    }

    public void createNotification(String title, String text,int Icon){


        mBuilder.setSmallIcon(Icon);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        mBuilder.setContentIntent(pi);


    }
}
