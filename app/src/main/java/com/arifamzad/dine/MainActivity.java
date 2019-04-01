package com.arifamzad.dine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    TextView createNew, alreadyHave;


    public class MyApp extends Application {

        public void onCreate() {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNew = findViewById(R.id.create_new);
        //alreadyHave = findViewById(R.id.already_have);

        createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ManagerRegActivity.class);
                startActivity(i);
            }

        });

        alreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ManagerLoginActivity.class);
                startActivity(i);
            }

        });
    }
}
