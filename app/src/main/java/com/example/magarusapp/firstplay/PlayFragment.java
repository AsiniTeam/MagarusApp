package com.example.magarusapp.firstplay;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.magarusapp.AskUserFragmentArgs;
import com.example.magarusapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PlayFragment extends Fragment {


    private static final String TAG = "PlayFragment";
    private static final String ON_BOARDING_STRING = "on_boarding_pref";
    private static final String USER_STRING = "on_boarding_string2";

    private Handler handler = new Handler();
    private static final long TIME_MOVING = 100;
    private static final long TIME_FINISH = 10000;
    private ImageView fagianRunner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        String refString1 = PlayFragmentArgs.fromBundle(getArguments()).getRefString1();
        String refString2 = PlayFragmentArgs.fromBundle(getArguments()).getRefString2();
        String myName = loadNameFromPhone();
        fagianRunner = view.findViewById(R.id.fagianRunner);
        final boolean run = false;
        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference().child("connection").child(refString1).child(refString2);
        checkVictory(refString1, refString2, view);

        handler.postDelayed(runnable, TIME_MOVING);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkVictoryHandler(refString1, view);
            }
        }, TIME_FINISH);

        //countDownStart(refString1, refString2, view);



        fagianRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                victory(refString1, refString2, myName, view);
            }
        });


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



    public  void victory(String refString1, String refString2, String myName, View view) {
        DatabaseReference refVictory = FirebaseDatabase.getInstance().getReference().child("connection").child(refString1);
        refVictory.child(refString2).child("victory").setValue(true);
        setPointsFirebase(myName);
        String winnerUser = myName;
        PlayFragmentDirections.ActionPlayFragmentToFinishFirstPlayFragment action = PlayFragmentDirections.actionPlayFragmentToFinishFirstPlayFragment(winnerUser);
        Navigation.findNavController(view).navigate(action);
    }

    public void checkVictory(String refString1, String refString2, View view) {
        DatabaseReference refVictory = FirebaseDatabase.getInstance().getReference().child("connection").child(refString1);
        if(refString2.equals("first")) {
            refVictory = refVictory.child("second");
        } else {
            refVictory = refVictory.child("first");
        }
        refVictory.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean victory = snapshot.child("victory").getValue(boolean.class);
                    String winnerName = snapshot.child("name").getValue(String.class);
                    if (victory == true) {
                        setPointsFirebase(winnerName);
                        PlayFragmentDirections.ActionPlayFragmentToFinishFirstPlayFragment action = PlayFragmentDirections.actionPlayFragmentToFinishFirstPlayFragment(winnerName);
                        Navigation.findNavController(view).navigate(action);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(requireContext(), "Ci dispiace, non c'è connessione", Toast.LENGTH_LONG).show();
                }
            });
    }

    public void setPointsFirebase(String winnerUser) {
       DatabaseReference pointRef = FirebaseDatabase.getInstance().getReference().child("points").child(winnerUser);
       pointRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               int points = snapshot.child("points").getValue(int.class);
               points = points + 1;
               pointRef.child("points").setValue(points);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(requireContext(), "Ci dispiace, non c'è connessione", Toast.LENGTH_LONG).show();
           }
       });
    }


    public String loadNameFromPhone() {
        SharedPreferences pref = requireContext().getSharedPreferences(ON_BOARDING_STRING, requireActivity().MODE_PRIVATE);
        String name = pref.getString(USER_STRING, null);
        return name;
    }



    public void checkVictoryHandler(String refString1, View view) {
        final boolean[] victoryFirst = new boolean[1];
        final boolean[] victorySecond = new boolean[1];
        DatabaseReference vicRef = FirebaseDatabase.getInstance().getReference().child("connection").child(refString1);
        vicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               boolean victory = snapshot.child("first").child("victory").getValue(boolean.class);
               victoryFirst[0] = victory;
                victory = snapshot.child("second").child("victory").getValue(boolean.class);
                victorySecond[0] = victory;
                Log.i(TAG, "victories are: " + victoryFirst[0] + ", " + victorySecond[0]);
                if(!victoryFirst[0] & !victorySecond[0]) {
                    String noWinnerString = "noOne";
                    PlayFragmentDirections.ActionPlayFragmentToFinishFirstPlayFragment action = PlayFragmentDirections.actionPlayFragmentToFinishFirstPlayFragment(noWinnerString);
                    Navigation.findNavController(view).navigate(action);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(requireContext(), "Non c'è connessione zio", Toast.LENGTH_LONG).show();
            }
        });
    }




}