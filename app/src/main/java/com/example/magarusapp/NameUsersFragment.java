package com.example.magarusapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NameUsersFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_name_users, container, false);

        EditText editText = view.findViewById(R.id.editTextName);

        Button button = view.findViewById(R.id.buttonUser);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(editText.getText().toString().isEmpty()) {
                   Toast.makeText(requireContext(), "Che cazzo schiacci, che poi io schiaccio tua madre", Toast.LENGTH_LONG).show();
               }
               else {
                   add_user(editText);
                   Navigation.findNavController(view).navigate(R.id.action_nameUsersFragment_to_inviteFragment2);
               }
            }
        });

        return view;
    }



    public void add_user(EditText editText) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(editText.getText().toString()).child("name").setValue(editText.getText().toString());

    }


    public void invite_player(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.setValue("pisello");


    }


}