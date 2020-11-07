package com.example.sportsecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sportsecommerce.model.SellerOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerViewOrderActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef, cartListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_order);
        setTitle("Order Details");

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.seller_order_list_recyclerView);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<SellerOrders> options =
                new FirebaseRecyclerOptions.Builder<SellerOrders>()
                .setQuery(ordersRef, SellerOrders.class)
                .build();

        FirebaseRecyclerAdapter<SellerOrders, SellerOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<SellerOrders, SellerOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerOrdersViewHolder holder, final int position, @NonNull final SellerOrders model) {
                        //Display here
                        holder.userName.setText("Name: " + model.getName());
                        holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount: " + model.getTotalAmount());
                        holder.userAddress.setText("Shipping Address: " + model.getAddress());
                        holder.userDateTime.setText("Order at: " + model.getDate() + " "+ model.getTime());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options [] = new CharSequence[]
                                        {
                                           "Yes",
                                           "No"
                                        };;

                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerViewOrderActivity.this);
                                builder.setTitle("Have you shipped this customer order?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (i == 0){
                                            String uID = getRef(position).getKey();

                                            removeOrder(uID);
                                        }
                                        else{
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SellerOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_orders_layout, parent, false);
                        return new SellerOrdersViewHolder(view);

                    }
                };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class SellerOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhoneNumber, userTotalPrice, userAddress, userDateTime;

        public SellerOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userAddress = itemView.findViewById(R.id.order_address);
            userDateTime = itemView.findViewById(R.id.order_date_time);

        }
    }

    private void removeOrder(String uID) {

        ordersRef.child(uID).removeValue();

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Seller View").child(uID).child("Products");

        cartListRef.child("Cart List").removeValue();
    }
}