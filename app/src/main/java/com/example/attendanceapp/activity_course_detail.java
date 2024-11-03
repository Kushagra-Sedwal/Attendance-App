package com.example.attendanceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class activity_course_detail extends AppCompatActivity {
    private TextView courseNameView, totalLecturesView, attendedLecturesView,attendanceSummary,predictionMessage;
    private Button markAttendedButton, markMissedButton ;
    protected int courseId;
    private DbHelper dbHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    // Handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selecetedItem = item.getItemId();
        if(selecetedItem==R.id.action_home) {
            Intent intent = new Intent(activity_course_detail.this, MainActivity.class);
            startActivity(intent);
        }
        if (selecetedItem == R.id.action_settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
        }
        if (selecetedItem == R.id.action_about) {
            Intent intent_info = new Intent(activity_course_detail.this, activity_mark_attendance.class);
            startActivity(intent_info);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_course_detail);

        dbHelper = new DbHelper(this);

        courseNameView = findViewById(R.id.course_name);
        totalLecturesView = findViewById(R.id.total_lectures_view);
        attendedLecturesView = findViewById(R.id.attended_lectures_view);
        attendanceSummary=findViewById(R.id.attendance_summary);
        predictionMessage=findViewById(R.id.prediction_message);
        markAttendedButton = findViewById(R.id.btn_attended);
        markMissedButton = findViewById(R.id.btn_missed);

        FloatingActionButton deleteCourseButton = findViewById(R.id.delete_course_button);
        FloatingActionButton edit = findViewById(R.id.editAttendance);

        Intent intent_id = getIntent();
        courseId = intent_id.getIntExtra("courseId",-1);
        loadCourseDetails();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_edit = new Intent(activity_course_detail.this, activity_edit_attendance.class);
                intent_edit.putExtra("courseId",courseId);
                finish();
                startActivity(intent_edit);


            }
        });

        deleteCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteCourse(courseId);
                Intent intent_main = new Intent(activity_course_detail.this, MainActivity.class);
                finish();
                startActivity(intent_main);
            }
        });

        markAttendedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAttendance(true);

            }
        });

        markMissedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAttendance(false);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadCourseDetails() {

        Cursor cursor = dbHelper.getCourseById(courseId);
        if (cursor.moveToFirst()) {
            String courseName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            int totalLectures = cursor.getInt(cursor.getColumnIndexOrThrow("total_lectures"));
            int attendedLectures = cursor.getInt(cursor.getColumnIndexOrThrow("attended_lectures"));
            int lectureConducted = cursor.getInt(cursor.getColumnIndexOrThrow("lecture_conducted"));

            courseNameView.setText(courseName);
            String total_lec ="Total Lectures: " + totalLectures;
            totalLecturesView.setText(total_lec);
            String attended_lec = "Attended Lectures: " + attendedLectures;
            attendedLecturesView.setText(attended_lec);
            if(lectureConducted>0) {
                double attendancePercentage = (double) attendedLectures / lectureConducted * 100;
                String summary = "Attendance: " + String.format(Locale.US,"%.2f", attendancePercentage) + "% (" + lectureConducted + "/" + totalLectures + " lectures have been conducted)";
                attendanceSummary.setText(summary);
                double minAttendance = cursor.getDouble(cursor.getColumnIndexOrThrow("minimum_attendance"));
                int lecturesToAttend = (int) Math.ceil((minAttendance / 100) * totalLectures);
                int lecturesToMark = lecturesToAttend - attendedLectures;
                String message = "You need to attend " + lecturesToMark + " more lectures to meet the minimum attendance requirement.";
                predictionMessage.setText(message);
            }
        }
    }

    private void updateAttendance(boolean attended) {

        Cursor cursor = dbHelper.getCourseById(courseId);
        if (cursor.moveToFirst()) {
            int totalLectures = cursor.getInt(cursor.getColumnIndexOrThrow("total_lectures"));
            int attendedLectures = cursor.getInt(cursor.getColumnIndexOrThrow("attended_lectures"));
            int lectureConducted = cursor.getInt(cursor.getColumnIndexOrThrow("lecture_conducted"));
            if(lectureConducted<totalLectures){

                if (attended) {
                    ++attendedLectures;
                }
                ++lectureConducted;
                Toast.makeText(activity_course_detail.this, "Attendance updated successfully!", Toast.LENGTH_SHORT).show();
                dbHelper.updateAttendance(courseId, totalLectures,lectureConducted, attendedLectures);
            }
            else{
                AlertDialog.Builder alert= new AlertDialog.Builder(this);
                alert.setTitle("Alert");
                alert.setMessage("Your session has ended ! ");
                alert.setIcon(R.drawable.baseline_add_alert_24);
                alert.show();
                dbHelper.deleteCourse(courseId);
                Toast.makeText(activity_course_detail.this, "Your session has ended", Toast.LENGTH_SHORT).show();
            }

            loadCourseDetails();
        }

    }
}




