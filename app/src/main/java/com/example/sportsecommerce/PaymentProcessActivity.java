package com.example.sportsecommerce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportsecommerce.config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PaymentProcessActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PALPAL_CLIENT_ID);

    Button payNowBtn;
    TextView totalAmountTxt;

    String totalAmount ="";
    String productAmount="";

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_process);

        totalAmount = getIntent().getStringExtra("Total Price");
        productAmount = getIntent().getStringExtra("Product Price");

        //Start payPal service, set up the payPal service config
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        payNowBtn = (Button) findViewById(R.id.total_amount_button);
        totalAmountTxt = (TextView) findViewById(R.id.total_amount_text);

        totalAmountTxt.setText("RM " + totalAmount);

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

    }

    private void processPayment() {
        //Create and Specify the payment object, totalAmount will be the value from cart
        //MYR is static currency and Total Amount is description
        PayPalPayment payPalPayment = new
                PayPalPayment(new BigDecimal(String.valueOf(totalAmount)),
                "MYR", "Total Amount", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class); //putting PayPal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);  //putting PayPal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment); //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        //PayPal login and payment screen will be presented at this point
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            //Check to see if the result that comes back is RESULT_OK
            if (resultCode == RESULT_OK) {
                //Get the payment confirmation
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) { //If confirmation is not null
                    try { //Get the payment details
                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        startActivity(new Intent(this, PaymentDetailsActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("Total Price", totalAmount)
                                .putExtra("Product Price",productAmount));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {  //If user cancel payment
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
}
