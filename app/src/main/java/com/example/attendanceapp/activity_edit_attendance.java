package com.example.attendanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_edit_attendance extends AppCompatActivity {
    TextView tv1;
    Button btn1, btn2,btn3;
    DbHelper dbHelper;
    protected int courseId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_attendance);

        dbHelper = new DbHelper(getApplicationContext());
        Intent intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);

        tv1 = findViewById(R.id.display_record);
        btn1 = findViewById(R.id.present);
        btn2 = findViewById(R.id.absent);
        btn3 = findViewById(R.id.goback);

        loadcoursedetails();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upateattendace(true);


            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upateattendace(false);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void upateattendace(boolean b) {
        Cursor cursor = dbHelper.getCourseById(courseId);
        if (cursor.moveToFirst()) {
            int totalLectures = cursor.getInt(cursor.getColumnIndexOrThrow("total_lectures"));
            int attendedLectures = cursor.getInt(cursor.getColumnIndexOrThrow("attended_lectures"));
            int lectureConducted = cursor.getInt(cursor.getColumnIndexOrThrow("lecture_conducted"));
            if (lectureConducted < totalLectures) {
                if (b) attendedLectures+=1;
                else attendedLectures-=1;
                dbHelper.updateAttendance(courseId, totalLectures,lectureConducted, attendedLectures);
                Toast.makeText(activity_edit_attendance.this, "Attendance updated successfully!", Toast.LENGTH_SHORT).show();
                loadcoursedetails();
            }
            else {
                Toast.makeText(this, "Your Session has ended", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }


    private void loadcoursedetails() {
        Cursor cursor = dbHelper.getCourseById(courseId);
        if (cursor.moveToFirst()) {
            String courseName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int totalLectures = cursor.getInt(cursor.getColumnIndexOrThrow("total_lectures"));
            int attendedLectures = cursor.getInt(cursor.getColumnIndexOrThrow("attended_lectures"));
            int lectureConducted = cursor.getInt(cursor.getColumnIndexOrThrow("lecture_conducted"));

            String tv = "COURSE NAME : "+courseName + "\n" + "Total Lectures: " + totalLectures + "\n" + "Attended Lectures: " + attendedLectures+ "\n" + "Lecture Conducted: " + lectureConducted;
            tv1.setText(tv);
        }
    }
}