package com.example.shopbroker;

/**
 * Created by Jacob on 4/2/2015.
 */
public class ItemRetrieval {
    public String getPrice(String item){
        //concatenate together constants with item to create url to
        String urlToSend = Constants.baseURL + item + Constants.format + Constants.Key;
        String itemPrice = "0.00";
        return itemPrice;
    }
}
