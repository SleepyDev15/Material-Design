package com.google.codelabs.mdc.java.shrine.network;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.google.codelabs.mdc.java.shrine.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A product entry in the list of products.
 */
public class ProductEntry {
    private static final String TAG = ProductEntry.class.getSimpleName();

    public final String title;
    public final Uri dynamicUrl;
    public final String url;
    public final String price;
    public final String description;

    public ProductEntry(String title, String dynamicUrl, String url, String price, String description) {
        this.title = title;
        this.dynamicUrl = Uri.parse(dynamicUrl);
        this.url = url;
        this.price = price;
        this.description = description;
    }



    /**
     * Loads a raw JSON at R.raw.products and converts it into a list of ProductEntry objects
     */
    public static List<ProductEntry> initProductEntryList(Resources resources) {
        List<ProductEntry> productEntryList = new ArrayList<>();
        productEntryList.clear();
        InputStream inputStream = resources.openRawResource(R.raw.products);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonProductsString = writer.toString();
        Gson gson = new Gson();
        Type productListType = new TypeToken<ArrayList<ProductEntry>>() {
        }.getType();

       //
        try {
            JSONObject obj = new JSONObject(jsonProductsString);
            JSONArray m_jArry = obj.getJSONArray("products");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String title = jo_inside.getString("title");
                String url = jo_inside.getString("url");
                String price = jo_inside.getString("price");

                productEntryList.add(new ProductEntry(title, "",url, price, ""));

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        //return gson.fromJson(jsonProductsString, productListType);
        return productEntryList;
    }
}