package com.example.sportsecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetailsActivity extends AppCompatActivity {

    TextView textId, textAmount, textStatus;
    private String totalAmount = "";
    private String productAmount="";
    private String paymentDetails="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        textId = (TextView) findViewById(R.id.txtId);
        textAmount = (TextView) findViewById(R.id.txtAmount);
        textStatus = (TextView) findViewById(R.id.txtStatus);

        //Get Intent
        totalAmount = getIntent().getStringExtra("Total Price");
        productAmount = getIntent().getStringExtra("Product Price");
        paymentDetails = getIntent().getStringExtra("PaymentDetails");


        try{
            JSONObject jsonObject = new JSONObject(paymentDetails);
            showDetails(jsonObject.getJSONObject("response"), totalAmount);
            Toast.makeText(PaymentDetailsActivity.this,"Payment Successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PaymentDetailsActivity.this, PaymentReceiptActivity.class);
            intent.putExtra("Total Price", String.valueOf(totalAmount));
            intent.putExtra("Product Price", String.valueOf(productAmount));
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            textId.setText(response.getString("id"));
            textStatus.setText(response.getString("status"));
            textAmount.setText(response.getString(String.format("%s", paymentAmount)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}