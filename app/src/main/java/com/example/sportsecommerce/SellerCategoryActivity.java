package com.example.sportsecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SellerCategoryActivity extends AppCompatActivity {

    private ImageView menShirt, womenShirt, menShoe, womenShoe;
    private ImageView accessories, football, badminton;

    private Button viewOrdersBtn, logoutBtn, manageProductBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_category);
        setTitle("Seller Activity");

        viewOrdersBtn = (Button) findViewById(R.id.view_orders_btn);
        logoutBtn = (Button) findViewById(R.id.seller_logout_btn);
        manageProductBtn = (Button) findViewById(R.id.manage_product_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerCategoryActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        viewOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerCategoryActivity.this, SellerViewOrderActivity.class);
                startActivity(intent);
            }
        });

        manageProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerCategoryActivity.this, HomeActivity.class);
                //Have to differentiate between seller and user home activity
                intent.putExtra("Seller", "Seller");
                startActivity(intent);
            }
        });

        menShirt = (ImageView) findViewById(R.id.men_shirt);
        womenShirt = (ImageView) findViewById(R.id.women_shirt);
        menShoe = (ImageView) findViewById(R.id.men_shoes);
        womenShoe = (ImageView) findViewById(R.id.women_shoes);
        accessories = (ImageView) findViewById(R.id.accessories);
        football = (ImageView) findViewById(R.id.football);
        badminton = (ImageView) findViewById(R.id.badminton);

        menShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menShirtIntent = new Intent(SellerCategoryActivity.this, AddNewProductActivity.class);
                //pass the value
                menShirtIntent.putExtra("Category", "menShirt");
                startActivity(menShirtIntent);
            }
        });

        womenShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent womenShirtIntent = new Intent(SellerCategoryActivity.this, AddNewProductActivity.class);
                //pass the value
                womenShirtIntent.putExtra("Category", "womenShirt");
                startActivity(womenShirtIntent);
            }
        });

        menShoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menShoeIntent = new Intent(SellerCategoryActivity.this, AddNewProductActivity.class);
                //pass the value
                menShoeIntent.putExtra("Category", "menShoe");
                startActivity(menShoeIntent);
            }
        });

        womenShoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent womenShoeIntent = new Intent(SellerCategoryActivity.this, AddNewProductActivity.class);
                //pass the value
                womenShoeIntent.putExtra("Category", "womenShoe");
                startActivity(womenShoeIntent);
            }
        });

        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessoriesIntent = new Intent(SellerCategoryActivity.this, AddNewProductActivity.class);
                //pass the value
                accessoriesIntent.putExtra("Category", "accessories");
                startActivity(accessoriesIntent);
            }
        });

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent footballIntent = new Intent(SellerCategoryActivity.this, AddNewProductActivity.class);
                //pass the value
                footballIntent.putExtra("Category", "football");
                startActivity(footballIntent);
            }
        });

        badminton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent badmintonIntent = new Intent(SellerCategoryActivity.this, AddNewProductActivity.class);
                //pass the value
                badmintonIntent.putExtra("Category", "badminton");
                startActivity(badmintonIntent);
            }
        });
    }
}