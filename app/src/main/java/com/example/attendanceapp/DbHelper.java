package com.example.attendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME_ = "db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_COURSES = "courses";

    // Table columns
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String TOTAL_LECTURES = "total_lectures";
    private static final String ATTENDED_LECTURES = "attended_lectures";
    private static final String MINIMUM_ATTENDANCE = "minimum_attendance";
    private static final String LECTURE_CONDUCTED = "lecture_conducted";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME_, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE " + TABLE_COURSES + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT,"
                + TOTAL_LECTURES + " INTEGER,"
                + ATTENDED_LECTURES + " INTEGER,"
                + LECTURE_CONDUCTED + " INTEGER,"
                + MINIMUM_ATTENDANCE + " REAL)";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (DATABASE_VERSION < 2) { // Check if upgrading from version 1
            sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_COURSES + " ADD COLUMN " + LECTURE_CONDUCTED + " INTEGER ");
        }
        if (DATABASE_VERSION < 3){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
            onCreate(sqLiteDatabase);
        }
    }

    public void addCourse(String name, int totalLectures, int attendedLectures,int  lecture_conducted ,double minimumAttendance) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(NAME, name);
            values.put(TOTAL_LECTURES, totalLectures);
            values.put(ATTENDED_LECTURES, attendedLectures);
            values.put(LECTURE_CONDUCTED, lecture_conducted);
            values.put(MINIMUM_ATTENDANCE, minimumAttendance);
            db.insert(TABLE_COURSES, null, values);
        } catch (Exception e) {
            Log.d("Database Error", "Error adding course: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    // Get all courses
    public Cursor getAllCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id AS _id , name, total_lectures, attended_lectures, lecture_conducted,minimum_attendance FROM courses", null);
    }

    // Update course attendance
    public void updateAttendance(int courseId, int totalLectures,int lecture_conducted, int attendedLectures) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TOTAL_LECTURES, totalLectures);
        values.put(LECTURE_CONDUCTED,lecture_conducted);
        values.put(ATTENDED_LECTURES, attendedLectures);


        db.update(TABLE_COURSES, values, ID + " = ?", new String[]{String.valueOf(courseId)});
        db.close();
    }

    // Get course by ID
    public Cursor getCourseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COURSES + " WHERE " + ID + "==" + id, null);
    }

    // Method to delete a course by ID
    public void deleteCourse(int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Use the delete method to remove the course based on its ID
        db.delete(TABLE_COURSES, ID + " = ?", new String[]{String.valueOf(courseId)});

        db.close();
    }

}
