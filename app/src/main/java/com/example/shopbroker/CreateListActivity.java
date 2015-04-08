package com.example.shopbroker;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CreateListActivity extends ActionBarActivity {

    //Activity scope variables to handle onClicks
    private ArrayList<String> itemsShared = new ArrayList<String>();
    private ArrayList<String> itemsMine = new ArrayList<String>();
    private int itemSpot = 0;
    private boolean toAdd = false;
    private DBAdapter dbhelper;
    private long rowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            rowID = getIntent().getLongExtra("ID", 0);
           // String rowID_string = Long.toString(rowID);
           // TextView id = (TextView) findViewById(R.id.editText);
           // id.setText(rowID_string);
            dbhelper = new DBAdapter(this);
            dbhelper.open();
            Cursor cursor = dbhelper.getRow(rowID);
            String ListName = cursor.getString(DBAdapter.COL_NAME);
            setTitle(ListName);
        }
        dbhelper = new DBAdapter(this);
        dbhelper.open();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //onClick for Mine button
    public void addToMine(View v){
        String temp;
        TextView itemET = (TextView) findViewById(R.id.editTextItem);
        temp = itemET.getText().toString();
        if(temp.equals("") || temp.equals(" "))//checks if input is blank or a space(account for 1 accidental spacebar hit)s
            return;
        else {
            ItemRetrieval retrievalTask = new ItemRetrieval();//get price using different thread;
            retrievalTask.execute(temp);
            //test toast stuff for debugging
            //int duration = Toast.LENGTH_SHORT;
            //Toast toast = Toast.makeText(this, itemPrice, duration);
            //toast.show();
            itemET.setText("");//reset input
            toAdd = true;//test value
            dbhelper.insertRow_to_Items(temp, String.valueOf(rowID));
            populateListViewMine(toAdd, temp);
        }
    }

    //onClick for Shared Button
    public void addToShared(View v){
        String temp;
        TextView itemET = (TextView) findViewById(R.id.editTextItem);
        temp = itemET.getText().toString();//get text from EditText and convert to string
        if(temp.equals("") || temp.equals(" "))//checks if input is blank or a space(account for 1 accidental spacebar hit)
            return;
        else {
            itemET.setText("");//reset input
            toAdd = true;//test value
            populateListViewShared(toAdd, temp);
        }
        //more methods and things for updating online database in the future
    }

    //create list view for Mine
    public void populateListViewMine(boolean test, String item){
        if(test)
            itemsMine.add(item);
        //dbhelper = new DBAdapter(this);
        //dbhelper.open();
        //Cursor cursor = dbhelper.getAllItemRows();
        Cursor cursor = dbhelper.getItemRow(rowID);
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME,DBAdapter.KEY_ITEM_LISTID};
        int[] viewIDs = new int[]{R.id.item,R.id.textView};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_create_list,
                cursor,fieldnames,viewIDs,0);
        ListView mylist = (ListView) findViewById(R.id.listViewMine);
        mylist.setAdapter(myCursorAdapter);

        //ListAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsMine);
       // ListView listView = (ListView) findViewById(R.id.listViewMine);
       // listView.setAdapter(itemsAdapter);
    }

    //create list view for shared
    public void populateListViewShared(boolean test, String item){
        if(test)
            itemsShared.add(item);
        ListAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsShared);//convert to list items for ListView
        ListView listView = (ListView) findViewById(R.id.listViewShared);
        listView.setAdapter(itemsAdapter);
    }

    /**
     * Background task to get price
     */
    private class ItemRetrieval extends AsyncTask<String, Void, String> {

        public static final String Key = "nsfceev8vy89jk5pcg4x4j52";
        public static final String baseURL = "api.walmartlabs.com/v1/search?query=";
        public static final String format = "&format=json&apiKey=";

        @Override
        protected String doInBackground(String... items) {
            String item = items[0];
            //concatenate together constants with item to create url to
            String urlToSend = baseURL + item + format + Key;
            JSONParser jObj = new JSONParser();
            String itemPrice = jObj.getJSONFromUrl(urlToSend);
            //String itemPrice = "0.00";//hardcoded for testing
            return itemPrice;
        }

        protected void onPostExecute(String price){
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), price, duration);
            toast.show();
        }
    }
}
