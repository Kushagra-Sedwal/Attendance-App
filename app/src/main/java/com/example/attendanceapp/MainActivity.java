package com.example.attendanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

private ListView courselistview;
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
            Toast.makeText(this, "Already on Home Page", Toast.LENGTH_SHORT).show();
        }
        if (selecetedItem == R.id.action_settings) {
            Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
        }
       if (selecetedItem == R.id.action_about) {
            Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show();
            Intent intent_info = new Intent(MainActivity.this, activity_mark_attendance.class);
            startActivity(intent_info);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        dbHelper=new DbHelper(getApplicationContext());
        courselistview=findViewById(R.id.courses_list_view);
        loadCourses();

        FloatingActionButton addCourseButton = findViewById(R.id.add_course_button);

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, activity_add_course.class);
                startActivity(intent);
            }
        });

        courselistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent2 = new Intent(MainActivity.this, activity_course_detail.class);

                intent2.putExtra("courseId",(int)id);
                startActivity(intent2);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadCourses() {
        Cursor cursor = dbHelper.getAllCourses();
        CustomCursorAdapter adapter = new CustomCursorAdapter(MainActivity.this, cursor);
        courselistview.setAdapter(adapter);
    }
}