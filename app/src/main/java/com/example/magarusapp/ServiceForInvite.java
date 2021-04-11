package com.example.magarusapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.magarusapp.firstplay.FirstPlayActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceForInvite extends Service {

    public static final String TAG = "ServiceForInvite";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String USER_STRING = "on_boarding_string";
    private boolean notificationSent = false;
    private static final long TIME_WAIT_NOTIFICATION = 3000;
    private int j = 0;


    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
//            startMyOwnForeground();
//        else
//            startForeground(1, new Notification());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String name = loadNameFromPhone();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("notification").child(name);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                checkUser(snapshot, ref);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "no internet connection", Toast.LENGTH_LONG).show();
            }
        });

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public String loadNameFromPhone() {
        SharedPreferences pref = getSharedPreferences(ON_BOARDING_STRING, MODE_PRIVATE);
        String name = pref.getString(USER_STRING, null);
        return name;
    }

    public void checkUser(DataSnapshot snapshotNotification, DatabaseReference ref) {

        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("users");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                User user = snapshotUser.getValue(User.class);
                ArrayList<String> namesList = user.getNamesList();

                NotificationUser notificationUser = new NotificationUser();
                Boolean notification = false;
                for (int i = 0; i < namesList.size(); i++) {
                    notificationUser = snapshotNotification.child(namesList.get(i)).getValue(NotificationUser.class);
                    notification = notificationUser.isNotification();
                    String message = notificationUser.getMessage();
                    if (notification == true) {
                        j++;
                        Log.i(TAG, "j is: " + j);
                        sendNotification(namesList.get(i), message);
                        ref.child(namesList.get(i)).child("notification").setValue(false);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ServiceForInvite.this, "No internet connection, sorry", Toast.LENGTH_LONG).show();
            }
        });

    }



    @RequiresApi(Build.VERSION_CODES.O)
    private void sendNotification(String user, String message) {

        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);

        expandedView.setTextViewText(R.id.textViewExpanded1, message);
        collapsedView.setTextViewText(R.id.textNotificationCollapsed2, user + " has a message for you");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setSmallIcon(R.drawable.ic_fagian_notification)
                .setAutoCancel(true)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        Intent intent = new Intent(this, FirstPlayActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notificationBuilder.build());
    }




}
