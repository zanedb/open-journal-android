package org.openssf.openjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.openssf.openjournal.utils.DBHelper;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // Define ListView
    private ListView lv;
    // Define allNotes ArrayList
    public static ArrayList<String> allNotes;
    // Define database helper class
    DBHelper notesdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_home layout
        setContentView(R.layout.activity_home);

        // Initialize database helper class
        notesdb = new DBHelper(this);

        // Initialize ListView from XML
        lv = (ListView) findViewById(R.id.list_view_home_activity);
        // Initialize ArrayList for data
        allNotes = notesdb.getAllNotes();
        // Check if empty ArrayList
        if(allNotes.size() == 0) {
            // If so, display text about it
            TextView noNotes = (TextView) findViewById(R.id.no_notes_textview);
            noNotes.setVisibility(View.VISIBLE);
        } else {
            // Otherwise, display list items
            // Create new adapter with note titles
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
                    // Pass note ID to Activity
                    existingNote.putExtra("note_id", notesdb.getNoteIdFromTitle(noteTitle));
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

        // Initialize Toolbar from layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home_activity);
        // Set as Action Bar
        setSupportActionBar(toolbar);
    }

    /*
      TEMPORARILY DISABLE SEARCH ICON
      Will be coming in later version..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items in ActionBar/add items to ActionBar
        getMenuInflater().inflate(R.menu.menu_home_activity_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle ClickEvents on ActionBar/Toolbar items
        switch (item.getItemId()) {
            // Checkmark ClickEvent for saving note
            case R.id.search_icon:
                searchIcon();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } **/

    /** UNUSED/DEPRECATED
     *  getAllNotes() function
     *  switched to SQLite
     *  TODO to be removed
    public ArrayList<String> getAllNotes() {
        // Create ArrayList to store note titles in
        ArrayList notes = new ArrayList();
        // Create File array of filenames
        File[] filenames = this.getFilesDir().listFiles();
        // If the File's name is a note, then add it to the notes ArrayList
        for(File filename : filenames) {
            if (filename.getName().endsWith("_openJournalNote")) {
                // Add to ArrayList and remove _openJournalNote identifier from display
                notes.add(filename.getName().substring(0, filename.getName().length() - 16));
            }
        }
        // Return the notes ArrayList
        return notes;
     }
     **/

    // Override onRestart() method to run refreshList()
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent().addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        // TODO: Actually update ListView instead of restarting Activity
    }

    public void searchIcon() {
        // TODO: Add code here to search notes
    }
}