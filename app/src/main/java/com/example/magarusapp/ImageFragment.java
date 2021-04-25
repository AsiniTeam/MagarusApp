package com.example.magarusapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ImageFragment extends Fragment {

    public static final int TIME_TEXT = 8000;
    public static final String TAG = "Screen6";
    private static final String USER_STRING = "on_boarding_string2";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        ViewPager2 viewPager2 = requireActivity().findViewById(R.id.onBoardingViewPager);

        ImageView fagiano = view.findViewById(R.id.fagiano);
        ImageView asino = view.findViewById(R.id.asino);
        ImageView dugongo = view.findViewById(R.id.dugongo);
        ImageView cinghiale = view.findViewById(R.id.cinghiale);
        ImageView bono = view.findViewById(R.id.bono);

        ArrayList<ImageView> images = new ArrayList<>();
        images.add(fagiano);  // 0 fagiano
        images.add(asino);   // 1 asini
        images.add(dugongo); // 2 dugongo
        images.add(cinghiale); // 3 cinghiale
        images.add(bono);


        String myName = loadNameFromPhone();

        setFirebase(images, view, myName);


        for (int i = 0; i < images.size(); i++) {
            YoYo.with(Techniques.Wobble).duration(2000).playOn(images.get(i));
        }


        return view;
    }




    public String loadNameFromPhone() {
        SharedPreferences pref = requireContext().getSharedPreferences(ON_BOARDING_STRING, requireActivity().MODE_PRIVATE);
        String name = pref.getString(USER_STRING, null);
        return name;
    }





    public void setFirebase(ArrayList<ImageView> images, View view, String myName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        for (int i = 0; i < images.size() - 1; i++) {
            int finalI = i;
            images.get(finalI).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "myName is: " + myName);
                    ref.child("images").child(myName).child("image").setValue(finalI);
                    Navigation.findNavController(view).navigate(R.id.action_imageFragment_to_homeFragment);
                }
            });
        }
    }

}