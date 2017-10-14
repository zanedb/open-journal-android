package org.openssf.openjournal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.openssf.openjournal.utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    // Define database helper class
    DBHelper notesdb;
    // Define notes
    EditText note;
    EditText note_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_add_note layout
        setContentView(R.layout.activity_add_note);

        // Initialize notes
        note = (EditText) findViewById(R.id.note_edittext);
        note_title = (EditText) findViewById(R.id.note_title_edittext);
        // Set Input types of note + title EditText
        note.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        note_title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);

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

        // Open keyboard
        showKeyboard(note_title);

        // SHARE MENU CODE:

        // Get intent
        Intent intent = getIntent();
        // Get extras
        Bundle extras = intent.getExtras();
        // Get action
        String action = intent.getAction();
        // If coming from share menu, pre-fill note EditText with shared data
        if (Intent.ACTION_SEND.equals(action)) {
            note.setText(extras.getString(Intent.EXTRA_TEXT));
        }
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
            // Hide keyboard + exit activity
            hideKeyboard();
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
                            // Exit Activity & return to HomeActivity, while closing keyboard
                            hideKeyboard();
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

    private void saveNote() {
        // Get value of note & title

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
                    if(DateFormat.is24HourFormat(this)) {
                        notesdb.insertNote(note_title.getText().toString(), note.getText().toString(), (new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US).format(new Date())));
                    } else {
                        notesdb.insertNote(note_title.getText().toString(), note.getText().toString(), (new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.US).format(new Date())));
                    }
                    // Close keyboard and exit activity
                    hideKeyboard();
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

    void showKeyboard(View view) {
        // Function to open keyboard to specified view
        // Create new IMM for manipulating keyboard
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // Enable keyboard
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        // Open to specified view
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    void hideKeyboard() {
        // Create IMM to close keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Close keyboard
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /*
    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK);
        super.finish();
    }
    */
}