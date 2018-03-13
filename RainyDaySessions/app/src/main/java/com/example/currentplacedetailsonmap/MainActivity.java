package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position == 0){
                    Intent shredWindow = new Intent(MainActivity.this, parkActivity.class);
                    startActivity(shredWindow);
                }

                if(position == 1){
                    Intent shredWindow = new Intent(MainActivity.this, streetActivity.class);
                    startActivity(shredWindow);
                }

                if(position == 2){
                    Intent shredWindow = new Intent(MainActivity.this, beginnerActivity.class);
                    startActivity(shredWindow);
                }

                if(position == 3){
                    Intent shredWindow = new Intent(MainActivity.this, allActivity.class);
                    startActivity(shredWindow);
                }
            }
        });

        final Button btn = (Button) findViewById(R.id.contact);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:spostlethwaitebmx@gmail.com"));
                startActivity(emailIntent);
            }
        });

    }
}
