package com.example.shopbroker;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
* Created by Jacob on 3/1/2015.
*/
public class FriendsFragment extends Fragment implements View.OnClickListener {
    Button addFriend;
    EditText friendName;
    private DBAdapter dbhelper;
    //private SimpleCursorAdapter myCursorAdapter;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FriendsFragment newInstance(int sectionNumber) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendsFragment() {
    }

    //called to "inflate"/display layout file
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend, container, false);

        dbhelper = new DBAdapter(getActivity());
        dbhelper.open();
        populatefriendView(rootView);


        addFriend = (Button) rootView.findViewById(R.id.btnAddFriend);
        addFriend.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ListActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
    public void onClick(View rootView){
        friendName = (EditText) getView().findViewById(R.id.friendNameEditText);
        final String tempName = friendName.getText().toString();

        ParseQuery<ParseUser> userquery = ParseUser.getQuery();
        userquery.whereEqualTo("username", tempName);
        userquery.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(parseUser == null){
                    //fail
                    Toast.makeText(getActivity(), "not found", Toast.LENGTH_LONG).show();
                    //dbhelper.insertRow_to_Friends(tempName, "null");
                    //populatefriendView(getView());
                    friendName.setText("");
                }
                else{
                    String objectid = parseUser.getObjectId();
                    dbhelper.insertRow_to_Friends(tempName, objectid);
                    populatefriendView(getView());
                    friendName.setText("");
                    Toast.makeText(getActivity(), "found+added", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void populatefriendView(View rootview){
        Cursor cursor = dbhelper.getAllFriendRows();
        String[] fieldnames = new String[]{DBAdapter.KEY_NAME, DBAdapter.KEY_OBJID};
        int[] viewIDs = new int[]{R.id.friendName, R.id.objectID};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(rootview.getContext(), R.layout.friendsview_layout,
                cursor, fieldnames, viewIDs, 0);
        ListView mylist = (ListView) rootview.findViewById(R.id.friendsView);
        mylist.setAdapter(myCursorAdapter);

    }
}
