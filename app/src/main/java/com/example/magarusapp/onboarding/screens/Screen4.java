package com.example.magarusapp.onboarding.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.magarusapp.R;


public class Screen4 extends Fragment {

    public static final int TIME_TEXT = 8000;
    public static final int TIME_TEXT_ALPHA1 = 4000;
    public static final int TIME_TEXT_ALPHA2 = 2000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_screen4, container, false);


        TextView markText = view.findViewById(R.id.textViewMark);
        markText.setVisibility(View.INVISIBLE);
        TextView asiniText = view.findViewById(R.id.textViewAsini);
        ViewPager2 viewPager2 = requireActivity().findViewById(R.id.onBoardingViewPager);


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
                viewPager2.setCurrentItem(4);
            }
        }, TIME_TEXT);


        return view;
    }




}