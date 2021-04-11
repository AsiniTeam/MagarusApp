package com.example.magarusapp.firstplay;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.magarusapp.ConnectionUser;
import com.example.magarusapp.MainActivity;
import com.example.magarusapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CountFragment extends Fragment {

    public static final String TAG = "CountFragment";
    public static final int TIME_COUNT = 10000;
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String USER_STRING = "on_boarding_string";
    public static final String STRING_INVITED_USER_INTENT = "stringForInvitedUser1";

    private DatabaseReference refConnection = FirebaseDatabase.getInstance().getReference().child("connection");
    private Button buttonCount;
    private Handler handler;
    String myName;
    String invitedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count, container, false);
        buttonCount = view.findViewById(R.id.countButton);
        handler = new Handler();
        myName = loadNameFromPhone();
        invitedUser = requireActivity().getIntent().getStringExtra(STRING_INVITED_USER_INTENT);
        Log.i(TAG, "invitedUser is: " + invitedUser);

        if (invitedUser == null) {
            refConnection.child(myName).child("second").child("connection").setValue(true);
            countNull(view);
        }
        else {
            refConnection.child(invitedUser).child("first").child("connection").setValue(true);
            Long time = System.currentTimeMillis();
            refConnection.child(invitedUser).child("first").child("initTime").setValue(time);
            refConnection.child(invitedUser).child("first").child("name").setValue(invitedUser);
            count(view);
        }
        return view;
    }





    ///////////////////////////////
    // FUNCTION                  //
    ///////////////////////////////

    public void count(View view) {
        final int[] countDigit = {Integer.parseInt(buttonCount.getText().toString())};
        new CountDownTimer(TIME_COUNT, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                countDigit[0] = countDigit[0] - 1;
                String countDigitString = String.valueOf(countDigit[0]);
                Log.i(TAG, "countDigit is: " + countDigit[0]);
                buttonCount.setText(countDigitString);
            }

            @Override
            public void onFinish() {
                checkConnection(view);
            }
        }.start();
    }



    public void checkConnection(View view) {
        refConnection.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ConnectionUser user = snapshot.child(invitedUser).child("second").getValue(ConnectionUser.class);
                boolean connection = user.isConnection();
                if (connection == true) {
                    Navigation.findNavController(view).navigate(R.id.action_countFragment2_to_playFragment);
                    refConnection.child(invitedUser).child("second").child("connection").setValue(false);
                }
                else {
                    Toast.makeText(requireContext(), "Sorry, " + invitedUser + " has not accepted in time", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Sorry, no internet connection available", Toast.LENGTH_LONG).show();
            }
        });

    }



    public void countNull(View view) {
        refConnection.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ConnectionUser user = snapshot.child(myName).child("first").getValue(ConnectionUser.class);
                boolean connection = user.isConnection();
                String name = user.getName();
                long refTime = user.getInitTime();

                if( connection == true) {
                    int TIME_COUNT_NULL = (int) (System.currentTimeMillis() - refTime);
                    TIME_COUNT_NULL = TIME_COUNT - TIME_COUNT_NULL;
                    final int[] TIME_COUNTER_SECOND = {TIME_COUNT_NULL / 1000};
                    new CountDownTimer(TIME_COUNT_NULL, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            TIME_COUNTER_SECOND[0] = TIME_COUNTER_SECOND[0] - 1;
                            String countDigitString = String.valueOf(TIME_COUNTER_SECOND[0]);
                            buttonCount.setText(countDigitString);
                        }

                        @Override
                        public void onFinish() {
                            Navigation.findNavController(view).navigate(R.id.action_countFragment2_to_playFragment);
                            refConnection.child(myName).child("first").child("connection").setValue(false);
                        }

                    };
                }

                else {
                    Toast.makeText(requireContext(), "Sorry, " + name + " is no longer connected", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Sorry, no internet connection available", Toast.LENGTH_LONG).show();
            }
        });

    }




    public String loadNameFromPhone() {
        SharedPreferences pref = requireContext().getSharedPreferences(ON_BOARDING_STRING, requireActivity().MODE_PRIVATE);
        String name = pref.getString(USER_STRING, null);
        return name;
    }


    @Override
    public void onStop() {
        super.onStop();
        if(invitedUser == null) {
            refConnection.child(myName).child("second").child("connection").setValue(false);
        }
        else {
            refConnection.child(invitedUser).child("first").child("connection").setValue(false);
        }
    }
}