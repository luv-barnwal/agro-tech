package com.luv.agrotech.ui.home;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luv.agrotech.R;
import com.luv.agrotech.adapter.ProductAdapter;
import com.luv.agrotech.adapter.ProductCategoryAdapter;
import com.luv.agrotech.model.ProductCategory;
import com.luv.agrotech.model.Products;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ProductCategoryAdapter productCategoryAdapter;
    RecyclerView productCatRecycler, prodItemRecycler;
    ProductAdapter productAdapter;
    FirebaseAuth mAuth;
    TextView intro;
    private DatabaseReference databaseReference;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView productDetail = root.findViewById(R.id.productDetail);
        intro = root.findViewById(R.id.intro);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            intro.setText("Hello\t" + mAuth.getCurrentUser().getDisplayName() + "!");
        }

        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(new ProductCategory(1, "Trending"));
        productCategoryList.add(new ProductCategory(2, "Most Popular"));
        productCategoryList.add(new ProductCategory(3, "Ploughing Tools"));
//        productCategoryList.add(new ProductCategory(4, "Skin Care"));
//        productCategoryList.add(new ProductCategory(5, "Hair Care"));
//        productCategoryList.add(new ProductCategory(6, "Make Up"));
//        productCategoryList.add(new ProductCategory(7, "Fragrance"));

        setProductRecycler(productCategoryList, root);

        List<Products> productsList = new ArrayList<>();
        databaseReference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
//                    int total = (int) snapshot.getChildrenCount();
                    int i = 1;
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        int productId = i;
                        String productName = dataSnapshot.child("productName").getValue().toString();
                        String productQty = dataSnapshot.child("productQty").getValue().toString();
                        String productPrice = "₹\t" + String.format("%.2f", Double.parseDouble(dataSnapshot.child("productPrice").getValue().toString()));
                        String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                        productsList.add(new Products(productId, productName, productQty, productPrice, imageUrl));
                        i++;
                        Log.i("Name", String.valueOf(productId));
                        Log.i("url", imageUrl);
                    }
                    setProdItemRecycler(productsList, root);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        productsList.add(new Products(1, "Japanese Cherry Blossom", "250 ml", "$ 17.00", R.drawable.prod2));
//        productsList.add(new Products(2, "African Mango Shower Gel", "350 ml", "$ 25.00", R.drawable.prod1));
//        productsList.add(new Products(1, "Japanese Cherry Blossom", "250 ml", "$ 17.00", R.drawable.prod2));
//        productsList.add(new Products(2, "African Mango Shower Gel", "350 ml", "$ 25.00", R.drawable.prod1));
//        productsList.add(new Products(1, "Japanese Cherry Blossom", "250 ml", "$ 17.00", R.drawable.prod2));
//        productsList.add(new Products(2, "African Mango Shower Gel", "350 ml", "$ 25.00", R.drawable.prod1));


        return root;
    }
    private void setProductRecycler(List<ProductCategory> productCategoryList, View view){

        productCatRecycler = view.findViewById(R.id.cat_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        productCatRecycler.setLayoutManager(layoutManager);
        productCategoryAdapter = new ProductCategoryAdapter(getContext(), productCategoryList);
        productCatRecycler.setAdapter(productCategoryAdapter);

    }

    private void setProdItemRecycler(List<Products> productsList, View view){

        prodItemRecycler = view.findViewById(R.id.product_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        prodItemRecycler.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(getContext(), productsList);
        prodItemRecycler.setAdapter(productAdapter);

    }
}