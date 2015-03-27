package com.example.shopbroker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ListsFragment extends Fragment {
    /**
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private DBAdapter dbhelper;
    private SimpleCursorAdapter myCursorAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";
    Context ctx = getActivity();
    ListActivity listAc = new ListActivity();
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ListsFragment newInstance(int sectionNumber) {
        ListsFragment fragment = new ListsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ListsFragment() {
    }

    //called to "inflate"/display layout file
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        //Populates Listview on creation of the fragment
        dbhelper = new DBAdapter(getActivity());
        dbhelper.open();
        Cursor cursor = dbhelper.getAllRows();
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME,DBAdapter.KEY_ROWID ,DBAdapter.KEY_DATEADDED};
        int[] viewIDs = new int[]{R.id.listname,R.id.rowID,R.id.listdate};
        myCursorAdapter = new SimpleCursorAdapter(rootView.getContext(),R.layout.listview_layout,
                cursor,fieldnames,viewIDs,0);
        ListView mylist = (ListView) rootView.findViewById(R.id.listView);
        mylist.setAdapter(myCursorAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ListActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
/*
 *in main for now
    public void openDB(){
        myDb = new DBAdapter(ctx);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }


    // called when button is pressed
    public void onClick_AddList(View v){
        //listAc.displayText("Clicked add List");

        //long newId = myDb.insertRow("<List Name>",987,"<Date>");
        //Query for record added
        //Cursor cursor = myDb.getRow(newId);
        //displayRecordset(cursor);
    }
    public void onClick_ClearAll(View v){
        listAc.displayText("Clicked clear all");
        myDb.deleteAll();
    }
    public void onClick_DisplayLists(View v){
        listAc.displayText("Clicked display lists");
        Cursor cursor = myDb.getAllRows();
        displayRecordset(cursor);
    }

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
        listAc.displayText(message);
    }
*/

