package com.example.magarusapp.onboarding.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.magarusapp.R;
import com.example.magarusapp.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Screen5 extends Fragment {

    private static final String TAG = "Screen5";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String BOOLEAN_STRING = "on_boarding_boolean3";
    private static final String USER_STRING = "on_boarding_string";
    public static final int TIME_UPLOADING = 3000;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_screen5, container, false);

        EditText editText = view.findViewById(R.id.userEditText);
        Button button = view.findViewById(R.id.userButton);
        ViewPager2 viewPager2 = requireActivity().findViewById(R.id.onBoardingViewPager);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserOnFirebase(editText, view, viewPager2);
            }
        });


        return view;

    }




    public void saveUserOnFirebase(EditText editText, View view, ViewPager2 viewPager2){
        if (editText.getText().toString().isEmpty()) {
            Toast.makeText(requireContext(), "Che cacchio schiacci che poi io schiaccio tua madre", Toast.LENGTH_LONG).show();
        }
        else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null) {
                        Log.i(TAG, "snapshot is (1)" + snapshot);
                        String name = editText.getText().toString();
                        ArrayList<String> namesList = new ArrayList<>();
                        namesList.add(name);
                        ref.child("users").child("namesList").setValue(namesList);
                        setFirebaseFirst(ref, name);
                        onBoardingFinished(name);
                        viewPager2.setCurrentItem(5);

                    } else {
                        Log.i(TAG, "snapshot is (2)" + snapshot);
                        User user = snapshot.child("users").getValue(User.class);
                        ArrayList<String> namesList = user.getNamesList();
                        String name = editText.getText().toString();
                        if (checkUser(namesList, name)) {
                            namesList.add(name);
                            ref.child("users").child("namesList").setValue(namesList);
                            setFirebase(name, ref, namesList);
                            onBoardingFinished(name);
                            viewPager2.setCurrentItem(5);

                        } else {
                            Toast.makeText(requireContext(), "This user already exists", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), "No internet connection, sorry", Toast.LENGTH_LONG).show();
                }
            });


        }
    }





    public boolean checkUser(ArrayList namesList, String name) {
        boolean check = true;
        for (int i = 0; i < namesList.size(); i++) {
            if (namesList.get(i).toString().equalsIgnoreCase(name)) {
                check = false;
            }
        }
        return check;
    }




    private void onBoardingFinished(String nameUser) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(ON_BOARDING_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BOOLEAN_STRING, true);
        editor.putString(USER_STRING, nameUser);
        editor.apply();
    }




    public void setFirebaseFirst(DatabaseReference ref, String name) {
        ref.child("notification").child(name).child(name).child("notification").setValue(false);
        ref.child("notification").child(name).child(name).child("message").setValue("No message written");
        ref.child("confirmation").child(name).child(name).child("confirmation").setValue(false);
    }




    public  void setFirebase(String name, DatabaseReference ref, ArrayList<String> namesList) {
        for (int i = 0; i < namesList.size(); i++) {
            ref.child("notification").child(name).child(namesList.get(i)).child("notification").setValue(false);
            ref.child("notification").child(name).child(namesList.get(i)).child("message").setValue("No message written");
            ref.child("confirmation").child(name).child(namesList.get(i)).child("confirmation").setValue(false);
        }
        for (int i = 0; i < namesList.size(); i++) {
            ref.child("notification").child(namesList.get(i)).child(name).child("notification").setValue(false);
            ref.child("notification").child(namesList.get(i)).child(name).child("message").setValue("No message written");
            ref.child("confirmation").child(namesList.get(i)).child(name).child("confirmation").setValue(false);
        }
    }



}