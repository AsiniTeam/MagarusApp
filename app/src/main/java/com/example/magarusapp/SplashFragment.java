package com.example.magarusapp;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashFragment extends Fragment {


    private static final long SPLASH_TIME = 4000 ;
    private static final long SPLASH_TIME_SOUND = 1000 ;
    private static final String ON_BOARDING_STRING = "on_boarding_pref" ;
    private static final String BOOLEAN_STRING = "on_boarding_boolean3";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_splash, container, false);


        ImageView imageView = view.findViewById(R.id.imageView2);
//        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.splash_animation);
//        imageView.startAnimation(animation);

        YoYo.with(Techniques.Shake).duration(700).delay(1000).playOn(imageView);
        YoYo.with(Techniques.Shake).duration(700).delay(2500).playOn(imageView);

        Handler handler1 = new Handler();

        handler1.postDelayed(new Runnable() {
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



        MediaPlayer donkey_sound = MediaPlayer.create(requireContext(), R.raw.donkey_sound);


        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                donkey_sound.start();
            }
        }, SPLASH_TIME_SOUND);



        ServiceForInvite2 serviceForInvite2 = new ServiceForInvite2();
        serviceForInvite2.stopSelf();

        return view;


    }

    private boolean onBoardingFinished() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(ON_BOARDING_STRING, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(BOOLEAN_STRING, false);
    }


}