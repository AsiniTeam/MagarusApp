package com.example.magarusapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class HomeFragment extends Fragment {


    Intent mServiceIntent;
    private ServiceForInvite mYourService;
    public static final String TAG = "HomeFragment";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        Button button = view.findViewById(R.id.buttonInviteFragment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_inviteFragment2);
            }
        });


        mYourService = new ServiceForInvite();
        mServiceIntent = new Intent(requireContext(), mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            requireActivity().startService(mServiceIntent);
        }


        return view;

    }




    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i (TAG, "Service Running");
                return true;
            }
        }
        Log.i (TAG, "Service not running");
        return false;
    }




    @Override
    public void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(requireContext(), Restarter.class);
        requireContext().sendBroadcast(broadcastIntent);
        super.onDestroy();
    }


}