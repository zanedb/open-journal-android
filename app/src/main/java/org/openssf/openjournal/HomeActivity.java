package org.openssf.openjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.openssf.openjournal.utils.DBHelper;
import org.openssf.openjournal.utils.RecyclerItemClickListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    // Define RecyclerView
    private RecyclerView rv;
    // Define LinearLayoutManager
    private LinearLayoutManager llm;
    // Define allNotes ArrayList
    public static ArrayList<String> allNotes;
    // Define database helper class
    DBHelper notesdb;
    // Define NotesAdapter
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_home layout
        setContentView(R.layout.activity_home);

        // Initialize database helper class
        notesdb = new DBHelper(this);

        // Initialize RecyclerView from XML
        rv = (RecyclerView) findViewById(R.id.recycler_view_home_activity);
        // Initialize LinearLayoutManager
        llm = new LinearLayoutManager(this);
        // Set RecyclerView LinearLayoutManager to llm
        rv.setLayoutManager(llm);
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
            adapter = new NotesAdapter(this, allNotes);
            // Set RecyclerView adapter
            rv.setAdapter(adapter);
            // Add divider between rows
            rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            // Add onClickListener
            rv.addOnItemTouchListener(
                    new RecyclerItemClickListener(getApplicationContext(), rv, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // Row click
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

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    })
            );
            // Set RecyclerView to visible
            rv.setVisibility(View.VISIBLE);
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