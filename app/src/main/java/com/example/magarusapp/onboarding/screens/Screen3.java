package com.example.magarusapp.onboarding.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.magarusapp.R;


public class Screen3 extends Fragment {

    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String BOOLEAN_STRING = "on_boarding_boolean";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_screen3, container, false);

        ViewPager2 viewPager2 = requireActivity().findViewById(R.id.onBoardingViewPager);

        TextView next = view.findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_viewPagerFragment_to_homeFragment);
                onBoardingFinished();
            }
        });



        return view;
    }

    private void onBoardingFinished() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(ON_BOARDING_STRING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(BOOLEAN_STRING, true);
        editor.apply();
    }


}