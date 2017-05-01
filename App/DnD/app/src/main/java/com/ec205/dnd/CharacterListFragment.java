package com.ec205.dnd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CharacterListFragment extends Fragment {

    private DatabaseReference mDatabaseRef;

    private String username;
    private TextView usernameTextView;
    private Button addCharacterButton;
    private LayoutInflater inflater;

    private AlertDialog.Builder addCharacterDialogBuilder;
    private AlertDialog addCharacterDialog;
    private AlertDialog.Builder editCharacterDialogBuilder;
    private AlertDialog editCharacterDialog;

    private ListView characterListView;
    private CharacterCustomAdapter characterCustomAdapter;

    ArrayList<Character> characters = new ArrayList<Character>();

    public CharacterListFragment() {}

    public static CharacterListFragment newInstance(String login) {
        CharacterListFragment fragment = new CharacterListFragment();
        Bundle args = new Bundle();
        args.putString("login", login);
        fragment.setArguments(args);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            username = bundle.getString("login");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character_list, container, false);
        readBundle(getArguments());
        this.inflater = inflater;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        usernameTextView = (TextView) view.findViewById(R.id.text_view_username_character_list);
        addCharacterButton = (Button) view.findViewById(R.id.button_add_character);
        characterListView = (ListView) view.findViewById(R.id.list_view_character);

        getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    characterCustomAdapter = new CharacterCustomAdapter(getContext(), characters);
                    characterListView.setAdapter(characterCustomAdapter);
                    characterCustomAdapter.notifyDataSetChanged();
                }
            });

        usernameTextView.setText("Username: " + username);
        updateListView();
        editCharacter();
        addCharacter();

        return view;
    }

    private void editCharacter(){
        characterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Character character = characterCustomAdapter.getItem(position);
                editCharacterDialogBuilder = new AlertDialog.Builder(getContext());
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();

                View editCharacterDialogView = inflater.inflate(R.layout.dialog_character, null);

                final EditText characterTextView = (EditText) editCharacterDialogView.findViewById(R.id.character_name_edit_text);
                final EditText xpTextView = (EditText) editCharacterDialogView.findViewById(R.id.character_xp_edit_text);
                final EditText classTextView = (EditText) editCharacterDialogView.findViewById(R.id.character_class_edit_text);
                final EditText hitDiceTextView = (EditText) editCharacterDialogView.findViewById(R.id.character_hitdice_edit_text);
                final EditText caracteristicsTextView = (EditText) editCharacterDialogView.findViewById(R.id.character_caracteristics_edit_text);
                final EditText levelTextView = (EditText) editCharacterDialogView.findViewById(R.id.character_level_edit_text);
                final EditText alignmentTextView = (EditText) editCharacterDialogView.findViewById(R.id.character_alignment_edit_text);

                Button addCharacterButton = (Button) editCharacterDialogView.findViewById(R.id.character_confirm_button);
                Button deleteCharacterButton = (Button) editCharacterDialogView.findViewById(R.id.character_delete_button);

                deleteCharacterButton.setEnabled(true);
                characterTextView.setEnabled(false);

                characterTextView.setText(character.getName());
                xpTextView.setText(character.getXp());
                classTextView.setText(character.getCharacterClass());
                hitDiceTextView.setText(character.getHitDice());
                caracteristicsTextView.setText(character.getCaracteristics());
                levelTextView.setText(character.getLevel());
                alignmentTextView.setText(character.getAlignment());

                editCharacterDialogBuilder.setView(editCharacterDialogView);
                editCharacterDialog = editCharacterDialogBuilder.create();
                editCharacterDialog.show();

                deleteCharacterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mDatabaseRef.child("users").child(username).child("characters").child(character.getName()).removeValue();

                        characterTextView.setText("");
                        xpTextView.setText("");
                        classTextView.setText("");
                        hitDiceTextView.setText("");
                        caracteristicsTextView.setText("");
                        levelTextView.setText("");
                        alignmentTextView.setText("");
                        editCharacterDialog.cancel();
                    }
                });

                addCharacterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Character newCharacter = new Character(
                                character.getName(),
                                xpTextView.getText().toString(),
                                classTextView.getText().toString(),
                                hitDiceTextView.getText().toString(),
                                caracteristicsTextView.getText().toString(),
                                levelTextView.getText().toString(),
                                alignmentTextView.getText().toString()
                        );

                        mDatabaseRef.child("users").child(username).child("characters").child(character.getName()).removeValue();
                        mDatabaseRef.child("users").child(username).child("characters").child(character.getName()).setValue(newCharacter);

                        characterTextView.setText("");
                        xpTextView.setText("");
                        classTextView.setText("");
                        hitDiceTextView.setText("");
                        caracteristicsTextView.setText("");
                        levelTextView.setText("");
                        alignmentTextView.setText("");

                        editCharacterDialog.cancel();

                    }
                });
            }
        });
    }

    private void addCharacter(){
        addCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCharacterDialogBuilder = new AlertDialog.Builder(getContext());
                View addCharacterDialogView = inflater.inflate(R.layout.dialog_character, null);
                final EditText characterTextView = (EditText) addCharacterDialogView.findViewById(R.id.character_name_edit_text);
                final EditText xpTextView = (EditText) addCharacterDialogView.findViewById(R.id.character_xp_edit_text);
                final EditText classTextView = (EditText) addCharacterDialogView.findViewById(R.id.character_class_edit_text);
                final EditText hitDiceTextView = (EditText) addCharacterDialogView.findViewById(R.id.character_hitdice_edit_text);
                final EditText caracteristicsTextView = (EditText) addCharacterDialogView.findViewById(R.id.character_caracteristics_edit_text);
                final EditText levelTextView = (EditText) addCharacterDialogView.findViewById(R.id.character_level_edit_text);
                final EditText alignmentTextView = (EditText) addCharacterDialogView.findViewById(R.id.character_alignment_edit_text);

                Button addCharacterButton = (Button) addCharacterDialogView.findViewById(R.id.character_confirm_button);
                Button deleteCharacterButton = (Button) addCharacterDialogView.findViewById(R.id.character_delete_button);
                deleteCharacterButton.setEnabled(false);

                addCharacterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean exists = false;
                        if(!characters.isEmpty()){
                            for(Character i : characters){
                                if(i.getName().compareTo(characterTextView.getText().toString()) == 0) exists = true;
                            }
                        }

                        if(exists){
                            Toast.makeText(getActivity(), "Nome do personagem já existente.", Toast.LENGTH_SHORT).show();
                        }else if(characterTextView.getText().toString().compareTo("") == 0){
                            Toast.makeText(getActivity(), "Preencha o nome Personagem.", Toast.LENGTH_SHORT).show();
                        }else{
                            Character newCharacter = new Character(
                                    characterTextView.getText().toString(),
                                    xpTextView.getText().toString(),
                                    classTextView.getText().toString(),
                                    hitDiceTextView.getText().toString(),
                                    caracteristicsTextView.getText().toString(),
                                    levelTextView.getText().toString(),
                                    alignmentTextView.getText().toString()
                            );
                            mDatabaseRef.child("users").child(username).child("characters").child(characterTextView.getText().toString()).setValue(newCharacter);

                            characterTextView.setText("");
                            xpTextView.setText("");
                            classTextView.setText("");
                            hitDiceTextView.setText("");
                            caracteristicsTextView.setText("");
                            levelTextView.setText("");
                            alignmentTextView.setText("");

                            addCharacterDialog.cancel();
                        }
                    }
                });

                addCharacterDialogBuilder.setView(addCharacterDialogView);
                addCharacterDialog = addCharacterDialogBuilder.create();

                addCharacterDialog.show();
            }
        });
    }

    public void updateListView(){
        mDatabaseRef.child("users").child(username).child("characters").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                characters.clear();
                if(snapshot.exists()){
                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                        System.out.println(userSnapshot);
                        Character character = userSnapshot.getValue(Character.class);
                        characters.add(character);
                    }
                }

                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            characterCustomAdapter = new CharacterCustomAdapter(getContext(), characters);
                            characterCustomAdapter.notifyDataSetChanged();
                            characterListView.setAdapter(characterCustomAdapter);
                        }
                    });
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}
