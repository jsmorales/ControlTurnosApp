package com.example.johanmorales.controlturnossai.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.johanmorales.controlturnossai.Models.Ubication;
import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {

    //properties
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ubicationsDB.db";

    public static final String TABLE_NAME = "Ubications";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_AIRPORT = "airport";
    private static final String TAG = MyDBHandler.class.getSimpleName();

    //constructor - initialize the database
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Id INTEGER PRIMARY KEY AUTOINCREMENT
        String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT)", TABLE_NAME, COLUMN_ID, COLUMN_POSITION, COLUMN_AIRPORT);
        //execute creation table
        try {
            db.execSQL(CREATE_TABLE);
        }catch (android.database.SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //get all UBICATIONS on JSONArray
    public List<Ubication> loadUbicationList() {

        List<Ubication> resultList = new ArrayList<>();

        String query = "select * from "+TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        while(cursor.moveToNext()){

            Ubication ubRes =  new Ubication();

            ubRes.setId(String.valueOf(cursor.getInt(0)));
            ubRes.setPosition(String.valueOf(cursor.getString(1)));
            ubRes.setAirport(String.valueOf(cursor.getString(2)));

            resultList.add(ubRes);

        }

        return resultList;
    }

    //add a note
    public void addHandler(Ubication ubication) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, ubication.getId());
        values.put(COLUMN_POSITION, ubication.getPosition());
        values.put(COLUMN_AIRPORT, ubication.getAirport());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public Ubication findHandler(String idUbication) {

        String query = "select * from "+TABLE_NAME+" where "+COLUMN_ID+" = "+"'"+idUbication+"'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Ubication ubication = new Ubication();

        if(cursor.moveToFirst()){

            cursor.moveToFirst();

            ubication.setId(String.valueOf(cursor.getInt(0)));
            ubication.setPosition(cursor.getString(1));
            ubication.setAirport(cursor.getString(2));

            cursor.close();
        }else{
            ubication = null;
        }

        db.close();

        return ubication;
    }


    public boolean deleteHandler(int ID) {

        Boolean result = false;

        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = '" + String.valueOf(ID) + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Ubication ubication = new Ubication();

        if (cursor.moveToFirst()) {

            ubication.setId(String.valueOf(cursor.getInt(0)));

            db.delete(TABLE_NAME, COLUMN_ID + "=?",
                    new String[] {
                            String.valueOf(ubication.getId())
                    });

            cursor.close();

            result = true;
        }

        db.close();

        return result;
    }

    /**/
    public boolean updateHandler(String Id, String position, String airport) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();

        args.put(COLUMN_ID, Id);
        args.put(COLUMN_POSITION, position);
        args.put(COLUMN_AIRPORT, airport);

        return db.update(TABLE_NAME, args, COLUMN_ID + "=" + Id, null) > 0;

    }
}
