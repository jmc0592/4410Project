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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

//parse to JSON to sql DB
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
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ListActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
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
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME, DBAdapter.KEY_ROWID, DBAdapter.KEY_DATEADDED};
        int[] viewIDs = new int[]{R.id.listname, R.id.rowID, R.id.listdate};
        myCursorAdapter = new SimpleCursorAdapter(rootView.getContext(), R.layout.listview_layout,
                cursor, fieldnames, viewIDs, 0);
        ListView mylist = (ListView) rootView.findViewById(R.id.listView);
        mylist.setAdapter(myCursorAdapter);

        mylist.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //creates new activity when an item is clicked
                Intent intent = new Intent(getActivity(), CreateListActivity.class);
                intent.putExtra("ID",id);
                startActivity(intent);//added this back to it goes to activity
                updateItem(id);
                ShowCurrentUser();
               // displayToast(id);
               //toParse(id); //Adds the Row in the "list" table to Parse

            }


        });
        return rootView;
    }
    //not used...for now
    private void updateItem(long id) {
        Cursor cursor = dbhelper.getRow(id);
        if(cursor.moveToFirst()){
            long rowid = cursor.getLong(DBAdapter.COL_ROWID);
            String name = cursor.getString(DBAdapter.COL_NAME);
            String date = cursor.getString(DBAdapter.COL_DATEADDED);

            dbhelper.updateRow(rowid,name,date);

        }
        cursor.close();
        //displayToast(id);
    }
   // ParseObject testObject = new ParseObject("TestObject");
    //testObject.put("foo", "bar");
   // testObject.saveInBackground();
    private void toParse(long id) {
        ParseObject testObject = new ParseObject("TestObject");
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> relation = testObject.getRelation("SharedBy");
        //testObject.WhereEqualTo("rowID", urowID);
        Cursor cursor = dbhelper.getRow(id);
        if(cursor.moveToFirst()){
            long rowid = cursor.getLong(DBAdapter.COL_ROWID);
            String name = cursor.getString(DBAdapter.COL_NAME);
            String date = cursor.getString(DBAdapter.COL_DATEADDED);
            //ParseObject testObject = new ParseObject("TestObject");
            testObject.put("rowID", rowid);
            testObject.put("ListName", name);
            testObject.put("Date", date);
            relation.add(currentUser);
            testObject.saveInBackground();
        }
        cursor.close();
        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("rowID", rowid);
        // testObject.saveInBackground();

    }
    //not used...for now
    private void displayToast(long id){
        Cursor cursor = dbhelper.getRow(id);
        if(cursor.moveToFirst()){
            long rowid = cursor.getLong(DBAdapter.COL_ROWID);
            String name = cursor.getString(DBAdapter.COL_NAME);
            String date = cursor.getString(DBAdapter.COL_DATEADDED);

            String message = "ID:" + rowid +"\n"
                    + "Listname:" + name +"\n"
                    + "Date added:" + date;
            Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();

        }
        cursor.close();
    }
    public void ShowCurrentUser(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser!=null){
            String username = currentUser.getString("username");
            String objectId = currentUser.getObjectId();//currentUser.getString("objectId");
            String message = "Username: " + username +"\n"
                    + "objectId: " + objectId;
            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
        }
    }

}