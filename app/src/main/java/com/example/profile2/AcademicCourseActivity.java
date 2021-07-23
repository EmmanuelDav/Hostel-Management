package com.example.profile2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AcademicCourseActivity extends AppCompatActivity {
    ArrayList<String> mCoursesArrayList;
    ListView mRecylerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academic_course);
        mCoursesArrayList = new ArrayList<>();
        mRecylerview = findViewById(R.id.coursesRecylerview);
        AddCourses();
    }

    void AddCourses(){
        mCoursesArrayList.add("Collage Of Health Science");
        mCoursesArrayList.add("Faculty of engineering");
        mCoursesArrayList.add("Faculty of Art and Social Science");
        mCoursesArrayList.add("Faculty of Law");
        mCoursesArrayList.add("Faculty of Managed and Applied Science");
        mCoursesArrayList.add("Faculty OF Management Science");
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCoursesArrayList);
        mRecylerview.setAdapter(itemsAdapter);
    }
}