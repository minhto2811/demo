package com.mgok.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {
    public MyDatabase(Context context) {
        super(context, "tensv_sqlite", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS Contact_MaSV(ID INTEGER PRIMARY KEY, NAME TEXT NOT NULL, PHONE TEXT NOT NULL);";
        db.execSQL(sql);

        String data = "INSERT INTO Contact_MaSV(NAME,PHONE) VALUES\n" +
                "('NGUYEN VAN A','0321456987'),\n" +
                "('NGUYEN VAN B','0321456987'),\n" +
                "('NGUYEN VAN C','0321456987'),\n" +
                "('TEN SINH VIEN','0321456987'),\n" +
                "('NGUYEN VAN D','0321456987');";

        db.execSQL(data);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS Contact_MaSV;");
            onCreate(db);
        }
    }

    public ArrayList<Contact> getList() {
        ArrayList<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Contact_MaSV;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(0));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }


    public long insert(Contact contact) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", contact.getName());
        contentValues.put("PHONE", contact.getPhone());
        return sqLiteDatabase.insert("Contact_MaSV", null, contentValues);
    }


    public long update(Contact contact) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", contact.getName());
        contentValues.put("PHONE", contact.getPhone());
        return sqLiteDatabase.update("Contact_MaSV", contentValues, "ID=?", new String[]{String.valueOf(contact.getId())});
    }


    public long delete(int contactId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete("Contact_MaSV", "ID=?", new String[]{String.valueOf(contactId)});
    }


    public ArrayList<Contact> filter(String query) {
        ArrayList<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM Contact_MaSV WHERE NAME LIKE ?";
        Cursor cursor = db.rawQuery(sql, new String[]{"%"+query+"%"});
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(0));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contacts;
    }
}
