/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Database adapter class
 * 
 * @author silvian
 */

package android.pack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DemoDBAdapter{

    public static final String PROVIDER = "provider_name"; 
    public static final String IDENTIFIER = "row_identifier";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ALTITUDE = "altitude";
    public static final String SPEED = "speed";
    public static final String BEARING = "bearing";
    public static final String ACCURACY = "accuracy";

    private static final String TAG = "LocationDemoDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table location (provider_name text not null, "+
        						"row_identifier long primary key, "+
        						"latitude double not null, " +
        						"longitude double not null, " +
        						"altitude double not null, " +
        						"speed float not null, " +
        						"bearing float not null, " +
        						"accuracy float not null)";

    private static final String DATABASE_NAME = "LocationDemo";
    private static final String DATABASE_TABLE = "LocationData";
    private static final int DATABASE_VERSION = 2;

  private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DemoDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialisation call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DemoDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new data using the title and body provided. If the data is
     * successfully created return the new rowId for that data, otherwise return
     * a -1 to indicate failure.
     */
    public long createLocationData(String provider, 
    							   int row_identifier, 
    							   double latitude, 
    							   double longitude, 
    							   double altitude, 
    							   float speed, 
    							   float bearing, 
    							   float accuracy) {
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(PROVIDER, provider);
        initialValues.put(IDENTIFIER, row_identifier);
        initialValues.put(LATITUDE, latitude);
        initialValues.put(LONGITUDE, longitude);
        initialValues.put(ALTITUDE, altitude);
        initialValues.put(SPEED, speed);
        initialValues.put(BEARING, bearing);
        initialValues.put(ACCURACY, accuracy);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
        
    }

    /**
     * Delete the data with the given rowId
     * 
     * @param rowId id of data to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteDataPoint(int row_identifier) {

        return mDb.delete(DATABASE_TABLE, IDENTIFIER + "=" + row_identifier, null) > 0;
    }
    
    

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all data
     */
    public Cursor fetchAllLocationData() {

        return mDb.query(DATABASE_TABLE, new String[] {PROVIDER, 
        											   IDENTIFIER, 
        											   LATITUDE, 
        											   LONGITUDE, 
        											   ALTITUDE, 
        											   SPEED, 
        											   BEARING,
        											   ACCURACY}, null, null, null, null, null);
    }
    
    
    public Cursor fetchDataPoint2(int row_identifier) {

        return mDb.rawQuery("SELECT "+row_identifier+" as row_identifier, from "+DATABASE_NAME, new String[] {});
    }

    /**
     * Return a Cursor positioned at the data that matches the given rowId
     * 
     */
    public Cursor fetchDataPoint(int row_identifier) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {PROVIDER, 
            											  IDENTIFIER,
            											  LATITUDE,
            											  LONGITUDE,
            											  ALTITUDE,
            											  SPEED,
            											  BEARING,
            											  ACCURACY}, IDENTIFIER + "=" + row_identifier, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the data using the details provided. The note to be updated is
     * specified using the key_time, and it is altered to use the title and body
     * values passed in
     * 
     */
    public boolean updateDataPoint(String provider, 
    							   int row_identifier, 
    							   double latitude, 
    							   double longitude,
    							   double altitude, 
    							   float speed, 
    							   float bearing, 
    							   float accuracy) {
    	
        ContentValues args = new ContentValues();
        args.put(PROVIDER, provider);
        args.put(IDENTIFIER, row_identifier);
        args.put(LATITUDE, latitude);
        args.put(LONGITUDE, longitude);
        args.put(ALTITUDE, altitude);
        args.put(SPEED, speed);
        args.put(BEARING, bearing);
        args.put(ACCURACY, accuracy);

        return mDb.update(DATABASE_TABLE, args, IDENTIFIER + "=" + row_identifier, null) > 0;
    }
}
