package com.example.bottomnavigation.yeexiaoze.database.selfDevelopment

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

@Suppress("UNREACHABLE_CODE")
class ChapterSQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, "Chapter.db", null, 1)
{
    companion object {
        const val CHAPTER_TABLE = "chapter"
        private const val CHAPTER_ID = "chapterID"
        private const val TITLE = "title"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE " + CHAPTER_TABLE + "(" + CHAPTER_ID +
                " TEXT PRIMARY KEY, " + TITLE + " TEXT" + ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $CHAPTER_TABLE")
    }

    @SuppressLint("Range", "Recycle")
    fun getAttribute(COLUMN_NAME: String): ArrayList<String>
    {
        val attributeList: ArrayList<String> = ArrayList()
        val query = "SELECT $COLUMN_NAME FROM $CHAPTER_TABLE"
        val db = this.readableDatabase

        val cursor: Cursor?

        try
        {
            cursor = db.rawQuery(query, null)
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            db.execSQL(query)

            return ArrayList()
        }

        var attribute: String

        if (cursor.moveToFirst())
        {
            do
            {
                attribute = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))

                attributeList.add(attribute)
            }while (cursor.moveToNext())
        }

        return attributeList
    }

    @SuppressLint("Range", "Recycle")
    fun conditionalGetAttribute(COLUMN_NAME: String, CONDITIONAL_COLUMN: String, CONDITIONAL_STRING: String): String
    {
        var attribute = ""
        val query = "SELECT $COLUMN_NAME FROM $CHAPTER_TABLE WHERE $CONDITIONAL_COLUMN = '$CONDITIONAL_STRING'"
        val db = this.readableDatabase

        val cursor: Cursor?

        try
        {
            cursor = db.rawQuery(query, null)
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            db.execSQL(query)

            return ""
        }

        if (cursor.moveToFirst())
        {
            do
            {
                attribute = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                break
            }while (cursor.moveToNext())
        }

        return attribute
    }
}