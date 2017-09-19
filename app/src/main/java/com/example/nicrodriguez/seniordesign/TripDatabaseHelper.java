package com.example.nicrodriguez.seniordesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by nicrodriguez on 8/8/17.
 */

public class TripDatabaseHelper extends SQLiteOpenHelper{

    /*Database Name*/
    public static final String DATABASE_NAME = "trip_list_with_users_v2.db";

    /*Table Name*/
    public static String TABLE_NAME;
    public static final String TABLE_NAME2 = "user_table";


    /*Column Names*/
    public static final String COL_1 = "ID";
    public static final String COL_2 = "trip_name";
    public static final String COL_3 = "trip_time";
    public static final String COL_4 = "trip_path_lat";
    public static final String COL_5 = "trip_path_lon";
    public static final String COL_6 = "averageSpeed";
    public static final String COL_7 = "maxSpeed";
    public static final String COL_8 = "speedPoints";
    public static final String COL_9 = "travelDistance";
    public static final String COL_10 = "timeOfMeasurements";
    public static final String COL_11 = "date";


    public static final String COL_1_2 = "ID";
    public static final String COL_2_2 = "user_name";
    public static final String COL_3_2 = "password";


    public TripDatabaseHelper(Context context) {
        /*(context,Database Name, Factory, Version)*/
        super(context, DATABASE_NAME, null, 2);

        /*create database and table (Just for checking)*/
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          /* create a table, add table name, then add the column names and their implementations ("NAME (INT,TEXT, etc...),NAME (INT,TEXT,etc...)) */
//        db.execSQL("create table " + TABLE_NAME +" ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" TEXT,"
//                +COL_3+" TEXT,"+COL_4+" TEXT,"
//                +COL_5+" TEXT,"+COL_6+" TEXT,"
//                +COL_7+" TEXT,"+COL_8+" TEXT,"
//                +COL_9+" TEXT,"+COL_10+" TEXT)");

                  /* create a table, add table name, then add the column names and their implementations ("NAME (INT,TEXT, etc...),NAME (INT,TEXT,etc...)) */
        db.execSQL("create table " + TABLE_NAME2 +" ("+COL_1_2+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2_2+" TEXT,"
                +COL_3_2+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
        onCreate(db);
    }

    public boolean insertData(String name, String tripTime, String pointsLat, String pointsLon
            ,String averageSpeed, String maxSpeed, String speedPoints, String travelDistance
            , String timeMeasurement,String date){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        /*insert values into appropriate columns*/
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,tripTime);
        contentValues.put(COL_4,pointsLat);
        contentValues.put(COL_5,pointsLon);
        contentValues.put(COL_6,averageSpeed);
        contentValues.put(COL_7,maxSpeed);
        contentValues.put(COL_8,speedPoints);
        contentValues.put(COL_9,travelDistance);
        contentValues.put(COL_10,timeMeasurement);
        contentValues.put(COL_11,date);

        long result = db.insert(TABLE_NAME,null,contentValues);
        return result != -1;
    }

    public boolean insertUserData(String name, String tripTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        /*insert values into appropriate columns*/
        contentValues.put(COL_2_2,name);
        contentValues.put(COL_3_2,tripTime);

        long result = db.insert(TABLE_NAME2,null,contentValues);
        return result != -1;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        /*Creating a curser to query database,   '*' means 'all'   */
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);

        return res;
    }

    public Cursor getAllUserData(){
        SQLiteDatabase db = this.getWritableDatabase();
        /*Creating a curser to query database,   '*' means 'all'   */
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2,null);

        return res;
    }

    public int updateData(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        /*insert values into appropriate columns*/
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
       Integer up =  db.update(TABLE_NAME,contentValues,"id = ?",new String[] {id});
        return up;

    }

    public int updateUserData(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        /*insert values into appropriate columns*/
        contentValues.put(COL_1_2,id);
        contentValues.put(COL_2_2,name);
        Integer up =  db.update(TABLE_NAME2,contentValues,"id = ?",new String[] {id});
        return up;

    }

    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        /*"ID = ?",new String[] {id}, Basically says to replace ? with the intended row id*/
        Integer del = db.delete(TABLE_NAME,"ID = ?",new String[] {id});

        return del;

    }

    public Integer deleteUserData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        /*"ID = ?",new String[] {id}, Basically says to replace ? with the intended row id*/
        Integer del = db.delete(TABLE_NAME2,"ID = ?",new String[] {id});

        return del;

    }
    public void createUserTable(String user) {
        final SQLiteDatabase db = getWritableDatabase();
        String CREATE_TABLE_NEW_USER = "CREATE TABLE " + user + " ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" TEXT,"
                +COL_3+" TEXT,"+COL_4+" TEXT,"
                +COL_5+" TEXT,"+COL_6+" TEXT,"
                +COL_7+" TEXT,"+COL_8+" TEXT,"
                +COL_9+" TEXT,"+COL_10+" TEXT,"
                +COL_11+" TEXT)";
        db.execSQL(CREATE_TABLE_NEW_USER);
        db.close();
    }

    public void deleteUserTable(String TABLE_NAME){
        final SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.close();
    }





}