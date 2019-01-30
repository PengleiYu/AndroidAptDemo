package com.example.penglei.aptdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.annotationlib.DIActivity;
import com.example.annotationlib.DIView;

//@HelloWorld
@DIActivity
public class MainActivity extends AppCompatActivity {
    // TODO: 2019/1/30 这里不能private
    @DIView(R.id.tv_main)
    TextView mTextView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DIMainActivity.bindView(this);

        mTextView.setText("Hello DI!");
    }
}
