package org.openssf.openjournal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // Define ListView
    private ListView lv;
    // Define allNotes ArrayList
    public static ArrayList<String> allNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_home layout
        setContentView(R.layout.activity_home);

        // Initialize ListView from XML
        lv = (ListView) findViewById(R.id.list_view_home_activity);
        // Initialize ArrayList for data
        allNotes = getAllNotes(getApplicationContext());
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
            // Add onClickListener
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get title of note
                    String noteTitle = allNotes.get(position);
                    // Create new intent for opening ExistingNoteActivity
                    Intent existingNote = new Intent(getApplicationContext(), ExistingNoteActivity.class);
                    // Pass note title to Activity
                    existingNote.putExtra("note_title", noteTitle);
                    // Start activity
                    startActivity(existingNote);
                }

            });
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

    // Override onRestart() method to run refreshList()
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
        // TODO 7: Actually update ListView instead of restarting Activity
    }
}