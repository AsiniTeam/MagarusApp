package com.example.magarusapp.onboarding;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.magarusapp.R;
import com.example.magarusapp.onboarding.screens.Screen1;
import com.example.magarusapp.onboarding.screens.Screen2;
import com.example.magarusapp.onboarding.screens.Screen3;

import java.util.ArrayList;


public class ViewPagerFragment extends Fragment {

    private static final int NumberOfFragments = 3;

    Screen1 screen1 = new Screen1();
    Screen2 screen2 = new Screen2();
    Screen3 screen3 = new Screen3();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        ArrayList<Fragment> fragmentList = new ArrayList<>(NumberOfFragments);

        fragmentList.add(screen1);
        fragmentList.add(screen2);
        fragmentList.add(screen3);

        FragmentAdapter fragmentAdapter = new FragmentAdapter(requireActivity(), fragmentList);

        ViewPager2 viewPager2 = view.findViewById(R.id.onBoardingViewPager);
        viewPager2.setAdapter(fragmentAdapter);

        return view;

    }
}