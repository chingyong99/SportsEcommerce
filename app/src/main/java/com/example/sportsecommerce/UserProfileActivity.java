package com.example.sportsecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportsecommerce.prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private Button userSettingButton, userLogOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("User Profile");

        TextView userNameTextView = (TextView) findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = (CircleImageView) findViewById(R.id.profile_image);

        userNameTextView.setText(Prevalent.onlineUser.getName());
        Picasso.get().load(Prevalent.onlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);

        userSettingButton = (Button) findViewById(R.id.user_profile_setting_btn);
        userLogOutButton = (Button) findViewById(R.id.user_logout_button);

        userSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        userLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}