package com.example.findmycarv2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "findMyCar.db";
    public static final String TABLE_NAME = "history_locations";

    public static final String COL_ID = "id INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String COL_LAT = "lat TEXT";
    public static final String COL_LONG = "long TEXT";
    public static final String COL_STREET = "street TEXT";
    public static final String COL_ZIPCODE = "zipcode TEXT";
    public static final String COL_DATETIME = "datetime TEXT";
    public static final String COL_PHOTOS_PATH = "photos_path TEXT";



    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "("+ COL_ID +","+ COL_LAT+","+ COL_LONG+","+ COL_STREET+","+ COL_ZIPCODE+","+ COL_DATETIME +","+ COL_PHOTOS_PATH +")";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String lat, String lon, String zipcode, String street, String dateTime, String imagesPath){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("datetime", dateTime);
        contentValues.put("lat", lat);
        contentValues.put("long", lon);
        contentValues.put("street", street);
        contentValues.put("zipcode", zipcode);
        contentValues.put("photos_path", imagesPath);

        if(db.insert(TABLE_NAME, null, contentValues ) < 1){
            return false;
        }else{
            return true;
        }
    };

    public CarLocation[] retrieveData(){

        CarLocation[] carLocations;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);

        carLocations = new CarLocation[cursor.getCount()];

        if (cursor.moveToFirst()) {
            do {
                carLocations[cursor.getPosition()] = new CarLocation();

                carLocations[cursor.getPosition()].setDateTime(cursor.getString(5));
                carLocations[cursor.getPosition()].setImagePath(cursor.getString(6));
                carLocations[cursor.getPosition()].setLat(cursor.getString(1));
                carLocations[cursor.getPosition()].setLon(cursor.getString(2));
                carLocations[cursor.getPosition()].setStreet(cursor.getString(3));
                carLocations[cursor.getPosition()].setZipcode(cursor.getString(4));


            } while (cursor.moveToNext());
        }

        return carLocations;

    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }
    public void insertDummyData(){

        insertData("51.722536", "5.358161", "1234AJ", "Portela","Fri Jan 10 19:49:29 GMT+00:00 2020", "a");
//        insertData("125", "4600", "1234AB", "Terpenborch 12","Fri Jan 10 19:49:29 GMT+00:00 2020", "a");
//        insertData("130", "350", "1234AC", "Terpenborch 1","Fri Jan 10 19:49:29 GMT+00:00 2020", "b");
//        insertData("400", "275", "1234AD", "Afrikaandersstraat 12","Fri Jan 10 19:49:29 GMT+00:00 2020", "c");
//        insertData("600", "45600", "1234AE", "Afrikaandersstraat 34","Fri Jan 10 19:49:29 GMT+00:00 2020", "d");
//        insertData("345", "800", "1234AF", "Terpenborch 23","Fri Jan 10 19:49:29 GMT+00:00 2020", "e");
//        insertData("600", "350", "1234AG", "Kruisherenborch 16","Fri Jan 10 19:49:29 GMT+00:00 2020", "f");
//        insertData("300", "375", "1234AH", "Terpenborch 8","Fri Jan 10 19:49:29 GMT+00:00 2020", "g");
//        insertData("1234", "870", "1234AI", "Dommelborg 6","Fri Jan 10 19:49:29 GMT+00:00 2020", "h");
//        insertData("5000", "560", "1234AK", "Dommelborg 9","Fri Jan 10 19:49:29 GMT+00:00 2020", "i");

        }



}

