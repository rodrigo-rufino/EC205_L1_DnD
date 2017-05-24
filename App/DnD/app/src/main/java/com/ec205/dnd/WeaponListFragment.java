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

import com.ec205.dnd.models.Magic;
import com.ec205.dnd.models.Weapon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WeaponListFragment extends Fragment {
    private LayoutInflater inflater;
    private DatabaseReference mDatabaseRef;

    WeaponCustomAdapter weaponCustomAdapter;
    ArrayList<Weapon> weapons = new ArrayList<Weapon>();
    ListView weaponListView;
    Button newWeaponButton;

    private AlertDialog.Builder addWeaponDialogBuilder;
    private AlertDialog addWeaponDialog;

    private AlertDialog.Builder editWeaponDialogBuilder;
    private AlertDialog editWeaponDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_weapon_list, container, false);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        newWeaponButton = (Button) view.findViewById(R.id.button_add_weapon);
        weaponListView = (ListView) view.findViewById(R.id.list_view_weapon);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                weaponCustomAdapter = new WeaponCustomAdapter(getContext(), weapons);
                weaponListView.setAdapter(weaponCustomAdapter);
                weaponCustomAdapter.notifyDataSetChanged();
            }
        });

        updateListView();
        addWeapon();
        editWeapon();

        return view;
    }

    private void addWeapon(){
        newWeaponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeaponDialogBuilder = new AlertDialog.Builder(getContext());
                final View addWeaponDialogView = inflater.inflate(R.layout.dialog_weapon, null);


                final EditText weaponEditText = (EditText) addWeaponDialogView.findViewById(R.id.weapon_name_edit_text);
                final EditText weaponPriceEditText = (EditText) addWeaponDialogView.findViewById(R.id.weapon_price_edit_text);
                final EditText weaponDamageEditText = (EditText) addWeaponDialogView.findViewById(R.id.weapon_damage_edit_text);
                final EditText weaponTypeEditText = (EditText) addWeaponDialogView.findViewById(R.id.weapon_type_edit_text);
                final EditText weaponMagicEditText = (EditText) addWeaponDialogView.findViewById(R.id.weapon_magic_edit_text);
                final EditText weaponBonusEditText = (EditText) addWeaponDialogView.findViewById(R.id.weapon_bonus_edit_text);


                Button addWeaponButton = (Button) addWeaponDialogView.findViewById(R.id.weapon_confirm_button);
                Button deleteWeaponButton = (Button) addWeaponDialogView.findViewById(R.id.weapon_delete_button);
                deleteWeaponButton.setEnabled(false);

                addWeaponButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean exists = false;
                        if(!weapons.isEmpty()){
                            for(Weapon i : weapons){
                                if(i.getName().compareTo(weaponEditText.getText().toString()) == 0) exists = true;
                            }
                        }

                        if(exists){
                            Toast.makeText(getActivity(), "Nome do equipamento já existente.", Toast.LENGTH_SHORT).show();
                        }else if(weaponEditText.getText().toString().compareTo("") == 0){
                            Toast.makeText(getActivity(), "Preencha o nome Equipamento.", Toast.LENGTH_SHORT).show();
                        }else{
                            Weapon newWeapon = new Weapon(
                                    weaponEditText.getText().toString(),
                                    weaponPriceEditText.getText().toString(),
                                    weaponDamageEditText.getText().toString(),
                                    weaponTypeEditText.getText().toString(),
                                    weaponMagicEditText.getText().toString(),
                                    weaponBonusEditText.getText().toString());
                            mDatabaseRef.child("weapons").child(weaponEditText.getText().toString()).setValue(newWeapon);

                            weaponEditText.setText("");
                            weaponPriceEditText.setText("");
                            weaponDamageEditText.setText("");
                            weaponTypeEditText.setText("");
                            weaponMagicEditText.setText("");
                            weaponBonusEditText.setText("");

                            addWeaponDialog.cancel();
                        }
                    }
                });

                addWeaponDialogBuilder.setView(addWeaponDialogView);
                addWeaponDialog = addWeaponDialogBuilder.create();

                addWeaponDialog.show();
            }
        });
    }

    private void editWeapon(){
        weaponListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Weapon weapon = weaponCustomAdapter.getItem(position);

                editWeaponDialogBuilder = new AlertDialog.Builder(getContext());
                final View editWeaponDialogView = inflater.inflate(R.layout.dialog_weapon, null);

                mDatabaseRef = FirebaseDatabase.getInstance().getReference();

                final EditText weaponEditText = (EditText) editWeaponDialogView.findViewById(R.id.weapon_name_edit_text);
                final EditText weaponPriceEditText = (EditText) editWeaponDialogView.findViewById(R.id.weapon_price_edit_text);
                final EditText weaponDamageEditText = (EditText) editWeaponDialogView.findViewById(R.id.weapon_damage_edit_text);
                final EditText weaponTypeEditText = (EditText) editWeaponDialogView.findViewById(R.id.weapon_type_edit_text);
                final EditText weaponMagicEditText = (EditText) editWeaponDialogView.findViewById(R.id.weapon_magic_edit_text);
                final EditText weaponBonusEditText = (EditText) editWeaponDialogView.findViewById(R.id.weapon_bonus_edit_text);

                Button addWeaponButton = (Button) editWeaponDialogView.findViewById(R.id.weapon_confirm_button);
                Button deleteWeaponButton = (Button) editWeaponDialogView.findViewById(R.id.weapon_delete_button);
                deleteWeaponButton.setEnabled(true);
                weaponEditText.setEnabled(false);

                weaponEditText.setText(weapon.getName());
                weaponPriceEditText.setText(weapon.getPrice());
                weaponDamageEditText.setText(weapon.getDamage());
                weaponTypeEditText.setText(weapon.getType());
                weaponMagicEditText.setText(weapon.getMagic());
                weaponBonusEditText.setText(weapon.getBonus());

                editWeaponDialogBuilder.setView(editWeaponDialogView);
                editWeaponDialog = editWeaponDialogBuilder.create();
                editWeaponDialog.show();

                deleteWeaponButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabaseRef.child("weapons").child(weapon.getName()).removeValue();

                        weaponEditText.setText("");
                        weaponPriceEditText.setText("");
                        weaponDamageEditText.setText("");
                        weaponTypeEditText.setText("");
                        weaponMagicEditText.setText("");
                        weaponBonusEditText.setText("");

                        editWeaponDialog.cancel();
                    }
                });

                addWeaponButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Weapon newWeapon = new Weapon(
                                weapon.getName(),
                                weaponPriceEditText.getText().toString(),
                                weaponDamageEditText.getText().toString(),
                                weaponTypeEditText.getText().toString(),
                                weaponMagicEditText.getText().toString(),
                                weaponBonusEditText.getText().toString());

                        mDatabaseRef.child("weapons").child(weapon.getName()).removeValue();
                        mDatabaseRef.child("weapons").child(weapon.getName()).setValue(newWeapon);

                        weaponEditText.setText("");
                        weaponPriceEditText.setText("");
                        weaponDamageEditText.setText("");
                        weaponTypeEditText.setText("");
                        weaponMagicEditText.setText("");
                        weaponBonusEditText.setText("");

                        editWeaponDialog.cancel();
                    }
                });
            }
        });
    }

    public void updateListView(){
        mDatabaseRef.child("weapons").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                weapons.clear();
                if(snapshot.exists()){
                    for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                        System.out.println(userSnapshot);
                        Weapon weapon = userSnapshot.getValue(Weapon.class);
                        weapons.add(weapon);
                    }
                }

                if(getActivity()!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weaponCustomAdapter = new WeaponCustomAdapter(getContext(), weapons);
                            weaponCustomAdapter.notifyDataSetChanged();
                            weaponListView.setAdapter(weaponCustomAdapter);
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
