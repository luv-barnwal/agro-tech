package com.luv.agrotech.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luv.agrotech.LoginActivity;
import com.luv.agrotech.MainActivity;
import com.luv.agrotech.R;

import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Button order;
    ImageView prodImage;
    TextView prodName, prodQty, prodPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        prodImage = root.findViewById(R.id.prod_image);
        prodName = root.findViewById(R.id.prod_name);
        prodQty = root.findViewById(R.id.prod_qty);
        prodPrice = root.findViewById(R.id.prod_price);
        order = root.findViewById(R.id.order);

        try {
            String productQty = getArguments().getString("quantity");
            String price = "₹\t" + String.format("%.2f", Double.parseDouble(getArguments().getString("price")));
            int id = getArguments().getInt("id");
            String name = getArguments().getString("name");
            String image = getArguments().getString("image");

            Glide.with(root).load(image).into(prodImage);
            prodName.setText(name);
            prodQty.setText(productQty);
            prodPrice.setText(price);

            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAuth.getCurrentUser() != null){
                        databaseReference.child("Products").child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                int qty = Integer.parseInt(snapshot.child("productQty").getValue().toString()) - Integer.parseInt(productQty);
                                snapshot.getRef().child("productQty").setValue(qty).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        databaseReference.child("History").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    int number = (int) snapshot.getChildrenCount();
                                                    Map<String, Object> order = new HashMap<>();
                                                    order.put("productName", name);
                                                    order.put("productQty", productQty);
                                                    order.put("productPrice", price);
                                                    order.put("productId", id);
                                                    order.put("imageUrl", image);
                                                    snapshot.getRef().child(String.valueOf(number)).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                } else {
                                                    int number = 0;
                                                    Map<String, Object> order = new HashMap<>();
                                                    order.put("productName", name);
                                                    order.put("productQty", productQty);
                                                    order.put("productPrice", price);
                                                    order.put("productId", id);
                                                    order.put("imageUrl", image);
                                                    databaseReference.child("History").child(mAuth.getCurrentUser().getUid()).child(String.valueOf(number)).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
//                        }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Please login or sign up to make a purchase", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        } catch (Exception e){
            Log.e("Error", e.getMessage());
        }

        return root;
    }

}