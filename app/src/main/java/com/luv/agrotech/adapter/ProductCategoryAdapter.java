package com.luv.agrotech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.luv.agrotech.R;
import com.luv.agrotech.model.ProductCategory;
import com.luv.agrotech.model.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductViewHolder>{

    Context context;
    List<ProductCategory> productCategoryList;

    public ProductCategoryAdapter(Context context, List<ProductCategory> productCategoryList) {
        this.context = context;
        this.productCategoryList = productCategoryList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_row_item, parent, false);
        // lets create a recyclerview row item layout file
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.catagoryName.setText(productCategoryList.get(position).getProductName());
        List<Products> productsList = new ArrayList<>();
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (productCategoryList.get(position).getProductName().equals("Trending")){
//                    DatabaseReference databaseReference;
//                    databaseReference = FirebaseDatabase.getInstance().getReference();
//                    databaseReference.child("Products").orderByChild("productQty").addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                                    int productId = Integer.parseInt(dataSnapshot.getValue(String.class));
//                                    String productName = dataSnapshot.child("productName").getValue().toString();
//                                    String productQty = dataSnapshot.child("productQty").getValue().toString();
//                                    String productPrice = "₹\t" + String.format("%.2f", Double.parseDouble(dataSnapshot.child("productPrice").getValue().toString()));
//                                    String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
//                                    productsList.add(new Products(productId, productName, productQty, productPrice, imageUrl));
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView catagoryName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            catagoryName = itemView.findViewById(R.id.cat_name);

        }
    }

}
