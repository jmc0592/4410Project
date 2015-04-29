// ------------------------------------ DBADapter.java ---------------------------------------------

package com.example.shopbroker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

public class DBAdapter {

	/////////////////////////////////////////////////////////////////////
	//	Constants & Data
	/////////////////////////////////////////////////////////////////////
	// For logging:
	private static final String TAG = "DBAdapter";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;
	/*
	 * CHANGE 1:
	 */
	// TODO: Setup your fields here:
	public static final String KEY_NAME = "name";
	public static final String KEY_DATEADDED = "dateAdded";
    public static final String KEY_ITEM_LISTID = "itemListId";
    public static final String KEY_PRICE = "price";
    public static final String KEY_OBJID = "objectID";

    public static final String KEY_SHARED = "shared";
	
	// TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
	public static final int COL_NAME = 1;
	public static final int COL_DATEADDED = 2;
    public static final int COL_ITEM_LISTID = 2;
    public static final int COL_OBJID = 2;

	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_DATEADDED};
    public static final String[] FRIENDS = new String[] {KEY_ROWID, KEY_NAME, KEY_OBJID};
    public static final String[] ITEMS = new String[] {KEY_ROWID, KEY_NAME, KEY_ITEM_LISTID, KEY_PRICE, KEY_SHARED};
	
	// DB info: it's name, and the table we are using (just one).
	public static final String DATABASE_NAME = "MyDb";
	public static final String DATABASE_TABLE = "mainTable";
    public static final String DATABASE_ITEMS = "grocery_items";
    public static final String FRIENDS_TABLE = "friend_Table";
	// Track DB version if a new version of your app changes the format.
	public static final int DATABASE_VERSION = 28;
	
	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			
			/*
			 * CHANGE 2:
			 */
			// TODO: Place your fields here!
			// + KEY_{...} + " {type} not null"
			//	- Key is the column name you created above.
			//	- {type} is one of: text, integer, real, blob
			//		(http://www.sqlite.org/datatype3.html)
			//  - "not null" means it is a required field (must be given a value).
			// NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
			+ KEY_NAME + " text not null, "
			+ KEY_DATEADDED + " string not null"
			
			// Rest  of creation:
			+ ");";

    private static final String CREATE_TABLE_ITEMS =
            "create table " + DATABASE_ITEMS
            +" (" + KEY_ROWID + " integer primary key, "
            + KEY_NAME + " text not null, "
            + KEY_ITEM_LISTID + " text not null, "
            + KEY_PRICE + " text not null, "
            + KEY_SHARED + " text not null "
            + ");";
    private static final String CREATE_TABLE_FRIENDS =
            "create table " + FRIENDS_TABLE
            +" (" + KEY_ROWID + " interger primary key, "
            + KEY_NAME + " text not null, "
            + KEY_OBJID + " text not null"
            + ");";
	// Context of application who uses us.
	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;

	/////////////////////////////////////////////////////////////////////
	//	Public methods:
	/////////////////////////////////////////////////////////////////////
	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Open the database connection.
	public DBAdapter open(){
        //myDBHelper = new DatabaseHelper(context);
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Close the database connection.
	public void close() {
		myDBHelper.close();
	}
	
	// Add a new set of values to the database.
	public long insertRow(String name, String dateAdded) {
		// Create row's data:
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_DATEADDED, dateAdded);
		
		// Insert it into the database.
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

    public long insertRow_to_Items(String name, String item_listid, String price, String shared){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME,name);
        initialValues.put(KEY_ITEM_LISTID, item_listid);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_SHARED, shared);

        return db.insert(DATABASE_ITEMS, null, initialValues);
    }
    public long insertRow_to_Friends(String name, String objID){
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_OBJID, objID);

        return db.insert(FRIENDS_TABLE, null, initialValues);
    }
	
	// Delete a row from the database, by rowId (primary key)
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	
	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Return all data in the database.
	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
    public Cursor getAllItemRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_ITEMS, ITEMS,
                where, null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllMineRows(long rowID) {
        String sRowID = Long.toString(rowID);
        String shared = "0";
        String where = KEY_SHARED + "=" + shared + " AND " + KEY_ITEM_LISTID + "=" + sRowID;
        Cursor c = 	db.query(true, DATABASE_ITEMS, ITEMS,
                where, null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllSharedRows(long rowID) {
        String sRowID = Long.toString(rowID);
        String shared = "1";
        String where = KEY_SHARED + "=" + shared + " AND " + KEY_ITEM_LISTID + "=" + sRowID;
        Cursor c = 	db.query(true, DATABASE_ITEMS, ITEMS,
                where, null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllFriendRows(){
        String where = null;
        Cursor c = db.query(true, FRIENDS_TABLE, FRIENDS,
                where, null, null, null, null, null);
        if (c != null){
            c.moveToFirst();
        }
        return c;
    }

	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

    public Cursor getItemRow(long rowId){
        String where = KEY_ITEM_LISTID + "=" + rowId;
        Cursor c = db.query(true, DATABASE_ITEMS, ITEMS,
                           where, null, null, null, null, null, null );
        if (c != null) {
            c.moveToFirst();
            c.moveToNext();
        }
        return c;
    }

    public Cursor getItemRowMine(long rowId){
        String shared = "0";
        String where = KEY_ITEM_LISTID + "=" + rowId + " AND " + KEY_SHARED + "=" + shared;
        Cursor c = db.query(true, DATABASE_ITEMS, ITEMS,
                where, null, null, null, null, null, null );
        if (c != null) {
            c.moveToFirst();
            c.moveToNext();
        }
        return c;
    }

    public Cursor getItemRowShared(long rowId){
        String shared = "1";
        String where = KEY_ITEM_LISTID + "=" + rowId + " AND " + KEY_SHARED + "=" + shared;
        Cursor c = db.query(true, DATABASE_ITEMS, ITEMS,
                where, null, null, null, null, null, null );
        if (c != null) {
            c.moveToFirst();
            c.moveToNext();
        }
        return c;
    }

	
	// Change an existing row to be equal to new data.
	public boolean updateRow(long rowId, String name, String dateAdded) {
		String where = KEY_ROWID + "=" + rowId;

		// Create row's data:
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_NAME, name);
		newValues.put(KEY_DATEADDED, dateAdded);
		
		// Insert it into the database.
		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}

    //update price in database
    public boolean updateItemPrice(long rowId, String price){
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_PRICE, price);

        //insert into database
        return db.update(DATABASE_ITEMS, newValues, where, null) != 0;
    }
	
	/////////////////////////////////////////////////////////////////////
	//	Private Helper Classes:
	/////////////////////////////////////////////////////////////////////
	
	/**
	 * Private class which handles database creation and upgrading.
	 * Used to handle low-level database access.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_SQL);
            db.execSQL(CREATE_TABLE_ITEMS);
            db.execSQL(CREATE_TABLE_FRIENDS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + FRIENDS_TABLE);
			
			// Recreate new database:
			onCreate(db);
		}
	}
}
