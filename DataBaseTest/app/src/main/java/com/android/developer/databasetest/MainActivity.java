package com.android.developer.databasetest;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editName, editSurname,editMarks;
    Button btnAddData, btnViewAll;

    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        editName = (EditText) findViewById(R.id.name_edit_text);
        editSurname = (EditText) findViewById(R.id.surname_edit_text);
        editMarks = (EditText) findViewById(R.id.marks_edit_text);
        btnAddData = (Button) findViewById(R.id.add_button);
        btnViewAll = (Button) findViewById(R.id.viewData_button);
        AddData();
        ViewAll();
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
                    editName.setText("");
                    editSurname.setText("");
                    editMarks.setText("");
                }
            }
        );
    }
    public void ViewAll(){
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDb.getAllData();
                if(res.getCount() == 0) {
                    Toast.makeText(MainActivity.this, "Empty.", Toast.LENGTH_LONG).show();
                    showMessage("Error", "Nothing found.");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()){
                    buffer.append("Id: "+res.getString(0)+"\n");
                    buffer.append("Name: "+res.getString(1)+"\n");
                    buffer.append("Surname: "+res.getString(2)+"\n");
                    buffer.append("Marks: "+res.getString(3)+"\n\n");
                }
                showMessage("Data", buffer.toString());
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
}
