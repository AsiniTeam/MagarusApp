package com.example.magarusapp;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InviteFragment2 extends Fragment {

    private static final String TAG = "InviteFragment2";
    ArrayList<ImageView> imageViews;
    ArrayList<TextView> textViews;
    ArrayList<LinearLayout> linearLayouts;
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String USER_STRING = "on_boarding_string2";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite, container, false);
        ConstraintLayout constraintLayoutForButtons = view.findViewById(R.id.constraintLayoutForButtons);
        String myName = loadNameFromPhone();
        loadUser(constraintLayoutForButtons, view, myName);
        return view;
    }





    //////////////////////////////////
    // FUNCTIONS                    //
    //////////////////////////////////

    public void loadUser(ConstraintLayout constraintLayoutForButtons, View view, String myName) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               User user = snapshot.getValue(User.class);
                ArrayList<String> namesList = user.getNamesList();
                imageViews     = new ArrayList<>(namesList.size());
                textViews      = new ArrayList<>(namesList.size());
                linearLayouts  = new ArrayList<>(namesList.size());
                for (int i = 0; i < namesList.size(); i++) {
                    createLayout(i, myName, namesList, constraintLayoutForButtons);
                }
                animateButtons(imageViews, textViews);
                checkUserClick(imageViews, textViews, view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "No internet connection, sorry", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void createLayout(int j, String myName, ArrayList<String> namesList, ConstraintLayout constraintLayoutForButtons) {
        View layoutView = getLayoutInflater().inflate(R.layout.layout_for_add_buttons, null, false);
        layoutView.setTranslationX((float) (Math.random()*(500)));
        layoutView.setTranslationY((float) (Math.random()*(500)));
        ImageView imageView = layoutView.findViewById(R.id.userButton);
        updateViews(namesList.get(j), imageView);
        imageViews.add(imageView);
        TextView userText = layoutView.findViewById(R.id.userText);
        textViews.add(userText);
        userText.setText(namesList.get(j));
        linearLayouts.add((LinearLayout) layoutView);
        constraintLayoutForButtons.addView(layoutView);
    }


    public void checkUserClick(ArrayList<ImageView> imageViews, ArrayList<TextView> textViews, View view) {
        for (int i = 0; i < imageViews.size(); i++) {
            int finalI = i;
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = textViews.get(finalI).getText().toString();
                    Log.i(TAG, "name is " + name);
                    InviteFragment2Directions.ActionInviteFragment2ToAskUserFragment action = InviteFragment2Directions.actionInviteFragment2ToAskUserFragment(name);
                    Navigation.findNavController(view).navigate(action);
                }
            });
        }
    }


    public void animateButtons(ArrayList<ImageView> imageViews, ArrayList<TextView> textViews) {
        for (int i = 0; i < imageViews.size(); i++) {
            YoYo.with(Techniques.Pulse)
                    .repeat(YoYo.INFINITE)
                    .repeatMode((int) Math.random()*1000)
                    .playOn(imageViews.get(i));
        }
        for (int i = 0; i < imageViews.size(); i++) {
            YoYo.with(Techniques.Pulse)
                    .repeat(YoYo.INFINITE)
                    .repeatMode((int) Math.random()*1000)
                    .playOn(textViews.get(i));
        }
    }




    public void updateViews(String name, ImageView imageProfile) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("images");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int image = snapshot.child(name).child("image").getValue(int.class);
                switch (image) {
                    case 0:
                        imageProfile.setImageResource(R.drawable.fagiano);
                        break;
                    case 1:
                        imageProfile.setImageResource(R.drawable.asino);

                        break;
                    case 2:
                        imageProfile.setImageResource(R.drawable.dugongo);
                        break;
                    case 3:
                        imageProfile.setImageResource(R.drawable.cinghiale);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Ci dispiace, non c'è connesione a internet", Toast.LENGTH_LONG).show();
            }
        });

    }


    public String loadNameFromPhone() {
        SharedPreferences pref = requireActivity().getSharedPreferences(ON_BOARDING_STRING, requireContext().MODE_PRIVATE);
        String name = pref.getString(USER_STRING, null);
        return name;
    }




}