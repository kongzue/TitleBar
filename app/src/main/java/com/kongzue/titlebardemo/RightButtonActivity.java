package com.kongzue.titlebardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kongzue.titlebar.TitleBar;
import com.kongzue.titlebar.interfaces.OnBackPressed;
import com.kongzue.titlebar.interfaces.OnRightButtonPressed;

public class RightButtonActivity extends AppCompatActivity {
    
    private TitleBar titleBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_button);
        
        titleBar = findViewById(R.id.titleBar);
    
        titleBar.setOnRightButtonPressed(new OnRightButtonPressed() {
            @Override
            public void onRightButtonPressed(View v) {
                Toast.makeText(RightButtonActivity.this,"点击了右侧按钮",Toast.LENGTH_SHORT).show();
            }
        });
    
        titleBar.setOnBackPressed(new OnBackPressed() {
            @Override
            public void onBackPressed(View v) {
        
            }
        });
    }
}
