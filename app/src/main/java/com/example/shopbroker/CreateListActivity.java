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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.ParseObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CreateListActivity extends ActionBarActivity {

    //Activity scope variables
    private ArrayList<String> itemsShared = new ArrayList<String>();
    private ArrayList<String> itemsMine = new ArrayList<String>();
    private String cPrice = "";
    private String cTemp;
    private DBAdapter dbhelper;
    private long rowID;
    private float totalPrice = 0.00f;
    private String shared;
    private boolean priceFound = false;

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
        //load lists on activity start
        populateListViewShared(rowID);
        populateListViewMine(rowID);

        ListView listMine = (ListView) findViewById(R.id.listViewMine);
        ListView listShared = (ListView) findViewById(R.id.listViewShared);
        listMine.setItemsCanFocus(true);




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
        shared = "0";
        TextView itemET = (TextView) findViewById(R.id.editTextItem);
        cTemp = itemET.getText().toString();
        if(cTemp.equals("") || cTemp.equals(" "))//checks if input is blank or a space(account for 1 accidental spacebar hit)s
            return;
        else {
            ItemRetrieval retrievalTask = new ItemRetrieval();//get price using different thread;
            retrievalTask.execute(cTemp);
            itemET.setText("");//reset input
            dbhelper.insertRow_to_Items(cTemp, String.valueOf(rowID), cPrice, shared);
            populateListViewMine(cTemp);
        }
    }

    //onClick for Shared Button
    public void addToShared(View v){
        shared = "1";
        TextView itemET = (TextView) findViewById(R.id.editTextItem);
        cTemp = itemET.getText().toString();
        if(cTemp.equals("") || cTemp.equals(" "))//checks if input is blank or a space(account for 1 accidental spacebar hit)s
            return;
        else {
            ItemRetrieval retrievalTask = new ItemRetrieval();//get price using different thread;
            retrievalTask.execute(cTemp);
            itemET.setText("");//reset input
            dbhelper.insertRow_to_Items(cTemp, String.valueOf(rowID), cPrice, shared);
            populateListViewShared(cTemp);
        }
        //more methods and things for updating online database in the future
    }

    //overloaded for onCreate list populate
    public void populateListViewMine(long id){
        Cursor cursor = dbhelper.getAllMineRows(id);//get item if it's tagged as mine
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME,DBAdapter.KEY_ITEM_LISTID, DBAdapter.KEY_PRICE, DBAdapter.KEY_SHARED};
        int[] viewIDs = new int[]{R.id.item,R.id.textView, R.id.price};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_create_list,
                cursor,fieldnames,viewIDs,0);
        ListView mylist = (ListView) findViewById(R.id.listViewMine);
        mylist.setAdapter(myCursorAdapter);
    }

    //overloaded for onCreate list populate
    public void populateListViewShared(long id){
        Cursor cursor = dbhelper.getAllSharedRows(id);//get item if it's tagged as shared
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME,DBAdapter.KEY_ITEM_LISTID, DBAdapter.KEY_PRICE, DBAdapter.KEY_SHARED};
        int[] viewIDs = new int[]{R.id.item,R.id.textView, R.id.price};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_create_list,
                cursor,fieldnames,viewIDs,0);
        ListView mylist = (ListView) findViewById(R.id.listViewShared);
        mylist.setAdapter(myCursorAdapter);

    }
    //create list view for Mine
    public void populateListViewMine(String item){
            itemsMine.add(item);
        //dbhelper = new DBAdapter(this);
        //dbhelper.open();
        //Cursor cursor = dbhelper.getAllItemRows();
        Cursor cursor = dbhelper.getItemRowMine(rowID);//get item if it's tagged as mine
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME,DBAdapter.KEY_ITEM_LISTID, DBAdapter.KEY_PRICE, DBAdapter.KEY_SHARED};
        int[] viewIDs = new int[]{R.id.item,R.id.textView, R.id.price};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_create_list,
                cursor,fieldnames,viewIDs,0);
        ListView mylist = (ListView) findViewById(R.id.listViewMine);
        mylist.setAdapter(myCursorAdapter);
    }

    //create list view for shared
    public void populateListViewShared(String item){

            itemsShared.add(item);
        Cursor cursor = dbhelper.getItemRowShared(rowID);//get item if it's tagged as shared
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME,DBAdapter.KEY_ITEM_LISTID, DBAdapter.KEY_PRICE, DBAdapter.KEY_SHARED};
        int[] viewIDs = new int[]{R.id.item,R.id.textView, R.id.price};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(this,R.layout.listview_create_list,
                cursor,fieldnames,viewIDs,0);
        ListView mylist = (ListView) findViewById(R.id.listViewShared);
        mylist.setAdapter(myCursorAdapter);

        //online handling
        if(priceFound)
        {
            ParseObject parseObj = new ParseObject("Items");
            Cursor cursor1 = dbhelper.getRow(rowID);//get row from current rowID
            String listName = cursor1.getString(DBAdapter.COL_NAME);
            parseObj.put("listName", listName);
            parseObj.put("itemName", item);
            parseObj.put("price", cPrice);
            parseObj.saveInBackground();
        }
    }

    public void addToTotal(String addPrice){
        String totalPriceChar;

        totalPrice += Float.parseFloat(addPrice);//update global total by converting String to float
        BigDecimal totalFormatted = new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        totalPriceChar = Float.toString(totalFormatted.floatValue());//after update convert back to String

        //update textView
        TextView totalText = (TextView) findViewById(R.id.totalNumeric);
        totalText.setText(totalPriceChar);
    }

    public void delItem(View v){
        Toast toast = Toast.makeText(this, "delete", Toast.LENGTH_SHORT);
        toast.show();

        ListView listMine = (ListView) findViewById(R.id.listViewMine);
        listMine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "listener", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });
    }

    /**
     * Background task to get price
     */
    private class ItemRetrieval extends AsyncTask<String, Void, String> {

        public static final String Key = "nsfceev8vy89jk5pcg4x4j52";
        public static final String baseURL = "http://api.walmartlabs.com/v1/search?query=";
        public static final String format = "&format=json&apiKey=";

        @Override
        protected String doInBackground(String... items) {
            String item = items[0];
            //concatenate together constants with item to create url to
            String urlToSend = baseURL + item + format + Key;
            JSONParser jObj = new JSONParser();
            String itemPrice = jObj.getJSONFromUrl(urlToSend);//get price from parsing JSON
            return itemPrice;
        }

        protected void onPostExecute(String price){

            cPrice = price;
            Cursor cursor = dbhelper.getAllItemRows();
            cursor.moveToLast();
            long id = cursor.getLong(DBAdapter.COL_ROWID);//get id of entered item
            dbhelper.updateItemPrice(id, cPrice);//update price in database
            if(!price.equals("Price not found.")) {
                priceFound = true;
                addToTotal(cPrice);
            }
            //choose which listview to refresh
            if(shared.equals("0"))
                populateListViewMine(cTemp);//repopulate listview
            else
                populateListViewShared(cTemp);

            //toasts for debugging purposes
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), cPrice, duration);
            toast.show();

            cPrice = "";//reset to blank input
            priceFound = false;
        }
    }
}
