package com.example.profile2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        findViewById(R.id.admission).setOnClickListener( view ->{
            openChrome("http://rnu.edu.ng/rnu_admission/");
        });
        findViewById(R.id.news).setOnClickListener( view ->{
            openChrome("http://rnu.edu.ng/news.php");
        });
    }

    void openChrome( String uri) {
        CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
        customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));
        customIntent.setShowTitle(false);
        openCustomTab(this, customIntent.build(), Uri.parse(uri));
    }

    private void  openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        String packageName = "com.android.chrome";
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}