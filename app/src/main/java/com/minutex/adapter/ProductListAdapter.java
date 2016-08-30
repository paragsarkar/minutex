package com.minutex.adapter;

import android.content.Context;
import android.net.Network;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.minutex.R;
import com.minutex.common.widgets.RupeeTextView;
import com.minutex.domain.ProductDetails;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    public static final String TAG = "ProductListAdapter";
    private Context mContext;
    private ArrayList<ProductDetails> productDetails;
    private ArrayList<ProductDetails> trydetails;
    public ProductListAdapter(Context mContext,ArrayList<ProductDetails> productDetails)
    {
        this.mContext = mContext;
        this.productDetails = productDetails;
        trydetails = productDetails;
    }
    @Override
    public int getItemCount() {
        Log.e(TAG,String.valueOf(productDetails.size()));
        return productDetails.size();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e(TAG, String.valueOf(position));
        ProductDetails productDetail = trydetails.get(position);
        holder.productImageView.setImageResource(productDetail.getImageid());
        holder.product_Name.setText(productDetail.getProduct_Name());
        holder.category_name.setText(productDetail.getCategory_name());
        holder.sellingPriceText.setRupeeText(productDetail.getSellingPriceText());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        //private NetworkImageView productImageView;
        private ImageView productImageView;
        private TextView product_Name;
        private TextView category_name;
        private RupeeTextView sellingPriceText;
        private Button btn_contact;

        public ViewHolder(View itemView)
        {
            super(itemView);
            //productImageView = (NetworkImageView)itemView.findViewById(R.id.nil_product_image);
            productImageView = (ImageView)itemView.findViewById(R.id.imv_product_image);
            product_Name = (TextView)itemView.findViewById(R.id.tv_product_name);
            category_name = (TextView)itemView.findViewById(R.id.tv_product_category);
            sellingPriceText  =(RupeeTextView)itemView.findViewById(R.id.rptv_detail);
            btn_contact = (Button)itemView.findViewById(R.id.btn_concats);
        }
    }
    public class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }
}
