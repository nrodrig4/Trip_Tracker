package com.example.nicrodriguez.seniordesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nicrodriguez on 8/15/17.
 */

public class UserDatabaseHelper extends SQLiteOpenHelper {
    /*Database Name*/
    public static final String DATABASE_NAME = "trip_list_with_metadata4.db";

    /*Table Name*/
    public static final String TABLE_NAME = "trip_table";
    public static final String TABLE_NAME2 = "user_table";

    /*Column Names*/
    public static final String COL_1 = "ID";
    public static final String COL_2 = "user_name";
    public static final String COL_3 = "password";

    public UserDatabaseHelper(Context context) {
        /*(context,Database Name, Factory, Version)*/
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          /* create a table, add table name, then add the column names and their implementations ("NAME (INT,TEXT, etc...),NAME (INT,TEXT,etc...)) */
        db.execSQL("create table " + TABLE_NAME +" ("+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_2+" TEXT,"
                +COL_3+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String tripTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        /*insert values into appropriate columns*/
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,tripTime);

        long result = db.insert(TABLE_NAME,null,contentValues);
        return result != -1;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        /*Creating a curser to query database,   '*' means 'all'   */
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);

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

    public Integer deleteData (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        /*"ID = ?",new String[] {id}, Basically says to replace ? with the intended row id*/
        Integer del = db.delete(TABLE_NAME,"ID = ?",new String[] {id});

        return del;

    }
}
