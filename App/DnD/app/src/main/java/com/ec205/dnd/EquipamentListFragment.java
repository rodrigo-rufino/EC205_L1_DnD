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
import android.widget.Toast;

import com.ec205.dnd.models.Equipament;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EquipamentListFragment extends Fragment {
    private LayoutInflater inflater;
    private DatabaseReference mDatabaseRef;

    EquipamentCustomAdapter equipamentCustomAdapter;
    ArrayList<Equipament> equipaments = new ArrayList<Equipament>();
    ListView equipamentListView;
    Button newEquipamentButton;

    private AlertDialog.Builder addEquipamentDialogBuilder;
    private AlertDialog addEquipamentDialog;

    private AlertDialog.Builder editEquipamentDialogBuilder;
    private AlertDialog editEquipamentDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_equipament_list, container, false);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        newEquipamentButton = (Button) view.findViewById(R.id.button_add_equipament);
        equipamentListView = (ListView) view.findViewById(R.id.list_view_equipament);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                equipamentCustomAdapter = new EquipamentCustomAdapter(getContext(), equipaments);
                equipamentListView.setAdapter(equipamentCustomAdapter);
                equipamentCustomAdapter.notifyDataSetChanged();
            }
        });

        updateListView();
        addEquipament();
        editEquipament();

        return view;
    }

    private void addEquipament(){
        newEquipamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEquipamentDialogBuilder = new AlertDialog.Builder(getContext());
                final View addEquipamentDialogView = inflater.inflate(R.layout.dialog_equipament, null);


                final EditText equipamentEditText = (EditText) addEquipamentDialogView.findViewById(R.id.equipament_name_edit_text);
                final EditText classeEditText = (EditText) addEquipamentDialogView.findViewById(R.id.equipament_classe_edit_text);
                final EditText levelEditText = (EditText) addEquipamentDialogView.findViewById(R.id.equipament_level_edit_text);
                final EditText hpEditText = (EditText) addEquipamentDialogView.findViewById(R.id.equipament_hp_edit_text);
                final EditText caracteristicsEditText = (EditText) addEquipamentDialogView.findViewById(R.id.equipament_caracterists_edit_text);
                final EditText xpEditText = (EditText) addEquipamentDialogView.findViewById(R.id.equipament_xp_edit_text);
                final EditText alignmentEditText = (EditText) addEquipamentDialogView.findViewById(R.id.equipament_alignment_edit_text);

                Button addEquipamentButton = (Button) addEquipamentDialogView.findViewById(R.id.equipament_confirm_button);
                Button deleteEquipamentButton = (Button) addEquipamentDialogView.findViewById(R.id.equipament_delete_button);
                deleteEquipamentButton.setEnabled(false);

                addEquipamentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean exists = false;
                        if(!equipaments.isEmpty()){
                            for(Equipament i : equipaments){
                                if(i.getName().compareTo(equipamentEditText.getText().toString()) == 0) exists = true;
                            }
                        }

                        if(exists){
                            Toast.makeText(getActivity(), "Nome do Equipamento já existente.", Toast.LENGTH_SHORT).show();
                        }else if(equipamentEditText.getText().toString().compareTo("") == 0){
                            Toast.makeText(getActivity(), "Preencha o nome Personagem.", Toast.LENGTH_SHORT).show();
                        }else{
                           Equipament newEquipament = new Equipament(

                                    equipamentEditText.getText().toString(),
                                    classeEditText.getText().toString(),
                                    levelEditText.getText().toString(),
                                    hpEditText.getText().toString(),
                                    caracteristicsEditText.getText().toString(),
                                    xpEditText.getText().toString(),
                                    alignmentEditText.getText().toString());
                            mDatabaseRef.child("equipaments").child(equipamentEditText.getText().toString()).setValue(newEquipament);

                            equipamentEditText.setText("");
                            classeEditText.setText("");
                            levelEditText.setText("");
                            hpEditText.setText("");
                            caracteristicsEditText.setText("");
                            xpEditText.setText("");
                            alignmentEditText.setText("");

                            addEquipamentDialog.cancel();
                        }
                    }
                });

                addEquipamentDialogBuilder.setView(addEquipamentDialogView);
                addEquipamentDialog = addEquipamentDialogBuilder.create();

                addEquipamentDialog.show();
            }
        });
    }

    private void editEquipament(){
        equipamentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Equipament equipament = equipamentCustomAdapter.getItem(position);

                editEquipamentDialogBuilder = new AlertDialog.Builder(getContext());
                final View editEquipamentDialogView = inflater.inflate(R.layout.dialog_equipament, null);

                mDatabaseRef = FirebaseDatabase.getInstance().getReference();

                final EditText equipamentEditText = (EditText) editEquipamentDialogView.findViewById(R.id.equipament_name_edit_text);
                final EditText classeEditText = (EditText) editEquipamentDialogView.findViewById(R.id.equipament_classe_edit_text);
                final EditText levelEditText = (EditText) editEquipamentDialogView.findViewById(R.id.equipament_level_edit_text);
                final EditText hpEditText = (EditText) editEquipamentDialogView.findViewById(R.id.equipament_hp_edit_text);
                final EditText caracteristicsEditText = (EditText) editEquipamentDialogView.findViewById(R.id.equipament_caracterists_edit_text);
                final EditText xpEditText = (EditText) editEquipamentDialogView.findViewById(R.id.equipament_xp_edit_text);
                final EditText alignmentEditText = (EditText) editEquipamentDialogView.findViewById(R.id.equipament_alignment_edit_text);

                Button addEquipamentButton = (Button) editEquipamentDialogView.findViewById(R.id.equipament_confirm_button);
                Button deleteEquipamentButton = (Button) editEquipamentDialogView.findViewById(R.id.equipament_delete_button);
                deleteEquipamentButton.setEnabled(true);
                equipamentEditText.setEnabled(false);

                equipamentEditText.setText(equipament.getName());
                classeEditText.setText(equipament.getClasse());
                levelEditText.setText(equipament.getLevel());
                hpEditText.setText(equipament.getHp());
                caracteristicsEditText.setText(equipament.getCaracteristics());
                xpEditText.setText(equipament.getXp());
                alignmentEditText.setText(equipament.getAlignment());


                editEquipamentDialogBuilder.setView(editEquipamentDialogView);
                editEquipamentDialog = editEquipamentDialogBuilder.create();
                editEquipamentDialog.show();

                deleteEquipamentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabaseRef.child("equipaments").child(equipament.getName()).removeValue();

                        equipamentEditText.setText("");
                        classeEditText.setText("");
                        levelEditText.setText("");
                        hpEditText.setText("");
                        caracteristicsEditText.setText("");
                        xpEditText.setText("");
                        alignmentEditText.setText("");
                        editEquipamentDialog.cancel();
                    }
                });

                addEquipamentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Equipament newEquipament = new Equipament(
                                equipament.getName(),
                                classeEditText.getText().toString(),
                                levelEditText.getText().toString(),
                                hpEditText.getText().toString(),
                                caracteristicsEditText.getText().toString(),
                                xpEditText.getText().toString(),
                                alignmentEditText.getText().toString());


                        mDatabaseRef.child("equipaments").child(equipament.getName()).removeValue();
                        mDatabaseRef.child("equipaments").child(equipament.getName()).setValue(newEquipament);

                        equipamentEditText.setText("");
                        classeEditText.setText("");
                        levelEditText.setText("");
                        hpEditText.setText("");
                        caracteristicsEditText.setText("");
                        xpEditText.setText("");
                        alignmentEditText.setText("");

                        editEquipamentDialog.cancel();
                    }
                });
            }
        });
    }

    public void updateListView(){
        mDatabaseRef.child("equipaments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                equipaments.clear();
                if(snapshot.exists()){
                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                        //System.out.println(userSnapshot);
                        Equipament equipament = userSnapshot.getValue(Equipament.class);
                        equipaments.add(equipament);
                    }
                }

                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            equipamentCustomAdapter = new EquipamentCustomAdapter(getContext(), equipaments);
                            equipamentCustomAdapter.notifyDataSetChanged();
                            equipamentListView.setAdapter(equipamentCustomAdapter);
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
