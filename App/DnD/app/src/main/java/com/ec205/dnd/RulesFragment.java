package com.ec205.dnd;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ec205.dnd.models.Magic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RulesFragment extends Fragment {
    private LayoutInflater inflater;
    private DatabaseReference mDatabaseRef;


    EditText ruleEditText;
    EditText loginEditText;
    EditText passwordEditText;
    Button editRuleButton;
    Button loginAdmRuleButton;

    String correctLogin;
    String correctPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_rules, container, false);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        ruleEditText = (EditText) view.findViewById(R.id.rules_edit_text);
        loginEditText = (EditText) view.findViewById(R.id.edit_text_rules_login);
        passwordEditText = (EditText) view.findViewById(R.id.edit_text_rules_password);
        loginAdmRuleButton = (Button) view.findViewById( R.id.button_login_rules);
        editRuleButton = (Button) view.findViewById( R.id.button_edit_rules);

        editRuleButton.setEnabled(false);
        ruleEditText.setFocusable(false);

        updateAdmin();
        loginAdm();
        editRule();
        updateRules();
        return view;
    }

    private void loginAdm(){
        loginAdmRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (login.compareTo(correctLogin)==0 && password.compareTo(correctPassword)==0) {
                    editRuleButton.setEnabled(true);
                    ruleEditText.setFocusable(true);
                    ruleEditText.setFocusableInTouchMode(true);
                    Toast.makeText(getActivity(), "Login realizado com sucesso.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Login e/ou Senha incorretos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editRule(){
        editRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.child("rules").setValue(ruleEditText.getText().toString());
            }
        });
    }


    public void updateRules(){
        mDatabaseRef.child("rules").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            ruleEditText.setText(snapshot.getValue().toString());
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public void updateAdmin(){
        mDatabaseRef.child("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    correctLogin = snapshot.child("login").getValue().toString();
                    correctPassword = snapshot.child("password").getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

}
