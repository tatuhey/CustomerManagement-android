package com.example.customermanagement

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    var context: Context
    init{
        this.context = context
    }

    companion object {
        private val DB_NAME = "smtbiz"
        private val DB_VERSION = 1
        val TABLE_NAME = "customer"
        val ID = "id"
        val NAME = "name"
        val EMAIL = "email"
        val MOBILE = "mobile"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID INTEGER PRIMARY KEY," +
                        "$NAME TEXT," +
                        "$EMAIL TEXT," +
                        "$MOBILE TEXT" + ")"
                )
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addCustomer(id: Int, name: String, email: String, mobile: String) {
        val db = this.writableDatabase
        val values = ContentValues()

        db.beginTransaction()
        try {
            values.put(ID, id)
            values.put(NAME, name)
            values.put(EMAIL, email)
            values.put(MOBILE, mobile)

            val db = this.writableDatabase

            db.insert(TABLE_NAME, null, values)
            db.setTransactionSuccessful()
        }
        finally {
            db.endTransaction()
        }
        db.close()
    }

    fun getAllCustomers(): ArrayList<User>{
        val db  = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

        val namelist = ArrayList<User>()
        if (cursor.moveToFirst()) {
            do {
                namelist.add(
                    User(
                        cursor.getInt(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MOBILE))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return namelist
    }

    fun deleteCustomer(id: String): Int {
        val db = this.writableDatabase
        val rows: Int
        db.beginTransaction()
        try {
            rows = db.delete(TABLE_NAME, "$ID=?", arrayOf(id))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
        return rows
    }

//    fun searchUserByName(name: String): User? {
//        val db = this.readableDatabase
//        var user: User? = null
//        var cursor: Cursor? = null
//        try {
//            cursor = db.query(
//                TABLE_NAME,
//                arrayOf(ID, NAME, EMAIL, MOBILE),
//                "$NAME=?",
//                arrayOf(name),
//                null,
//                null,
//                null
//            )
//            if (cursor != null && cursor.moveToFirst()) {
//                val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
//                val userName = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
//                val email = cursor.getString(cursor.getColumnIndexOrThrow(EMAIL))
//                val mobile = cursor.getString(cursor.getColumnIndexOrThrow(MOBILE))
//                user = User(id, userName, email, mobile)
//            }
//        } finally {
//            cursor?.close()
//            db.close()
//        }
//        return user
//    }
    fun searchByName(name: String): Cursor? {
        val db = this.readableDatabase

        val cursor: Cursor
        db.beginTransaction()
        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $NAME =?", arrayOf(name))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        return cursor
    }

    fun updateCustomer(name: String, email: String, mobile: String): Int {

        val db = this.writableDatabase
        val values = ContentValues()
        val rows: Int
        db.beginTransaction()
        try {
            values.put(EMAIL, email)
            values.put(MOBILE, mobile)
            rows = db.update(TABLE_NAME, values, "name=?", arrayOf(name))
            db.setTransactionSuccessful()
        }
        finally {
            db.endTransaction()
            db.close()
        }
        return rows

    }

    fun recreateDatabaseAndTables() {
    }
}