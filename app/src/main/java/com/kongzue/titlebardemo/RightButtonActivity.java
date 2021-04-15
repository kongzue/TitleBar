package com.kongzue.titlebardemo;

import androidx.appcompat.app.AppCompatActivity;
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
    
        titleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RightButtonActivity.this,"点击了titleBar",Toast.LENGTH_SHORT).show();
            }
        });
    
        titleBar.setOnRightButtonPressed(new OnRightButtonPressed() {
            @Override
            public void onRightButtonPressed(View v) {
                Toast.makeText(RightButtonActivity.this,"点击了右侧按钮",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
