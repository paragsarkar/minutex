package com.minutex.domain;

import java.io.Serializable;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class ProductDetails implements Serializable {


    /*private NetworkImageView productImageView;
    private TextView product_Name;
    private TextView category_name;
    private RupeeTextView sellingPriceText;*/

    private int imageid;
    private String product_Name;
    private String category_name;
    private String sellingPriceText;

    public ProductDetails()
    {

    }
    public ProductDetails(String product_Name,String category_name,String sellingPriceText)
    {
        //this.imageid = imageid;
        this.product_Name = product_Name;
        this.category_name = category_name;
        this.sellingPriceText = sellingPriceText;
    }
    public ProductDetails(int imageid,String product_Name,String category_name,String sellingPriceText)
    {
        this.imageid = imageid;
        this.product_Name = product_Name;
        this.category_name = category_name;
        this.sellingPriceText = sellingPriceText;
    }

    public int getImageid() {
        return imageid;
    }

    public String getProduct_Name() {
        return product_Name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getSellingPriceText() {
        return sellingPriceText;
    }
}
