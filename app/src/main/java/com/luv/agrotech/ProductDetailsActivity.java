package com.luv.agrotech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luv.agrotech.Fragment.CartFragment;

//import javax.swing.text.html.ImageView;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView productImageView, imageBack, add, reduce;
    TextView productName, productPrice, quantityPurchased, productDetail;
    int productId, productQty, amount = 1;
    ConstraintLayout constraintLayout;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    Button cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        productImageView = findViewById(R.id.productImage);
        imageBack = findViewById(R.id.imageBack);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.price);
        quantityPurchased = findViewById(R.id.quantityPurchased);
        productDetail = findViewById(R.id.productDetail);
        constraintLayout = findViewById(R.id.detailsLayout);
        add = findViewById(R.id.add);
        reduce = findViewById(R.id.reduce);
        cart = findViewById(R.id.cart);

        Intent intent = getIntent();
        Glide.with(this).load(intent.getStringExtra("image")).into(productImageView);
        productName.setText(intent.getStringExtra("name"));
        productPrice.setText(intent.getStringExtra("price"));
        productId = Integer.parseInt(intent.getStringExtra("id"));
        productQty = Integer.parseInt(intent.getStringExtra("quantity"));
        productImageView.bringToFront();
        databaseReference.child("Products").child(String.valueOf(productId)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    productDetail.setText(snapshot.child("productDetails").getValue().toString());
                    Log.i("Product", String.valueOf(productId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQty--;
                amount++;
                quantityPurchased.setText(Integer.toString(amount));
                if (!reduce.isClickable()){
                    reduce.setClickable(true);
                    reduce.setImageResource(R.drawable.ic_minus_shaded);
                }
                if (productQty == 1){
                    add.setClickable(false);
                    add.setImageResource(R.drawable.ic_plus_unshaded);
                }
                String price = (String) productPrice.getText().subSequence(2, productPrice.getText().toString().length());
                Log.i("Price", String.valueOf(Double.parseDouble(price) * amount));
            }
        });

        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productQty++;
                amount--;
                quantityPurchased.setText(Integer.toString(amount));
                if (!add.isClickable()){
                    add.setClickable(true);
                    add.setImageResource(R.drawable.ic_plus_shaded);
                }
                if (amount == 1){
                    reduce.setClickable(false);
                    reduce.setImageResource(R.drawable.ic_minus_unshaded);
                }
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = String.valueOf(Double.parseDouble((String) productPrice.getText().subSequence(2, productPrice.getText().toString().length())) * amount);
//                Log.i("PRice", price);
//                Intent cartIntent = new Intent(ProductDetailsActivity.this, CartFragment.class);
//                startActivity(cartIntent);
//                CartFragment cartFragment = new CartFragment();
//                getSupportFragmentManager().beginTransaction().add(R.id.productLayout, cartFragment)
//                        .addToBackStack(CartFragment.class.getSimpleName())
//                        .commit();
                ConstraintLayout layout = findViewById(R.id.productLayout);
                layout.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("price", price);
                bundle.putString("quantity", String.valueOf(amount));
                bundle.putInt("id", productId);
                bundle.putString("name", productName.getText().toString());
                bundle.putString("image", intent.getStringExtra("image"));
                CartFragment cartFragment = new CartFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                cartFragment.setArguments(bundle);
                fragmentManager.beginTransaction().add(R.id.content, cartFragment).commit();
            }
        });
//        constraintLayout.bringToFront();
//        productImageView.getLayoutParams().height = 800;
//        productImageView.getLayoutParams().width = 512;
    }
}