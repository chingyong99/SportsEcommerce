package com.example.sportsecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewProductActivity extends AppCompatActivity {

    private String categoryName, productName, productDescription, productPrice, saveCurrentDate, saveCurrentTime;
    private Button addNewProductBtn;
    private EditText addProductName, addProductDescription, addProductPrice;
    private ImageView addProductImage;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        setTitle("Add New Product");

        //get intent from putExtra in SellerCategoryActivity
        categoryName = getIntent().getExtras().get("Category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        addNewProductBtn = (Button) findViewById(R.id.add_new_product_btn);
        addProductName = (EditText) findViewById(R.id.add_product_name);
        addProductDescription = (EditText) findViewById(R.id.add_product_description);
        addProductPrice = (EditText) findViewById(R.id.add_product_price);
        addProductImage = (ImageView) findViewById(R.id.select_product_image);
        loadingBar = new ProgressDialog(this);

        addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoGallery();
            }
        });

        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
    }


    private void openPhotoGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            //display imageUri to the imageView
            addProductImage.setImageURI(imageUri);
        }
    }

    private void validateProductData() {
        productName = addProductName.getText().toString();
        productDescription = addProductDescription.getText().toString();
        productPrice = addProductPrice.getText().toString();

        //If do not select the image
        if (imageUri == null) {
            Toast.makeText(this, "Product image is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Product name is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productDescription)) {
            Toast.makeText(this, "Product description is required", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, "Product price is required", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductImageInformation();
        }
    }

    private void StoreProductImageInformation() {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please hold on, while we are adding new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        //get the current date
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        //get the current time
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey);

        final UploadTask uploadTask = filePath.putFile(imageUri);

        //If failure occurs
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
            //Image Upload is successfully
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddNewProductActivity.this, "Product Image is uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        //This is just to get the imageUri not the link
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            //get the url link here
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AddNewProductActivity.this, "Get product image url successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", productDescription);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", productPrice);
        productMap.put("pname", productName);

        productsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(AddNewProductActivity.this, SellerCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AddNewProductActivity.this, "Product is stored successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
