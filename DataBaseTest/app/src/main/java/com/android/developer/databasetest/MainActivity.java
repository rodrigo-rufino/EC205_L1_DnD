package com.android.developer.databasetest;

import android.database.Cursor;
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

public class MainActivity extends AppCompatActivity {
    EditText editName, editSurname,editMarks, editId;
    Button btnAddData, btnUpdate, btnDelete;
    ListView list;
    ArrayAdapter<String> listAdapter;

    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        editName = (EditText) findViewById(R.id.name_edit_text);
        editSurname = (EditText) findViewById(R.id.surname_edit_text);
        editMarks = (EditText) findViewById(R.id.marks_edit_text);
        editId = (EditText) findViewById(R.id.id_edit_text);
        btnAddData = (Button) findViewById(R.id.add_button);
        btnUpdate = (Button) findViewById(R.id.update_button);
        btnDelete = (Button) findViewById(R.id.delete_button);

        list = (ListView) findViewById(R.id.list);
        listAdapter = new ArrayAdapter<String>(this, R.layout.list_item);
        list.setAdapter(listAdapter);
        ViewAll();
        AddData();
        Update();
        Delete();
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
                    boolean isInserted = myDb.insertData(
                        editName.getText().toString(),
                        editSurname.getText().toString(),
                        editMarks.getText().toString());
                    if(isInserted == true)
                        Toast.makeText(MainActivity.this,"Data Inserted.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(MainActivity.this,"Data not Inserted.", Toast.LENGTH_LONG).show();
                    clearFields();
                    ViewAll();
                }
            }
        );
    }
    public void ViewAll(){
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Empty.", Toast.LENGTH_LONG).show();
            showMessage("Error", "Nothing found.");
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
        clearFields();
    }

    public void ViewItem(String id){
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Empty.", Toast.LENGTH_LONG).show();
            showMessage("Error", "Nothing found.");
            return;
        }
        for(int i = 0; i<res.getCount(); i++){
            res.moveToPosition(i);
            if(id.compareTo(res.getString(0))==0) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("Id: "+res.getString(0)+"\n");
                buffer.append("Name: "+res.getString(1)+"\n");
                buffer.append("Surname: "+res.getString(2)+"\n");
                buffer.append("Marks: "+res.getString(3));
                Toast.makeText(MainActivity.this, buffer, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Update(){
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdated = myDb.UpdateData(
                        editId.getText().toString(),
                        editName.getText().toString(),
                        editSurname.getText().toString(),
                        editMarks.getText().toString());
                if(isUpdated){
                    Toast.makeText(MainActivity.this,"Data Updated.", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(MainActivity.this,"Update error.", Toast.LENGTH_LONG).show();
                clearFields();
                ViewAll();
            }
        });
    }

    public void Delete(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer deletedRows = myDb.deleteData(editId.getText().toString());
                if(deletedRows>0)
                    Toast.makeText(MainActivity.this,"Data deleted.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this,"Data not deleted.", Toast.LENGTH_LONG).show();
                clearFields();
                ViewAll();
            }
        });
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearFields(){
        editName.setText("");
        editSurname.setText("");
        editMarks.setText("");
        editId.setText("");
    }
}
