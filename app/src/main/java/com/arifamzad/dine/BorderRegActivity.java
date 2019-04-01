package com.arifamzad.dine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class BorderRegActivity extends AppCompatActivity {


    EditText bregName, bregEmail, bregPassword,bregPhone;
    Button bregButton;
    private DatabaseReference userDatabase;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_border_reg);

        bregName = findViewById(R.id.breg_name);
        bregEmail = findViewById(R.id.breg_email);
        bregButton = findViewById(R.id.breg_button);
        bregPassword = findViewById(R.id.breg_pass);
        bregPhone = findViewById(R.id.breg_phone);

        userDatabase = FirebaseDatabase.getInstance().getReference().child("border");
        mAuth=FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        bregButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });


    }

    private void startRegister(){
        final String name = bregName.getText().toString().trim();
        final String email = bregEmail.getText().toString().trim();
        final String password = bregPassword.getText().toString().trim();
        final String phone = bregPhone.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)){

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference current_user_data= userDatabase.child(user_id);

                        current_user_data.child("email").setValue(email);
                        current_user_data.child("name").setValue(name);
                        current_user_data.child("phone").setValue(phone);

                        Intent registerintent= new Intent(BorderRegActivity.this, BorderDrawerActivity.class);
                        registerintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(registerintent);
                        finish();

                        String token_id =  FirebaseInstanceId.getInstance().getToken();

                        Map<String, Object> tokenMap = new HashMap<>();
                        tokenMap.put("token_id", token_id);
                        tokenMap.put("border_name", name);

                        mFirestore.collection("users").document(user_id).set(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                            }
                        });


                    }

                    else{
                        Toast.makeText(BorderRegActivity.this, "You have already registered! Please login", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
        else{
            Toast.makeText(BorderRegActivity.this, "Fill all value please", Toast.LENGTH_LONG).show();
        }
    }
}
