package com.kongzue.titlebardemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.kongzue.titlebar.TitleBar;

public class AllTransparentActivity extends AppCompatActivity {
    
    private ScrollView scroller;
    private TitleBar titleBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_transparent);
        
        scroller = findViewById(R.id.scroller);
        titleBar = findViewById(R.id.titleBar);
        
        scroller.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                
                float alphaValue = scrollY / 200f;
                if (alphaValue > 1f) alphaValue = 1f;
    
                Log.i(">>>", "alphaValue=" + alphaValue);
                
                titleBar.setBackgroundAlpha(alphaValue);
            }
        });
    }
}
