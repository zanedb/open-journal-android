package org.openssf.openjournal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // Define LinearLayout from layout file for adding elements
    LinearLayout ll = (LinearLayout) findViewById(R.id.linear_layout_activity_home);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_home layout
        setContentView(R.layout.activity_home);

        // Initialize FAB from layout for adding onClickListener
        FloatingActionButton addNote = (FloatingActionButton) findViewById(R.id.add_note_fab);
        // Add onClickListener
        addNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // On click, open AddNoteActivity
                Intent addnoteintent = new Intent(HomeActivity.this, AddNoteActivity.class);
                startActivity(addnoteintent);
            }
        });

        // TODO 1: Implement properly using ListView (https://developer.android.com/guide/topics/ui/layout/listview.html)

        // Define ArrayList to contain all note titles
        ArrayList allNotes = getAllNotes(getApplicationContext());
        // Go through each and add to layout
        for(int i=0;allNotes.size()<i;i++) {
            // Add elements here
        }
    }

    public ArrayList getAllNotes(Context context) {
        // Create ArrayList to store note titles in
        ArrayList notes = new ArrayList();
        // Create File array of filenames
        File[] filenames = this.getFilesDir().listFiles();
        // If the File's name is a note, then add it to the notes ArrayList
        for(int i=0;filenames.length<i;i++) {
            if(filenames[i].getName().endsWith("_openJournalNote")) {
                // Add to ArrayList and remove _openJournalNote identifier from display
                notes.add(filenames[i].getName().substring(0, filenames[i].getName().length() - 16));
            }
        }
        // Return the notes ArrayList
        return notes;
    }

    public String readNote(String noteName) {
        // Set noteText to error code in case try/catch fails
        String noteText = getString(R.string.ioexception);
        try {
            // Initialize and create FileInputStream with filename
            FileInputStream fis = openFileInput(noteName+"_openJournalNote");
            // Initialize and create InputStreamReader based on FileInputStream
            InputStreamReader isr = new InputStreamReader(fis);
            // Initialize and create BufferedReader based on InputStreamReader
            BufferedReader bufferedReader = new BufferedReader(isr);
            // Create/initialize StringBuilder
            StringBuilder sb = new StringBuilder();
            // Create string for checking if we have next line
            String line;
            // While loop to read next string
            while ((line = bufferedReader.readLine()) != null) {
                // Append next line to total text read
                sb.append(line);
            }
            // Set text string
            noteText = sb.toString();
            // Closing streams
            fis.close();
            isr.close();
            bufferedReader.close();
        } catch (IOException e) {
            // Do nothing if failure, as noteText will equal R.string.ioexception
        }
        // Return string
        return noteText;
    }
}