package com.example.magarusapp.firstplay;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.magarusapp.MainActivity;
import com.example.magarusapp.R;


public class FinishFirstPlayFragment extends Fragment {

    private static final long TIME_FINISH = 10000;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finish_first_play, container, false);
        TextView winnerTextView = view.findViewById(R.id.winnerText);
        String winnerName = FinishFirstPlayFragmentArgs.fromBundle(getArguments()).getWinnerUser();
        updateText(winnerTextView, winnerName);


        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }

        }, TIME_FINISH);


        return  view;

    }



    public void updateText(TextView winnerTextView, String winnerName) {
        if (winnerName.equals("noOne")) {
            winnerTextView.setText("Sfigati, non ha vinto nessuno");
        } else {
            winnerTextView.setText("Ha vinto " + winnerName);
        }
    }



}