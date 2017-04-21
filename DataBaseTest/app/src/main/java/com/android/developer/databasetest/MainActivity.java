package com.android.developer.databasetest;

import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnAddData;
    ListView list;
    ArrayAdapter<String> listAdapter;
    Array idArray;

    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        btnAddData = (FloatingActionButton) findViewById(R.id.add_button);
        btnAddData.setImageResource(R.drawable.ic_add_white_48dp);

        list = (ListView) findViewById(R.id.list);
        listAdapter = new ArrayAdapter<String>(this, R.layout.list_item);
        list.setAdapter(listAdapter);

        ViewAll();
        AddData();
        listItemClick();
    }

    public void listItemClick(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String[] parts = String.valueOf(listAdapter.getItem(i)).split("-");
                String id = parts[0];
                ViewItem(id);
            }
        });
    }

    public void AddData(){
        btnAddData.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddItem();
                }
            }
        );
    }

    public void AddItem(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.student_dialog, null);

        final EditText nameEditText = (EditText) view.findViewById(R.id.dialog_name_edit_text);
        final EditText surnameEditText = (EditText) view.findViewById(R.id.dialog_surname_edit_text);
        final EditText markEditText = (EditText) view.findViewById(R.id.dialog_marks_edit_text);
        final Button okButton = (Button) view.findViewById(R.id.dialog_ok_button);
        final Button deleteButton = (Button) view.findViewById(R.id.dialog_cancel_button);
        deleteButton.setEnabled(false);
        deleteButton.setVisibility(View.INVISIBLE);
        final Student student;
        mBuilder.setView(view);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(
                        nameEditText.getText().toString(),
                        surnameEditText.getText().toString(),
                        markEditText.getText().toString());
                if(isInserted == true) {
                    Toast.makeText(MainActivity.this, "Data Inserted.", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    ViewAll();
                }
                else
                    Toast.makeText(MainActivity.this,"Data not Inserted.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void ViewAll(){
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            listAdapter.clear();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        listAdapter.clear();
        while (res.moveToNext()){
            buffer.append(res.getString(0)+"-");
            buffer.append("Name: "+res.getString(1)+"\n");
            listAdapter.add(buffer.toString());
            buffer.setLength(0);
        }
    }

    public void ViewItem(final String id){
        Cursor res = myDb.getAllData();

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.student_dialog, null);

        final EditText nameEditText = (EditText) view.findViewById(R.id.dialog_name_edit_text);
        final EditText surnameEditText = (EditText) view.findViewById(R.id.dialog_surname_edit_text);
        final EditText markEditText = (EditText) view.findViewById(R.id.dialog_marks_edit_text);
        final Button okButton = (Button) view.findViewById(R.id.dialog_ok_button);
        final Button deleteButton = (Button) view.findViewById(R.id.dialog_cancel_button);

        for(int i = 0; i<res.getCount(); i++){
            res.moveToPosition(i);
            if(id.compareTo(res.getString(0))==0) {
                StringBuffer buffer = new StringBuffer();
                nameEditText.setText(res.getString(1));
                surnameEditText.setText(res.getString(2));
                markEditText.setText(res.getString(3));
            }
        }

        mBuilder.setView(view);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update( id,
                        nameEditText.getText().toString(),
                        surnameEditText.getText().toString(),
                        markEditText.getText().toString());
                dialog.cancel();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete(id);
                ViewAll();
                dialog.cancel();
            }
        });
    }

    public void Update(String id, String name, String surname, String marks){
        boolean isUpdated = myDb.UpdateData(id, name, surname, marks);
        if(!isUpdated) Toast.makeText(MainActivity.this,"Update error.", Toast.LENGTH_SHORT).show();
        ViewAll();
    }

    public void Delete(final String id){
        Integer deletedRows = myDb.deleteData(id);
        if(deletedRows>0)
            Toast.makeText(MainActivity.this,"Data deleted.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this,"Data not deleted.", Toast.LENGTH_LONG).show();
        ViewAll();
    }
}
