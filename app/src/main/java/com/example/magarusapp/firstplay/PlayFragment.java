package com.example.magarusapp.firstplay;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.magarusapp.MainActivity;
import com.example.magarusapp.R;
import com.example.magarusapp.ServiceForInvite2;


public class PlayFragment extends Fragment {

    private Handler handler = new Handler();
    private static final long TIME_MOVING = 500;
    private static final long TIME_FINISH = 10000;
    private ImageView fagianRunner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        fagianRunner = view.findViewById(R.id.fagianRunner);
        handler.postDelayed(runnable, TIME_MOVING);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigation.findNavController(view).navigate(R.id.action_playFragment_to_finishFirstPlayFragment);
            }
        }, TIME_FINISH);

        return view;

    }




    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            fagianRunner.setTranslationX((float) (Math.random()*500 - Math.random()*500));
            fagianRunner.setTranslationY((float) (Math.random()*500 - Math.random()*500));
            handler.postDelayed(this::run, TIME_MOVING);
        }
    };






}