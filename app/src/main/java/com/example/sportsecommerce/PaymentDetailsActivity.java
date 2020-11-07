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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        textId = (TextView) findViewById(R.id.txtId);
        textAmount = (TextView) findViewById(R.id.txtAmount);
        textStatus = (TextView) findViewById(R.id.txtStatus);

        //Get Intent
        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
            Toast.makeText(PaymentDetailsActivity.this,"Payment Successfully", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(PaymentDetailsActivity.this, HomeActivity.class);
            startActivity(homeIntent);

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