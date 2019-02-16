package com.example.md_nayeem_asha.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.md_nayeem_asha.ecommerce.Model.AdminOrders;
import com.example.md_nayeem_asha.ecommerce.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrderActivity extends AppCompatActivity
{
    private RecyclerView orderList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList = findViewById(R.id.order_list);


        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();


        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model)
                    {
                        holder.userName.setText("Name: " + model.getName());
                        holder.userPhone.setText("Phone : " + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount: " + model.getTotalAmount());
                        holder.userDateTime.setText("Order at: " + model.getDate() +" "+ model.getTime());
                        holder.userShippingAddress.setText("Shipping Address: " + model.getAddress()+", " + model.getCity());

                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserProductActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
                                builder.setTitle("Have you shipped this product ?");

                                builder.setItems(options, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i)
                                    {
                                        if (i == 0)
                                        {
                                            String uID = getRef(position).getKey();
                                            RemoveOrder(uID);
                                        }
                                        else
                                        {
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
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder
    {
        public TextView  userName,userPhone,userTotalPrice,userDateTime,userShippingAddress;
        public Button showOrdersBtn;

        public AdminOrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhone = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showOrdersBtn = itemView.findViewById(R.id.show_all_product_btn);

        }
    }

    private void RemoveOrder(String uID)
    {
        ordersRef.child(uID).removeValue();
    }
}
