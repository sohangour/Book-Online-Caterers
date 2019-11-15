package com.example.sohancaterers.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.sohancaterers.ModelClass.Order;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userorder.db";
    private static final String TABLE_NAME = "user_table";
    Context context;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(item_name TEXT primary key, price integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //add value to sqlite database
    public void addToCart(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_name", order.getItem_name());
        values.put("price", order.getPrice());
        db.insert(TABLE_NAME, null, values);
        db.close();

    }
    /* Retrive  data from database */

    public List<Order> getDataFromDB() {
        List<Order> modelList = new ArrayList<>();
        String query = "select * from " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Order model = new Order();
                model.setItem_name(cursor.getString(0));
                model.setPrice(cursor.getInt(1));
                modelList.add(model);
            } while (cursor.moveToNext());
        }
        return modelList;
    }

    public void delete(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "item_name" + " = ?", new String[]{name});
        db.close();
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        db.execSQL(query);
    }


}
