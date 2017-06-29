package org.openssf.openjournal;

import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_home layout
        setContentView(R.layout.activity_add_note);

        // Initialize Toolbar from layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set Toolbar to app ActionBar
        setSupportActionBar(toolbar);
        // Set navigation icon to back button
        //if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        //} else {
            toolbar.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back_black_24dp));
        //}
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                Toast.makeText(null, "Back button clicked", Toast.LENGTH_LONG).show();
            }
        });
    }
}
