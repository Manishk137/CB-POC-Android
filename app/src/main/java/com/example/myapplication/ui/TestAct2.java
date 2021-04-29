package com.example.myapplication.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.myapplication.R;

public class TestAct2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_two);

        String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};
        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);
    }
}
