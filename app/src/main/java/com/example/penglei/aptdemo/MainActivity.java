package com.example.penglei.aptdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.annotationlib.HelloWorld;

@HelloWorld
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
