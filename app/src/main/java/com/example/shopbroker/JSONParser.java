package com.example.shopbroker;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JSONParser {

    // constructor
    public JSONParser() {
    }

    public String getJSONFromUrl(String url) {
        // Making HTTP request
        try {
            //connection to web
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest= new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(getRequest);


            HttpEntity httpEntity = httpResponse.getEntity();
            String data = EntityUtils.toString(httpEntity);//get JSON data
            JSONObject jObj = new JSONObject(data);//create JSON object with data as input
            JSONArray jArray = jObj.getJSONArray("items");//get array of JSON objects called 'items
            float minCost = 1000.00f;

            //loop through json array of items
            for(int i=0; i < jArray.length(); i++) {
                JSONObject jRealObject = jArray.getJSONObject(i);
                String itemID = jRealObject.getString("itemId");
                String cost = jRealObject.getString("salePrice");//get cost as String
                float fCost = Float.valueOf(cost);//convert to float to compare

                //maintain minimum cost
                if(fCost < minCost)
                    minCost = fCost;
            }
            return Float.toString(minCost);
        }
        //required catches
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "Could not find item's price.";//if getting data doesn't work
    }
}
