package com.example.sportsecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sportsecommerce.model.Users;
import com.example.sportsecommerce.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button testRegister, testJoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testRegister = (Button) findViewById(R.id.test_register);
        testJoin = (Button) findViewById(R.id.test_join);

        Paper.init(this);

        testRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        testJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if (userPhoneKey != "" && userPasswordKey != "")
        {
            if (!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey))
            {
                userAccess(userPhoneKey, userPasswordKey);
            }
        }
    }

    private void userAccess(final String phone, final String password)
    {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()) {
                    //pass value to Users class
                    Users userData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    //Check whether phone and password by user input is equal to database
                    if (userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {
                            Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.onlineUser = userData;
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "This account does not exist, create a new account now", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}