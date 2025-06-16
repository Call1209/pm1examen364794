package com.example.pm1examen364794;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLConexion extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CONTACTS = "contacts";

    public SQLConexion(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pais TEXT, " +
                "nombre TEXT, " +
                "telefono TEXT, " +
                "nota TEXT, " +
                "imagen TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public boolean insertarContacto(String pais, String nombre, String telefono, String nota, String imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pais", pais);
        values.put("nombre", nombre);
        values.put("telefono", telefono);
        values.put("nota", nota);
        values.put("imagen", imagen); // Puedes pasar null si aún no manejas imágenes

        long resultado = db.insert("contacts", null, values);
        return resultado != -1;
    }

}
