package com.example.sportsecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PaymentReceiptActivity extends AppCompatActivity {

    private TextView itemPrice, totalPrice;
    private String totalAmount = "";
    private String productAmount="";
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);
        setTitle("Payment Receipt");

        totalAmount = getIntent().getStringExtra("Total Price");
        productAmount = getIntent().getStringExtra("Product Price");

        itemPrice = (TextView) findViewById(R.id.item_price);
        totalPrice = (TextView) findViewById(R.id.total_price);
        doneButton = (Button) findViewById(R.id.done_button);

        itemPrice.setText("RM " + productAmount);
        totalPrice.setText("RM " + totalAmount);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentReceiptActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}