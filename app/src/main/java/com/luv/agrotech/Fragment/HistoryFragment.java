package com.luv.agrotech.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luv.agrotech.R;
import com.luv.agrotech.adapter.ProductAdapter;
import com.luv.agrotech.model.Products;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView prodItemRecycler;
    ProductAdapter productAdapter;
    FirebaseAuth mAuth;
    TextView intro;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        intro = root.findViewById(R.id.intro);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        intro.setText("Hello\t" + mAuth.getCurrentUser().getDisplayName() + "!");

        List<Products> productsList = new ArrayList<>();
        databaseReference.child("History").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        int productId = Integer.parseInt(dataSnapshot.child("productId").getValue().toString());
                        String productName = dataSnapshot.child("productName").getValue().toString();
                        String productQty = dataSnapshot.child("productQty").getValue().toString();
                        String productPrice = dataSnapshot.child("productPrice").getValue().toString();
                        String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                        productsList.add(new Products(productId, productName, productQty, productPrice, imageUrl));
                    }
                    setProdItemRecycler(productsList, root);
                } else {
                    Toast.makeText(getContext(), "Please make a purchase to view the history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }

    private void setProdItemRecycler(List<Products> productsList, View root) {
        prodItemRecycler = root.findViewById(R.id.product_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        prodItemRecycler.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(getContext(), productsList);
        prodItemRecycler.setAdapter(productAdapter);
    }
}