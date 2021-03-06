package com.example.magarusapp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String USER_STRING = "on_boarding_string2";




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        Button button = view.findViewById(R.id.buttonInviteFragment);
        TextView pointText = view.findViewById(R.id.pointText);
        String myName = loadNameFromPhone();
        ImageView imageProfile = view.findViewById(R.id.imageProfile);
        updatePoints(myName, pointText);
        updateViews(myName, imageProfile);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_inviteFragment2);
            }
        });



       scheduledJob();





        return view;

    }




    public void updateViews(String myName, ImageView imageProfile) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("images");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int image = snapshot.child(myName).child("image").getValue(int.class);
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
                Toast.makeText(requireContext(), "Ci dispiace, non c'?? connesione a internet", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void updatePoints(String myName, TextView pointText) {

        DatabaseReference refPoint = FirebaseDatabase.getInstance().getReference().child("points");
        Log.i(TAG, "ref is : " + refPoint);
        refPoint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int points = snapshot.child(myName).child("points").getValue(int.class);
                pointText.setText("Punti: " + points);
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Ci dispiace, non c'?? connesione a internet", Toast.LENGTH_LONG).show();
            }
        });


    }




    public String loadNameFromPhone() {
        SharedPreferences pref = requireContext().getSharedPreferences(ON_BOARDING_STRING, requireActivity().MODE_PRIVATE);
        String name = pref.getString(USER_STRING, null);
        return name;
    }





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scheduledJob() {
        ComponentName componentName = new ComponentName(requireContext(), JobService1.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPeriodic(15*60*1000)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) requireActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if( resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.i(TAG, "Job scheduled");
        } else {
            Log.i(TAG, "Job cancelled");
        }

    }


}