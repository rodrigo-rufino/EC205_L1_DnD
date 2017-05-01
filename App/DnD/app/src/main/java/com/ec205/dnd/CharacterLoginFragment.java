package com.ec205.dnd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by rodri on 15-Apr-17.
 */

public class CharacterLoginFragment extends Fragment {
    private DatabaseReference mDatabaseRef;

    String login = "";
    String password = "";
    EditText loginEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    LayoutInflater inflater;

    Boolean buttonClick = false;
    DataSnapshot dataSnapshot;


    public CharacterLoginFragment() {
    }

    public static CharacterLoginFragment newInstance() {
        CharacterLoginFragment fragment = new CharacterLoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_character_login, container, false);

        loginEditText = (EditText) rootView.findViewById(R.id.edit_text_character_login);
        passwordEditText = (EditText) rootView.findViewById(R.id.edit_text_character_password);

        loginButton = (Button) rootView.findViewById(R.id.character_login_button);
        registerButton = (Button) rootView.findViewById(R.id.button_character_register);

        loginButton.setEnabled(false);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        login = loginEditText.getText().toString();
        password = passwordEditText.getText().toString();

        updateUser();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick = true;
                login = loginEditText.getText().toString();
                password = passwordEditText.getText().toString();
                updateUser();
                System.out.println(dataSnapshot);

                String correctLogin = dataSnapshot.child(login).child("login").getValue().toString();
                String correctPassword = dataSnapshot.child(login).child("password").getValue().toString();

                if(dataSnapshot.child(login).exists()){
                    if (login.compareTo(correctLogin)==0 && password.compareTo(correctPassword)==0){
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_main, new CharacterListFragment().newInstance(login));
                        ft.commit();
                        Toast.makeText(getActivity(), "Successful Login.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Login and/or password are incorrect.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        return rootView;
    }

    public void registerUser(){
        String newLogin = loginEditText.getText().toString();
        String newPassword = passwordEditText.getText().toString();

        mDatabaseRef.child("users").child(newLogin).child("login").setValue(newLogin);
        mDatabaseRef.child("users").child(newLogin).child("password").setValue(newPassword);
    }

    public void updateUser(){
        mDatabaseRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    dataSnapshot = snapshot;
                    System.out.println(dataSnapshot);
                    loginButton.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


}