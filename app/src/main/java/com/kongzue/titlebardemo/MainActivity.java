package com.kongzue.titlebardemo;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.titlebar.TitleBar;
import com.kongzue.titlebar.interfaces.OnTitleBarDoubleClick;

public class MainActivity extends AppCompatActivity {
    
    private TitleBar titleBar;
    private Button btnNoTransparent;
    private Button btnIosStyle;
    private Button btnRightButton;
    private Button btnWithTip;
    private Button btnAllTransparent;
    private TextView linkGithub;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        titleBar = findViewById(R.id.titleBar);
        btnNoTransparent = findViewById(R.id.btn_no_transparent);
        btnIosStyle = findViewById(R.id.btn_ios_style);
        btnRightButton = findViewById(R.id.btn_right_button);
        btnWithTip = findViewById(R.id.btn_with_tip);
        btnAllTransparent = findViewById(R.id.btn_all_transparent);
        linkGithub = findViewById(R.id.link_github);
        
        btnNoTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NoTransparentActivity.class));
            }
        });
        
        btnIosStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, IOSStyleActivity.class));
            }
        });
        
        btnRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RightButtonActivity.class));
            }
        });
        
        btnWithTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WithTipActivity.class));
            }
        });
        
        btnAllTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllTransparentActivity.class));
            }
        });
        
        linkGithub.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/kongzue/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
