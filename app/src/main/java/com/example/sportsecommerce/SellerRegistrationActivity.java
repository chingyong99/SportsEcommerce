package com.example.sportsecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private EditText username, registerPhoneNum, registerPassword;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);
        setTitle("Seller Register");

        username = (EditText) findViewById(R.id.register_name);
        registerPhoneNum = (EditText) findViewById(R.id.register_phone);
        registerPassword = (EditText) findViewById(R.id.register_password);
        signUpButton = (Button) findViewById(R.id.register_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAccount();
            }
        });
    }

    private void newAccount() {
        String name = username.getText().toString();
        String phone = registerPhoneNum.getText().toString();
        String password = registerPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            username.setError("Please enter the username");
        } else if (TextUtils.isEmpty(phone)) {
            registerPhoneNum.setError("Please enter the phone number");
        } else if (TextUtils.isEmpty(password)) {
            registerPassword.setError("Please enter the password");
        } else {
            validateUser(name, phone, password);
        }
    }

    private void validateUser(final String name, final String phone, final String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Sellers").child(phone).exists())) {
                    //if not exist then create new account (for users)
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", phone);
                    userDataMap.put("password", password);
                    userDataMap.put("name", name);

                    rootRef.child("Sellers").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SellerRegistrationActivity.this, "Your account have been created", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SellerRegistrationActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SellerRegistrationActivity.this, "This " + phone + " already existed", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SellerRegistrationActivity.this, SellerRegistrationActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}