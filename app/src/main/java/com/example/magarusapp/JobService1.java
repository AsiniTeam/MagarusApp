package com.example.magarusapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.magarusapp.firstplay.FirstPlayActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobService1 extends JobService {


    public static final String TAG = "JobService1";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String USER_STRING = "on_boarding_string2";
    private NotificationManagerCompat notificationManager;
    public static final String CHANNEL_1_ID = "channel1";
    private DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("notification");
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "Job Started");
        notificationManager = NotificationManagerCompat.from(this);
        String name = loadNameFromPhone();
        doBackground(name);


        // NOTE: the bacground system is not stopped
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "Job Stopped");
        return true;
    }





    public void doBackground(String name) {
        notificationRef.child(name).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotNotification) {
                checkUser(snapshotNotification, notificationRef, name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }



    public void checkUser(DataSnapshot snapshotNotification, DatabaseReference notificationRef, String myName) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                User user = snapshotUser.getValue(User.class);
                ArrayList<String> namesList = user.getNamesList();
                for (int i = 0; i < namesList.size(); i++) {
                    boolean notification = snapshotNotification.child(namesList.get(i)).child("notification").getValue(boolean.class);
                    String message = snapshotNotification.child(namesList.get(i)).child("message").getValue(String.class);
                    if (notification == true) {
                        sendNotification(namesList.get(i), message);
                        notificationRef.child(myName).child(namesList.get(i)).child("notification").setValue(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "No internet connection, sorry", Toast.LENGTH_LONG).show();
            }
        });
    }





    public String loadNameFromPhone() {
        SharedPreferences pref = getSharedPreferences(ON_BOARDING_STRING, MODE_PRIVATE);
        String name = pref.getString(USER_STRING, null);
        return name;
    }




    private void sendNotification(String user, String message) {

        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);
        expandedView.setTextViewText(R.id.textViewExpanded1, message);
        collapsedView.setTextViewText(R.id.textNotificationCollapsed2, user + " has a message for you");


        Intent intent = new Intent(this, FirstPlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //expandedView.setOnClickPendingIntent(R.id.imageViewExpanded1, pendingIntent);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_fagian_notification)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        notificationManager.notify(1, notification);

    }




}

