package com.example.magarusapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgs;
import androidx.navigation.NavArgument;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.magarusapp.firstplay.FirstPlayActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AskUserFragment extends Fragment {


    private static final String TAG = "AskUserFragment";
    //public static final String STRING_INVITED_USER = "invited_user1";
    public static String MESSAGE = "would you like to play with me??";
    private static final String USER_STRING = "on_boarding_string";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    public static final String STRING_INVITED_USER_INTENT = "stringForInvitedUser1";
    public static final String STRING_MY_USER_INTENT = "stringForMyUser1";

    Intent mServiceIntent;
    private ServiceForInvite2 mYourService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_user, container, false);

        String invitedUser = AskUserFragmentArgs.fromBundle(getArguments()).getInvitedUser();
        EditText messageText = view.findViewById(R.id.inviteEditText);
        updateViews(messageText, invitedUser);
        Button sendNotificationButton = view.findViewById(R.id.inviteButton);
        String myName = loadUserFromPhone();


        sendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(messageText, invitedUser, myName);
                Intent intent = new Intent(requireContext(), FirstPlayActivity.class);
                intent.putExtra(STRING_INVITED_USER_INTENT, invitedUser);
                startActivity(intent);
            }
        });




        return view;

    }



    //////////////////////////////////////////
    // FUNCTIONS                            //
    //////////////////////////////////////////





    public void updateViews(EditText editText, String name) {
        editText.setHint("Invite " + name);
    }

    public String loadUserFromPhone() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(ON_BOARDING_STRING, Context.MODE_PRIVATE);
        String myName = sharedPreferences.getString(USER_STRING, null);
        return myName;
    }


    public void sendNotification(EditText editText, String invitedUser, String myName) {
       if (editText.getText().toString().isEmpty()) {
           Toast.makeText(requireContext(), "Fatti furbo e scrivi qualcosa", Toast.LENGTH_SHORT).show();
       }
       else {
           MESSAGE = editText.getText().toString();
           sendMessage(MESSAGE, invitedUser, myName);
           startService(invitedUser);
       }
    }


    public void sendMessage( String message, String invitedUser, String myName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("notification")
                .child(invitedUser)
                .child(myName);
        ref.child("notification").setValue(true);
        ref.child("message").setValue(message);
    }


    public void startService (String invitedUser) {
        mYourService = new ServiceForInvite2();
        mServiceIntent = new Intent(requireContext(), mYourService.getClass());
        mServiceIntent.putExtra(STRING_INVITED_USER_INTENT, invitedUser);
        if (!isMyServiceRunning(mYourService.getClass())) {
            requireActivity().startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i (TAG, "Service2 running");
                return true;
            }
        }
        Log.i (TAG, "Service2 not running");
        return false;
    }


}



