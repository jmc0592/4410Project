package com.example.shopbroker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CreateListActivity extends ActionBarActivity {

    //Activity scope variables to handle onClicks
    private ArrayList<String> itemsShared = new ArrayList<String>();
    private ArrayList<String> itemsMine = new ArrayList<String>();
    private int itemSpot = 0;
    private boolean toAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            long rowID = getIntent().getLongExtra("ID", 0);
            String rowID_string = Long.toString(rowID);

            TextView id = (TextView) findViewById(R.id.editText);
            id.setText(rowID_string);
        }
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
        itemET.setText("");
        toAdd = true;//test value
        populateListViewMine(toAdd, temp);
    }

    //onClick for Shared Button
    public void addToShared(View v){
        String temp;
        TextView itemET = (TextView) findViewById(R.id.editTextItem);
        temp = itemET.getText().toString();//get text and convert to string
        itemET.setText("");//reset editText to blank
        toAdd = true;//test value
        populateListViewShared(toAdd, temp);

        //more methods and things for updating online database in the future
    }

    //create list view for Mine
    public void populateListViewMine(boolean test, String item){
        if(test)
            itemsMine.add(item);
        ListAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsMine);
        ListView listView = (ListView) findViewById(R.id.listViewMine);
        listView.setAdapter(itemsAdapter);
    }

    //create list view for shared
    public void populateListViewShared(boolean test, String item){
        if(test)
            itemsShared.add(item);
        ListAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsShared);//convert to list items for ListView
        ListView listView = (ListView) findViewById(R.id.listViewShared);
        listView.setAdapter(itemsAdapter);
    }
}
