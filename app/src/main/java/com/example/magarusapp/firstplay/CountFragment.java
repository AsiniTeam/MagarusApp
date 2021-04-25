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
    private static final String USER_STRING = "on_boarding_string2";
    public static final String STRING_INVITED_USER_INTENT = "stringForInvitedUser1";

    private DatabaseReference refConnection = FirebaseDatabase.getInstance().getReference().child("connection");
    private Button buttonCount;
    String myName;
    String invitedUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count, container, false);
        buttonCount = view.findViewById(R.id.countButton);
        Handler handler = new Handler();
        myName = loadNameFromPhone();
        invitedUser = requireActivity().getIntent().getStringExtra(STRING_INVITED_USER_INTENT);
        Log.i(TAG, "invitedUser is: " + invitedUser);

        if (invitedUser == null) {
            refConnection.child(myName).child("second").child("connection").setValue(true);
            refConnection.child(myName).child("second").child("name").setValue(myName);
            refConnection.child(myName).child("second").child("victory").setValue(false);
            Log.i(TAG, "sono passato qui");
            countNull(view);
        }
        else {
            refConnection.child(invitedUser).child("first").child("connection").setValue(true);
            refConnection.child(invitedUser).child("first").child("name").setValue(myName);
            refConnection.child(invitedUser).child("first").child("victory").setValue(false);
            count(view);

        }
        return view;
    }





    ///////////////////////////////
    // FUNCTION                  //
    ///////////////////////////////

    public void count(View view) {
        refConnection.child(invitedUser).child("second").child("connection").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connection = snapshot.getValue(boolean.class);
                if (connection) {
                    Toast.makeText(requireContext(), invitedUser + " si è connesso", Toast.LENGTH_LONG).show();
                    new CountDownTimer(TIME_COUNT, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String countDigitString = String.valueOf(millisUntilFinished / 1000);
                            Log.i(TAG, "countDigit is: " + countDigitString);
                            buttonCount.setText(countDigitString);
                        }
                        @Override
                        public void onFinish() {
                            //checkConnection(view);
                            String refString1 = invitedUser;
                            String refString2 = "first";
                            CountFragmentDirections.ActionCountFragment2ToPlayFragment action = CountFragmentDirections.actionCountFragment2ToPlayFragment(refString1, refString2);
                            Navigation.findNavController(view).navigate(action);
                        }
                    }.start();
                } else {
                    //Toast.makeText(requireContext(), "Aspetta finchè " + invitedUser + " non si connette.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Sorry, no internet connection available", Toast.LENGTH_LONG).show();
            }
        });
    }



    public void checkConnection(View view) {
        refConnection.child(invitedUser).child("second").child("connection").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connection = snapshot.getValue(boolean.class);
                if (connection) {
                    String refString1 = invitedUser;
                    String refString2 = "first";
                    CountFragmentDirections.ActionCountFragment2ToPlayFragment action = CountFragmentDirections.actionCountFragment2ToPlayFragment(refString1, refString2);
                    Navigation.findNavController(view).navigate(action);
                }
                else {
                    Toast.makeText(requireContext(), "Niente, " + invitedUser + " se n'è andato", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Sorry, no internet connection available", Toast.LENGTH_LONG).show();
            }
        });
    }




    public void countNull(View view) {
        refConnection.child(myName).child("first").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connection = snapshot.child("connection").getValue(boolean.class);
                String name = snapshot.child("name").getValue(String.class);
                if (connection) {
                    new CountDownTimer(TIME_COUNT, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String countDigitString = String.valueOf(millisUntilFinished/1000);
                            Log.i(TAG, "countDigit is: " + countDigitString);
                            buttonCount.setText(countDigitString);
                        }
                        @Override
                        public void onFinish() {
                            //checkConnectionNull(view, name);
                            String refString1 = myName;
                            String refString2 = "second";
                            CountFragmentDirections.ActionCountFragment2ToPlayFragment action = CountFragmentDirections.actionCountFragment2ToPlayFragment(refString1, refString2);
                            Navigation.findNavController(view).navigate(action);
                        }
                    }.start();
                } else {
                    Toast.makeText(requireContext(), name + " non ci sta più", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Sorry, no internet connection available", Toast.LENGTH_LONG).show();
            }
        });

    }



    public void checkConnectionNull( View view, String name) {
        refConnection.child(myName).child("first").child("connection").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connection = snapshot.getValue(boolean.class);
                if (connection) {
                    String refString1 = myName;
                    String refString2 = "second";
                    CountFragmentDirections.ActionCountFragment2ToPlayFragment action = CountFragmentDirections.actionCountFragment2ToPlayFragment(refString1, refString2);
                    Navigation.findNavController(view).navigate(action);
                }
                else {
                    Toast.makeText(requireContext(), "E niente, " + name + " se n'è andato ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
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
            Log.i(TAG, "onStop is called");
        }
        else {
            //refConnection.child(invitedUser).child("first").child("connection").setValue(false);
            Log.i(TAG, "onStop is called");
        }
    }


}