package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_add_course extends AppCompatActivity {
    private EditText courseNameInput, totalLecturesInput, minAttendanceInput;
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
            Intent intent = new Intent(activity_add_course.this, MainActivity.class);
            startActivity(intent);
            }
        if (selecetedItem == R.id.action_settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
        }
        if (selecetedItem == R.id.action_about) {
            Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
            Intent intent_info = new Intent(activity_add_course.this, activity_mark_attendance.class);
            startActivity(intent_info);
        }
        return super.onOptionsItemSelected(item);
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_course);

        dbHelper = new DbHelper(this);

        courseNameInput = findViewById(R.id.course_name_input);
        totalLecturesInput = findViewById(R.id.total_lectures_input);
        minAttendanceInput = findViewById(R.id.min_attendance_input);
        Button saveButton = findViewById(R.id.btn_save_course);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addCourse() {
        String name = courseNameInput.getText().toString();
        int totalLectures = Integer.parseInt(totalLecturesInput.getText().toString());
        double minAttendance = Double.parseDouble(minAttendanceInput.getText().toString());

        dbHelper.addCourse(name, totalLectures, 0, 0,minAttendance);
        Toast.makeText(this, "Course added successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(activity_add_course.this, MainActivity.class);
        startActivity(intent);
    }
}
