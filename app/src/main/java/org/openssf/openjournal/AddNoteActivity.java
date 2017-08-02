package org.openssf.openjournal;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.openssf.openjournal.utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    // Define database helper class
    DBHelper notesdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_add_note layout
        setContentView(R.layout.activity_add_note);

        // Initialize database helper class
        notesdb = new DBHelper(this);

        // Initialize Toolbar from layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_note);
        // Set as Action Bar
        setSupportActionBar(toolbar);
        // Add back button to Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Change ActionBar title to Add Note
        getSupportActionBar().setTitle(getString(R.string.add_note));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items in ActionBar/add items to ActionBar
        getMenuInflater().inflate(R.menu.menu_note_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle ClickEvents on ActionBar/Toolbar items
        switch (item.getItemId()) {
            // Checkmark ClickEvent for saving note
            case R.id.done_checkmark:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        // Handle back button in toolbar button press
        handleBackInput();
        // Return true because we performed back navigation
        return true;
    }

    @Override
    public void onBackPressed() {
        // Handle app back button press
        handleBackInput();
    }

    private void handleBackInput() {
        EditText note = (EditText) findViewById(R.id.note_edittext);
        EditText note_title = (EditText) findViewById(R.id.note_title_edittext);
        if(note.getText().toString().equals("") && note_title.getText().toString().equals("")) {
            // Send message informing user note is being discarded
            Toast.makeText(AddNoteActivity.this, getString(R.string.discarding), Toast.LENGTH_SHORT).show();
            // Exit activity
            finish();
        } else {
            // Create & initialize new AlertDialog Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Set dialog title, message
            builder.setTitle(getString(R.string.discard_note))
                    .setMessage(getString(R.string.discard_note_confirm))
                    // Add "positive" button - discarding note
                    .setPositiveButton(getString(R.string.discard), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Send Toast message telling user the note is being discarded
                            Toast.makeText(AddNoteActivity.this, getString(R.string.discarding), Toast.LENGTH_SHORT).show();
                            // Exit Activity & return to HomeActivity
                            finish();
                        }
                    })
                    // Set "negative" button - not discarding note
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing, because the note should NOT be discarded
                        }
                    })
                    // Show dialog box
                    .show();
        }
    }

    /** UNUSED/DEPRECATED
     *  Old saveNote() Function
     *  Saves note in local storage
     *  Replaced by new SQLite saveNote() function
    private void saveNote() {
        // Get value of note & title
        EditText note = (EditText) findViewById(R.id.note_edittext);
        EditText note_title = (EditText) findViewById(R.id.note_title_edittext);

        // Check if title is empty
        if(note_title.getText().toString().equals("")) {
            Toast.makeText(AddNoteActivity.this, getString(R.string.empty_text), Toast.LENGTH_SHORT).show();
        } else {
            // Check if title contains characters other than a-z A-Z 0-9 ?!
            // If not, allow them to save file
            if(note_title.getText().toString().matches("[a-zA-Z0-9!?. ]+")) {
                // Create FileOutputStream for writing file
                FileOutputStream fos;
                // Create new file to see if title already exists
                File filecheck = new File(this.getFilesDir(), note_title.getText().toString()+"_openJournalNote");
                if(filecheck.exists()) {
                    // If it exists, warn users
                    Toast.makeText(AddNoteActivity.this, getString(R.string.file_already_exists), Toast.LENGTH_SHORT).show();
                } else if(note.getText().toString().contains("openJournalTimestamp_")){
                    // Check if note contains timestamp identifier (could cause rendering problems)
                    // If so, display unsupported characters warning
                    unsupportedCharacters();
                } else {
                    // Otherwise, create it
                    try {
                        // Open FileOutputStream
                        fos = openFileOutput(note_title.getText().toString()+"_openJournalNote", Context.MODE_PRIVATE);
                        // Create new String to store timestamp and add identifier
                        String timestamp = "openJournalTimestamp_";
                        // Add date/time info to String
                        timestamp += new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.US).format(new Date());
                        // Write text with appended timestamp
                        // Replace \n line breaks with official line breaks (workaround to fix issue)
                        fos.write((note.getText().toString()+timestamp).getBytes());
                        // Tell user the note was saved
                        Toast.makeText(AddNoteActivity.this, getString(R.string.saving_note), Toast.LENGTH_SHORT).show();
                        fos.close();
                        finish();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(AddNoteActivity.this, getString(R.string.file_not_found_exception), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(AddNoteActivity.this, getString(R.string.ioexception), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Otherwise, show dialog box explaining the error
                unsupportedCharacters();
            }
        }
    }
    **/

    private void saveNote() {
        // Get value of note & title
        EditText note = (EditText) findViewById(R.id.note_edittext);
        EditText note_title = (EditText) findViewById(R.id.note_title_edittext);

        // Check if title is empty
        if(note_title.getText().toString().equals("")) {
            Toast.makeText(AddNoteActivity.this, getString(R.string.empty_text), Toast.LENGTH_SHORT).show();
        } else {
            // Check if title contains characters other than a-z A-Z 0-9 ?!
            // If not, allow them to save file
            if(note_title.getText().toString().matches("[a-zA-Z0-9!?. ]+")) {
                // Check if note exists
                boolean filecheck = notesdb.doesNoteExist(note_title.getText().toString());
                if(filecheck) {
                    // If it exists, warn users
                    Toast.makeText(AddNoteActivity.this, getString(R.string.file_already_exists), Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, create it
                    notesdb.insertNote(note_title.getText().toString(), note.getText().toString(), (new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.US).format(new Date())));
                    // End activity
                    finish();
                }
            } else {
                // Otherwise, show dialog box explaining the error
                unsupportedCharacters();
            }
        }
    }

    void unsupportedCharacters() {
        // Create & initialize new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set dialog title, message
        builder.setTitle(getString(R.string.unsupported_characters))
                .setMessage(getString(R.string.unsupported_characters_long))
                // Add okay button
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, just an alert message
                    }
                })
                // Show dialog box
                .show();
    }
}
