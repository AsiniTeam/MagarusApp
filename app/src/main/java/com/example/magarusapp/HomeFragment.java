package com.example.magarusapp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {


    Intent mServiceIntent;
    private ServiceForInvite mYourService;
    public static final String TAG = "HomeFragment";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String USER_STRING = "on_boarding_string";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        Button button = view.findViewById(R.id.buttonInviteFragment);
        String myName = loadNameFromPhone();
        ImageView imageProfile = view.findViewById(R.id.imageProfile);
        updateViews(myName, imageProfile);

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


    public void updateViews(String myName, ImageView imageProfile) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("images");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ImageUser user = snapshot.child(myName).getValue(ImageUser.class);
                int image = user.getImage();

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
        SharedPreferences pref = requireContext().getSharedPreferences(ON_BOARDING_STRING, requireActivity().MODE_PRIVATE);
        String name = pref.getString(USER_STRING, null);
        return name;
    }




}