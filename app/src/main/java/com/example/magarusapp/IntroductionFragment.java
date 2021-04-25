package com.example.magarusapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class IntroductionFragment extends Fragment {

    public static final int TIME_TEXT = 8000;
    public static final int TIME_TEXT_ALPHA1 = 4000;
    public static final int TIME_TEXT_ALPHA2 = 2000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_introduction, container, false);


        TextView markText = view.findViewById(R.id.textViewMark);
        markText.setVisibility(View.INVISIBLE);
        TextView asiniText = view.findViewById(R.id.textViewAsini);



        YoYo.with(Techniques.FadeIn).duration(TIME_TEXT_ALPHA1).playOn(asiniText);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                markText.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeIn).duration(TIME_TEXT_ALPHA2).playOn(markText);
            }
        }, TIME_TEXT_ALPHA1);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigation.findNavController(view).navigate(R.id.action_introductionFragment_to_nameFragment);
            }
        }, TIME_TEXT);


        return view;
    }




}