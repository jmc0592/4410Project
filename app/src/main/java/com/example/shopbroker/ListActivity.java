package com.example.shopbroker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ListActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{
    DBAdapter myDb;
    EditText Listname;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        openDB();
        //Listname = (EditText) findViewById(R.id.editText2);
        /*
         * Moved to App.java file. fixed app crash issue when pressing back button.

       //Parse.enableLocalDatastore(this);
       //Parse.initialize(this);
        */
       // ParseObject testObject = new ParseObject("TestObject");
       // testObject.put("foo", "bar");
       // testObject.saveInBackground();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        //switch fragments based on their position
        switch(position) {
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ListsFragment.newInstance(position + 1))
                        .commit();
                break;
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ListsFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, FriendsFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, StoresFragment.newInstance(position + 1))
                        .commit();
                break;
        }
        //displayrecord();
    }

    //get title for menu based on nav drawer selection
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_lists);
                break;
            case 2:
                mTitle = getString(R.string.title_friends);
                break;
            case 3:
                mTitle = getString(R.string.title_stores);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.list, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    // called when button is pressed
    public void onClick_AddList(View v){
        displayText("Clicked add List");
        Listname = (EditText) findViewById(R.id.editText2);
        //time me up brother
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat();
        String date = df.format(c.getTime());
        //String list = Listname.getText().toString();
        if(!TextUtils.isEmpty(Listname.getText().toString())){
          myDb.insertRow(Listname.getText().toString(),date);
        }
        //long newId = myDb.insertRow("<List>",date);
        populatelistview();
        //Query for record added
        //Cursor cursor = myDb.getRow(newId);
        //displayRecordset(cursor);
        //Intent intent = new Intent(this, CreateListActivity.class);
        //startActivity(intent);
    }
    public void onClick_ClearAll(View v){
        displayText("Clicked clear all");
        myDb.deleteAll();
        populatelistview();
    }
    public void onClick_DisplayLists(View v){
        //displayText("Clicked display lists");
        //Cursor cursor = myDb.getAllRows();
        //displayRecordset(cursor);
        shareList();
    }
    //Database related. List[]
    @Override
    protected void onDestroy(){
        super.onDestroy();
        closeDB();
    }

    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    public void displayText(String message) {
        //TextView textView = (TextView) findViewById(R.id.textDisplay);
        //textView.setText(message);
    }
    //Display entire record set
    private void displayRecordset(Cursor cursor) {
        String message = "";

        if(cursor.moveToFirst()){
            //process the data
            do {
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
               // int studentNumber = cursor.getInt(DBAdapter.COL_STUDENTNUM);
                String dateAdded = cursor.getString(DBAdapter.COL_DATEADDED);

                //append data to message
                message +=name +" "+ id + ">"
                        +"  Date added: " + dateAdded
                        + "\n";
            }while(cursor.moveToNext());
        }
        displayText(message);
    }
    public void populatelistview(){
        Cursor cursor = myDb.getAllRows();
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME,DBAdapter.KEY_ROWID ,DBAdapter.KEY_DATEADDED};
        int[] viewIDs = new int[]{R.id.listname,R.id.rowID,R.id.listdate};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.listview_layout,
                                cursor,fieldnames,viewIDs,0);
        ListView mylist = (ListView) findViewById(R.id.listView);
        mylist.setAdapter(myCursorAdapter);


    }
    public void shareList(){
        ParseQuery<ParseUser> userquery = ParseUser.getQuery();
        ParseQuery<ParseObject> listquery = ParseQuery.getQuery("TestObject");

        userquery.getInBackground("IduDOmBDh4", new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser parseUser, com.parse.ParseException e) {
            if(e==null) {
                ParseQuery<ParseObject> listquery = ParseQuery.getQuery("TestObject");

                listquery.whereEqualTo("ListName", "ok");
                listquery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject ListObject, com.parse.ParseException e) {
                        if (ListObject == null) {
                            //failed
                        } else {
                            ParseRelation<ParseObject> relation = ListObject.getRelation("SharedBy");
                            relation.add(parseUser);
                            ListObject.saveInBackground();
                        }
                    }
                });
            }
            }
        });
    }
}
