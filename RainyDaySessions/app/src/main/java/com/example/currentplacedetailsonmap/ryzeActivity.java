package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ryzeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ryze);

        TextView distance = (TextView) findViewById(R.id.distanceId);
        String d = getIntent().getStringExtra("ryzeDistance");

        distance.setText("Distance: " + d + "km");

        final Button btn = (Button) findViewById(R.id.directions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=55.866139, -3.054392");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        final Button webtn = (Button) findViewById(R.id.website);
        webtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ryzeandroll.co.uk/"));
                startActivity(browserIntent);
            }
        });

        final Button instbtn = (Button) findViewById(R.id.insta);
        instbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/ryze_and_roll");
                Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
                instagram.setPackage("com.instagram.android");
                startActivity(instagram);
            }
        });
    }
}
