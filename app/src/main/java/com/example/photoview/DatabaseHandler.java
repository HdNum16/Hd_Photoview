package com.example.photoview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "FavoriteImages";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "FavoriteImages";

    private static final String KEY_TAG = "tags";
    private static final String KEY_WEB_URL = "webformatURL";
    private static final String KEY_LARGE_URL = "largeImageURL";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_students_table = String.format("CREATE TABLE %s(%s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_TAG, KEY_WEB_URL, KEY_LARGE_URL);
        db.execSQL(create_students_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_students_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_students_table);

        onCreate(db);
    }

    public void addImage(imageInfo image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG, image.getTags());
        values.put(KEY_WEB_URL, image.getWebformatURL());
        values.put(KEY_LARGE_URL, image.getLargeImageURL());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<imageInfo> getAllImages() {
        List<imageInfo> images = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            imageInfo image = new imageInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            images.add(0, image);
            cursor.moveToNext();
        }
        cursor.close();
        return images;
    }

    public void deleteImage(String webformatURL) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_WEB_URL + " = ?", new String[] { webformatURL });
        db.close();
    }

    public boolean CheckIfDataExist(String webformatURL) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_WEB_URL + " = \"" + webformatURL + "\";";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}