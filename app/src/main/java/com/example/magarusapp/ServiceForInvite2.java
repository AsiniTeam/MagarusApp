package com.example.magarusapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.magarusapp.firstplay.FirstPlayActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceForInvite2 extends Service {


    public static final String TAG = "ServiceForInvite2";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String USER_STRING = "on_boarding_string";
    public static final String STRING_INVITED_USER_INTENT = "stringForInvitedUser1";
    public static final String STRING_MY_USER_INTENT = "stringForMyUser1";

    private String invitedUser;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "InvitedUser is " + invitedUser);
        Log.i(TAG, "service2 was created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        invitedUser = intent.getStringExtra(STRING_INVITED_USER_INTENT);
        String myName = loadNameFromPhone();
        Log.i(TAG, "InvitedUser is " + invitedUser);
        listenForConfirm(myName, invitedUser);



        return START_NOT_STICKY;

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "The service2 is destroyed");
        Intent intent = new Intent(this, FirstPlayActivity.class);
        stopService(intent);
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




    public  void listenForConfirm(String myName, String invitedUser) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("confirmation");
        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ConfirmationUser user = snapshot.child(myName).child(invitedUser).getValue(ConfirmationUser.class);
                boolean confermation = user.isConfirmation();
                Log.i(TAG, "confermation is " + confermation);
                if (confermation == true) {
                    Intent intent = new Intent(getApplicationContext(), FirstPlayActivity.class);
                    startActivity(intent);
                    ref.child(myName).child(invitedUser).child("confirmation").setValue(false);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Sorry, no internet connection", Toast.LENGTH_LONG).show();
            }
        });

    }



}
