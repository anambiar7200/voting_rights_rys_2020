package com.example.voting_rights_rys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//import com.google.android.gms.gcm.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "homeChannelID";
    public static final int NOTIFICATION_ID1 = 12345678;
    private NotificationManagerCompat notificationManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        //Get the Intent which started this activity (address string from Settings)
        Intent intent = getIntent();
        //Using the key to get the bundle (address)
        String pollplace = intent.getStringExtra(Elections.pollingLocation);
        System.out.println("pollplace: "+ pollplace);
        if (pollplace != null) {
            //TEST THAT ADDRESS IS RECEIVED FROM SETTINGS
            TextView polltext = findViewById(R.id.pollingplace);
            polltext.setText(pollplace);
        }

        //Initialize bottom nav bar and select "Home"
        BottomNavigationView bottomNavView = findViewById(R.id.bot_nav);
        bottomNavView.setSelectedItemId(R.id.home);

        TextView nextElec = findViewById(R.id.soonestElection);
        if (Elections.myElections != null) {
            System.out.println("yay there are elections");
            Election node = (Election) Elections.myElections.get(0);
            String election_info = node.getName() + "\n" + node.getDate() + "\n";
            nextElec.setText(election_info);
        }
        else {
            nextElec.setText("You have no upcoming elections at this time. Please check your address or try again later.");
        }

        //Start a new activity when a nav bar item is selected
        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.elections:
                        startActivity(new Intent(getApplicationContext(), Elections.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.resources:
                        startActivity(new Intent(getApplicationContext(), Resources.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }

        });
    }

    //create and send notification
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification (View v){
        System.out.println("Inside sendNotification button");

        //When notification is pressed, direct to Elections class
        Intent intent = new Intent(this, Elections.class);

        //Create a back stack to preserve user navigation experience
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Create the notification
        Notification.Builder notificationTest = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_small_icon)
                .setContentTitle("Test notification")
                .setContentText("test test test")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                //Pass pendingIntent to the app
                .setContentIntent(pendingIntent) //set up the intent to launch when user taps app
                .setAutoCancel(true); //automatically removes app when user taps it

        //Build notification and display notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID1, notificationTest.build());
    }
}