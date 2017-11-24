package org.openssf.openjournal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EndOfLifeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endoflife);
    }

    public void downloadStandardNotes(View view) {
        String appPackageName = "com.standardnotes";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void exportNotes(View view) {
        Toast.makeText(EndOfLifeActivity.this, getString(R.string.exporting), Toast.LENGTH_SHORT).show();
        TextView et = (TextView) findViewById(R.id.export_text);
        et.setText(R.string.export_start_msg);
    }

    public void openHomeActivity(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
