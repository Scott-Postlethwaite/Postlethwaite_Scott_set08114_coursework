package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class shredActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shred);

        TextView distance = (TextView) findViewById(R.id.distanceId);
        String d = getIntent().getStringExtra("shredDistance");

        distance.setText("Distance: " + d + "km");

        final Button btn = (Button) findViewById(R.id.directions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=55.480964, -4.603481");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        final Button webtn = (Button) findViewById(R.id.website);
        webtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://shredskatepark.co.uk/"));
                startActivity(browserIntent);
            }
        });

        final Button instbtn = (Button) findViewById(R.id.insta);
        instbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/shredskatepark");
                Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
                instagram.setPackage("com.instagram.android");
                startActivity(instagram);
            }
        });
    }
}
