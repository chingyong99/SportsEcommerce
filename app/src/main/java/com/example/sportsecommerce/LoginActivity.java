package com.example.sportsecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.rey.material.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sportsecommerce.model.Users;
import com.example.sportsecommerce.prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText loginPhoneNum, loginPassword;
    private CheckBox checkboxRememberMe;
    private Button loginButton;
    private TextView sellerLink, notSellerLink;
    private String parentNode = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        loginPhoneNum = (EditText) findViewById(R.id.login_phone);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        sellerLink = (TextView) findViewById(R.id.seller_panel);
        notSellerLink = (TextView) findViewById(R.id.not_seller_panel);
        checkboxRememberMe = (CheckBox) findViewById(R.id.remember_me_checkbox);
        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                userLogin();
            }
        });

        sellerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Seller Login");
                sellerLink.setVisibility(View.INVISIBLE);
                notSellerLink.setVisibility(View.VISIBLE);
                parentNode = "Sellers";
            }
        });

        notSellerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                sellerLink.setVisibility(View.VISIBLE);
                notSellerLink.setVisibility(View.INVISIBLE);
                parentNode = "Users";
            }
        });

    }

    private void userLogin()
    {
        String phone = loginPhoneNum.getText().toString();
        String password = loginPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            loginPhoneNum.setError("Please enter the phone number");
        }
        else if (TextUtils.isEmpty(password))
        {
            loginPassword.setError("Please enter the password");
        }
        else
        {
            userLoginSuccess(phone, password);
        }

    }

    private void userLoginSuccess(final String phone, final String password)
    {
        if(checkboxRememberMe.isChecked())
        {
            //store user data into android memory
            Paper.book().write(Prevalent.userPhoneKey, phone);
            Paper.book().write(Prevalent.userPasswordKey, password);
        }

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentNode).child(phone).exists())
                {
                    //pass value to Users class
                    Users userData = dataSnapshot.child(parentNode).child(phone).getValue(Users.class);

                    //Check whether phone and password by user input is equal to database
                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                            if (parentNode.equals("Sellers"))
                            {
                                Toast.makeText(LoginActivity.this,"Seller Login successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SellerCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if (parentNode.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this,"Login successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.onlineUser = userData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"This account does not exist, pls try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}