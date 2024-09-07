package com.example.find_a_doctor

import DoctorDTO
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FavoriteDoctorDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "favourite_doctors.db"
        private const val TABLE_FAVORITES = "favorite_doctors"
        private const val COLUMN_DOCTOR_ID = "doctor_id"
        private const val COLUMN_DOCTOR_DATA = "doctor_data"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_FAVORITES ($COLUMN_DOCTOR_ID INTEGER PRIMARY KEY, $COLUMN_DOCTOR_DATA TEXT)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_FAVORITES"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    private val gson = Gson()

    fun addFavoriteDoctor(doctor: DoctorDTO) {
        val db = writableDatabase
        val doctorJson = gson.toJson(doctor)
        db.execSQL(
            "INSERT OR REPLACE INTO $TABLE_FAVORITES ($COLUMN_DOCTOR_ID, $COLUMN_DOCTOR_DATA) VALUES (?, ?)",
            arrayOf(doctor.id, doctorJson)
        )
    }

    fun removeFavoriteDoctor(doctorId: Int) {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_FAVORITES WHERE $COLUMN_DOCTOR_ID = ?", arrayOf(doctorId))

    }

    fun getAllFavoriteDoctors(): List<DoctorDTO> {
        val doctors = mutableListOf<DoctorDTO>()
        val cursor = readableDatabase.rawQuery("SELECT $COLUMN_DOCTOR_DATA FROM $TABLE_FAVORITES", null)
        with(cursor) {
            while (moveToNext()) {
                val doctorJson = getString(getColumnIndexOrThrow(COLUMN_DOCTOR_DATA))
                val doctorType: Type = object : TypeToken<DoctorDTO>() {}.type
                val doctor = gson.fromJson<DoctorDTO>(doctorJson, doctorType)
                doctors.add(doctor)
            }
            close()
        }
        return doctors
    }
}
