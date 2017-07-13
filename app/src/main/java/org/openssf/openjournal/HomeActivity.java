package org.openssf.openjournal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // Define ListView
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_home layout
        setContentView(R.layout.activity_home);

        // Initialize ListView from XML
        lv = (ListView) findViewById(R.id.list_view_home_activity);
        // Initialize ArrayList for data
        ArrayList<String> allNotes = getAllNotes(getApplicationContext());
        // Check if empty ArrayList
        if(allNotes.size() == 0) {
            // If so, display text about it
            TextView noNotes = (TextView) findViewById(R.id.no_notes_textview);
            noNotes.setVisibility(View.VISIBLE);
        } else {
            // Otherwise, display list items
            // Create new adapter with note titles
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allNotes);
            NotesAdapter adapter = new NotesAdapter(this, allNotes);
            // Set ListView adapter
            lv.setAdapter(adapter);
            // Set ListView to visible
            lv.setVisibility(View.VISIBLE);
        }

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
    }

    public ArrayList<String> getAllNotes(Context context) {
        // Create ArrayList to store note titles in
        ArrayList notes = new ArrayList();
        // Create File array of filenames
        File[] filenames = this.getFilesDir().listFiles();
        // If the File's name is a note, then add it to the notes ArrayList
        int fnLength = filenames.length;
        for(int i=0;i<fnLength;i++) {
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

    // Override onRestart() method to recreate activity
    // Workaround to refresh list of notes
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


}