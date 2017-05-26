package com.ec205.dnd.model;

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
import android.widget.Toast;

import com.ec205.dnd.control.MagicCustomAdapter;
import com.ec205.dnd.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MagicListFragment extends Fragment {
    private LayoutInflater inflater;
    private DatabaseReference mDatabaseRef;

    MagicCustomAdapter magicCustomAdapter;
    ArrayList<Magic> magics = new ArrayList<Magic>();
    ListView magicListView;
    Button newMagicButton;

    private AlertDialog.Builder addMagicDialogBuilder;
    private AlertDialog addMagicDialog;

    private AlertDialog.Builder editMagicDialogBuilder;
    private AlertDialog editMagicDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_magic_list, container, false);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        newMagicButton = (Button) view.findViewById(R.id.button_add_magic);
        magicListView = (ListView) view.findViewById(R.id.list_view_magic);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                magicCustomAdapter = new MagicCustomAdapter(getContext(), magics);
                magicListView.setAdapter(magicCustomAdapter);
                magicCustomAdapter.notifyDataSetChanged();
            }
        });

        updateListView();
        addMagic();
        editMagic();

        return view;
    }

    private void addMagic(){
        newMagicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMagicDialogBuilder = new AlertDialog.Builder(getContext());
                final View addMagicDialogView = inflater.inflate(R.layout.dialog_magic, null);

                final EditText magicEditText = (EditText) addMagicDialogView.findViewById(R.id.magic_name_edit_text);
                final EditText damageEditText = (EditText) addMagicDialogView.findViewById(R.id.magic_damage_edit_text);
                final EditText distanceEditText = (EditText) addMagicDialogView.findViewById(R.id.magic_distance_edit_text);
                final EditText componentsEditText = (EditText) addMagicDialogView.findViewById(R.id.magic_components_edit_text);
                final EditText levelEditText = (EditText) addMagicDialogView.findViewById(R.id.magic_level_edit_text);

                Button addMagicButton = (Button) addMagicDialogView.findViewById(R.id.magic_confirm_button);
                Button deleteMagicButton = (Button) addMagicDialogView.findViewById(R.id.magic_delete_button);
                deleteMagicButton.setEnabled(false);

                addMagicButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean exists = false;
                        if(!magics.isEmpty()){
                            for(Magic i : magics){
                                if(i.getName().compareTo(magicEditText.getText().toString()) == 0) exists = true;
                            }
                        }

                        if(exists){
                            Toast.makeText(getActivity(), "Nome da magia já existente.", Toast.LENGTH_SHORT).show();
                        }else if(magicEditText.getText().toString().compareTo("") == 0){
                            Toast.makeText(getActivity(), "Preencha o nome Personagem.", Toast.LENGTH_SHORT).show();
                        }else{
                            Magic newMagic = new Magic(
                                    magicEditText.getText().toString(),
                                    damageEditText.getText().toString(),
                                    distanceEditText.getText().toString(),
                                    componentsEditText.getText().toString(),
                                    levelEditText.getText().toString());
                            mDatabaseRef.child("magics").child(magicEditText.getText().toString()).setValue(newMagic);

                            magicEditText.setText("");
                            damageEditText.setText("");
                            distanceEditText.setText("");
                            componentsEditText.setText("");
                            levelEditText.setText("");

                            addMagicDialog.cancel();
                        }
                    }
                });

                addMagicDialogBuilder.setView(addMagicDialogView);
                addMagicDialog = addMagicDialogBuilder.create();

                addMagicDialog.show();
            }
        });
    }

    private void editMagic(){
        magicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Magic magic = magicCustomAdapter.getItem(position);

                editMagicDialogBuilder = new AlertDialog.Builder(getContext());
                final View editMagicDialogView = inflater.inflate(R.layout.dialog_magic, null);

                mDatabaseRef = FirebaseDatabase.getInstance().getReference();

                final EditText magicEditText = (EditText) editMagicDialogView.findViewById(R.id.magic_name_edit_text);
                final EditText damageEditText = (EditText) editMagicDialogView.findViewById(R.id.magic_damage_edit_text);
                final EditText distanceEditText = (EditText) editMagicDialogView.findViewById(R.id.magic_distance_edit_text);
                final EditText componentsEditText = (EditText) editMagicDialogView.findViewById(R.id.magic_components_edit_text);
                final EditText levelEditText = (EditText) editMagicDialogView.findViewById(R.id.magic_level_edit_text);

                Button addMagicButton = (Button) editMagicDialogView.findViewById(R.id.magic_confirm_button);
                Button deleteMagicButton = (Button) editMagicDialogView.findViewById(R.id.magic_delete_button);
                deleteMagicButton.setEnabled(true);
                magicEditText.setEnabled(false);

                magicEditText.setText(magic.getName());
                damageEditText.setText(magic.getDamage());
                distanceEditText.setText(magic.getDistance());
                componentsEditText.setText(magic.getComponents());
                levelEditText.setText(magic.getLevel());

                editMagicDialogBuilder.setView(editMagicDialogView);
                editMagicDialog = editMagicDialogBuilder.create();
                editMagicDialog.show();

                deleteMagicButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabaseRef.child("magics").child(magic.getName()).removeValue();

                        magicEditText.setText("");
                        damageEditText.setText("");
                        distanceEditText.setText("");
                        componentsEditText.setText("");
                        levelEditText.setText("");

                        editMagicDialog.cancel();
                    }
                });

                addMagicButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Magic newMagic = new Magic(
                                magic.getName(),
                                damageEditText.getText().toString(),
                                distanceEditText.getText().toString(),
                                componentsEditText.getText().toString(),
                                levelEditText.getText().toString());

                        mDatabaseRef.child("magics").child(magic.getName()).removeValue();
                        mDatabaseRef.child("magics").child(magic.getName()).setValue(newMagic);

                        magicEditText.setText("");
                        damageEditText.setText("");
                        distanceEditText.setText("");
                        componentsEditText.setText("");
                        levelEditText.setText("");

                        editMagicDialog.cancel();
                    }
                });
            }
        });
    }

    public void updateListView(){
        mDatabaseRef.child("magics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                magics.clear();
                if(snapshot.exists()){
                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                        System.out.println(userSnapshot);
                        Magic magic = userSnapshot.getValue(Magic.class);
                        magics.add(magic);
                    }
                }

                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            magicCustomAdapter = new MagicCustomAdapter(getContext(), magics);
                            magicCustomAdapter.notifyDataSetChanged();
                            magicListView.setAdapter(magicCustomAdapter);
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
