package com.example.magarusapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashFragment extends Fragment {


    private static final long SPLASH_TIME = 3500 ;
    private static final String ON_BOARDING_STRING = "on_boarding_pref" ;
    private static final String BOOLEAN_STRING = "on_boarding_boolean";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_splash, container, false);


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(onBoardingFinished()) {
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_homeFragment);
                }
                else {
                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_viewPagerFragment);
                }
            }
        }, SPLASH_TIME);


        return view;


    }

    private boolean onBoardingFinished() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(ON_BOARDING_STRING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(BOOLEAN_STRING, false);
    }


}