package com.example.notesjc

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context?): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)
{
    override fun onCreate(db: SQLiteDatabase) {
        val query = """
            CREATE TABLE $TABLE_NAME (
                $ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, 
                $TITLE_COL TEXT, 
                $SUBJECT_COL TEXT, 
                $DESCRIPTION_COL TEXT
            )
        """
        db.execSQL(query)
    }
    fun addNotes(title: String?, subject: String?, description: String?) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(TITLE_COL, title)
            put(SUBJECT_COL, subject)
            put(DESCRIPTION_COL, description)

        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    fun readNotes(): ArrayList<CourseModal> {
        val db = this.readableDatabase
        val cursorCourses: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val courseModalArrayList: ArrayList<CourseModal> = ArrayList()

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(
                    CourseModal(
                        id = cursorCourses.getInt(cursorCourses.getColumnIndexOrThrow(ID_COL)),
                        title = cursorCourses.getString(cursorCourses.getColumnIndexOrThrow(TITLE_COL)),
                        subject = cursorCourses.getString(cursorCourses.getColumnIndexOrThrow(SUBJECT_COL)),
                        description = cursorCourses.getString(cursorCourses.getColumnIndexOrThrow(DESCRIPTION_COL)),

                    )
                )
            } while (cursorCourses.moveToNext())
        }
        cursorCourses.close()
        return courseModalArrayList
    }


    fun updateNote(originalTitle: String, title: String?, subject: String?, description: String?) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(TITLE_COL, title)
            put(SUBJECT_COL, subject)
            put(DESCRIPTION_COL, description)

        }
        db.update(TABLE_NAME, values, "$TITLE_COL=?", arrayOf(originalTitle))
        db.close()
    }

    fun deleteNote(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL=?", arrayOf(id.toString()))
        db.close()
    }

    companion object {
        private const val DB_NAME = "notesdb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "mynotes"
        private const val ID_COL = "id"
        private const val TITLE_COL = "title"
        private const val SUBJECT_COL = "subject"
        private const val DESCRIPTION_COL = "description"

    }



}//end of class




data class CourseModal(
    val id: Int,
    val title: String,
    val subject: String,
    val description: String
)