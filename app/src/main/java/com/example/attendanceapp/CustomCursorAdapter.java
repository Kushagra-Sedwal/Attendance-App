package com.example.attendanceapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {
    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.course_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView courseName = view.findViewById(R.id.course_name);
        TextView totalLectures = view.findViewById(R.id.course_total);
        TextView lecturesConducted = view.findViewById(R.id.lecture_conducted);
        TextView attendedLectures = view.findViewById(R.id.course_attended);

        courseName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        totalLectures.setText("Total Lectures: " + cursor.getInt(cursor.getColumnIndexOrThrow("total_lectures")));
        lecturesConducted.setText("Total Conducted: " + cursor.getInt(cursor.getColumnIndexOrThrow("lecture_conducted")));
        attendedLectures.setText("Total Attended: " + cursor.getInt(cursor.getColumnIndexOrThrow("attended_lectures")));
    }
}
