package com.ceng319.firebasecode;

// TODO:  Read the following website as reference.
// TODO: https://firebase.google.com/docs/database/android/start/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteDB extends AppCompatActivity {

    private Button save;
    private EditText name;
    private EditText temperature;
    private EditText humidity;
    private EditText message;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    DataStructure mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_db);
        this.setTitle("Write to Database");

        findAllViews();
        getDatabase();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData(name.getText(), temperature.getText(), humidity.getText(), message.getText());
            }
        });
    }

    private void getDatabase(){
        // TODO: Find the reference form the database.
        database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String path = "userdata/" + mAuth.getUid();  // Write to the user account.
        myRef = database.getReference(path);

    }

    private DataStructure createData(Editable name, Editable temperature, Editable humidity, Editable message){
        // TODO: Get the timestamp
        Long time = System.currentTimeMillis()/1000;
        String timestamp = time.toString();
        return new DataStructure(String.valueOf(name),
                            String.valueOf(temperature),
                            String.valueOf(humidity),
                            String.valueOf(message),
                            timestamp);
     }


    private void writeData(Editable name, Editable temperature, Editable humidity, Editable message) {

        DataStructure mData = createData(name, temperature, humidity, message);
        // Select one of the following methods to update the data.
        // 1. To set the value of data
        // myRef.setValue(mData);
        // 2. To create a new node on database.
       //  myRef.push().setValue(mData);
        // TODO: Write the data to the database.
        // 3. To create a new node on database and detect if the writing is successful.
        myRef.push().setValue(mData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Value was set. ", Toast.LENGTH_LONG).show();
                gotoRead();  // after write the data, read it from another screen.
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Writing failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Find all the views for this activity.
    private void findAllViews(){
        save = findViewById(R.id.save);
        name = findViewById(R.id.name);
        temperature = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        message = findViewById(R.id.log);
    }
    private void gotoRead() {
        // TODO : Start the read option After login
        Intent intent1 = new Intent(getApplicationContext(), ReadDB.class);
        startActivity(intent1);
    }
}
