package com.example.shopbroker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.TextView;


public class ListActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    DBAdapter myDb;

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
        
        //openDB();

    }
/*
    @Override
    protected void onDestroy(){
        super.onDestroy();
        closeDB();
    }
*/
    public void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        //switch fragments based on their position
        switch(position) {
            default:

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
/*
    private void openDB(){
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }
*/
    // called when button is pressed
    public void onClick_AddList(View v){
        displayText("Clicked add List");

//        long newId = myDb.insertRow("<List Name>",987,"<Date>");
        //Query for record added
      //  Cursor cursor = myDb.getRow(newId);
      //  displayRecordset(cursor);
        Intent intent = new Intent(this, CreateListActivity.class);
        startActivity(intent);

    }
    public void onClick_ClearAll(View v){
        displayText("Clicked clear all");
//        myDb.deleteAll();
    }
    public void onClick_DisplayLists(View v){
        displayText("Clicked display lists");
        //Cursor cursor = myDb.getAllRows();
        //displayRecordset(cursor);
    }
/*
    //Display entire record set
    private void displayRecordset(Cursor cursor) {
        String message = "";

        if(cursor.moveToFirst()){
            //process the data
            do {
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                int studentNumber = cursor.getInt(DBAdapter.COL_STUDENTNUM);
                String dateAdded = cursor.getString(DBAdapter.COL_DATEADDED);

                //append data to message
                message += "id=" + id
                        +" "+ name
                        +"  Date added: " + dateAdded
                        + "\n";
            }while(cursor.moveToNext());
        }
        displayText(message);
    }
*/
}
