package com.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.appanalyzer.R;

import java.io.File;

public class intermediate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = getIntent();
                String apkPath = intent1.getStringExtra("path");
                File mFileToHash = new File(apkPath);
                MD5 md5Obj = new MD5();
                Snackbar.make(view, md5Obj.checkMD5(mFileToHash), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

}
