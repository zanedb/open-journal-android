package org.openssf.openjournal;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExistingNoteActivity extends AppCompatActivity {

    // Define title String and retrieve it from Intent
    String noteTitle;
    // Define note class
    Note note;
    // Define EditTexts
    EditText title;
    EditText text;
    // Define FileOutputStream
    FileOutputStream fos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_existing_note layout
        setContentView(R.layout.activity_existing_note);

        // Initialize EditText's from layout
        title = (EditText) findViewById(R.id.existing_note_title_edittext);
        text = (EditText) findViewById(R.id.existing_note_edittext);

        // Initialize noteTitle
        noteTitle = this.getIntent().getExtras().getString("note_title");
        // Initialize note
        note = new Note(this,noteTitle);

        // Set title EditText to noteTitle
        title.setText(noteTitle);
        // Set text EditText to  note.readNote()
        text.setText(note.readNote());

        // Initialize Toolbar from layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_existing_note);
        // Set as Action Bar
        setSupportActionBar(toolbar);
        // Add back button to Toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Change ActionBar title to Add Note
        getSupportActionBar().setTitle(noteTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items in ActionBar/add items to ActionBar
        getMenuInflater().inflate(R.menu.menu_existing_note_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle ClickEvents on ActionBar/Toolbar items
        switch (item.getItemId()) {
            // Checkmark ClickEvent for saving note
            case R.id.done_checkmark_existing_note:
                saveNote();
                return true;
            case R.id.delete_icon_existing_note_toolbar:
                note.delete();
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
        if(title.getText().toString().equals(noteTitle) && text.getText().toString().equals(note.readNote())) {
            // Note is unchanged, exit activity
            finish();
        } else {
            // Create & initialize new AlertDialog Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Set dialog title, message
            builder.setTitle(getString(R.string.discard_changes))
                    .setMessage(getString(R.string.discard_changes_confirm))
                    // Add "positive" button - discarding note
                    .setPositiveButton(getString(R.string.discard), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Send Toast message telling user the note is being discarded
                            Toast.makeText(ExistingNoteActivity.this, getString(R.string.discarding), Toast.LENGTH_SHORT).show();
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

    private void saveNote() {
        if(title.getText().toString().equals(noteTitle) && text.getText().toString().equals(note.readNote())) {
            // Note is unchanged, exit activity
            finish();
        } else {
            if (!title.getText().toString().equals(noteTitle)) {
                if(title.getText().toString().matches("[a-zA-Z0-9!?. ]+")) {
                    // Create new file to see if title already exists
                    File filecheck = new File(this.getFilesDir(), title.getText().toString()+"_openJournalNote");
                    if(filecheck.exists()) {
                        // If it exists, warn users
                        Toast.makeText(ExistingNoteActivity.this, getString(R.string.file_already_exists), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!text.getText().toString().equals(note.readNote())) {
                            // If title and text are changed, save new note under different title and remove old one
                            saveUpdatedNote();
                        } else {
                            // If only title is changed, save new title and remove old one
                            saveUpdatedNote();
                        }
                    }
                } else {
                    unsupportedCharacters();
                }
            } else {
                // If title is not changed
                if (!text.getText().toString().equals(note.readNote())) {
                    // If only text is changed, save note under same title
                    try {
                        // Open FileOutputStream
                        fos = openFileOutput(title.getText().toString() + "_openJournalNote", Context.MODE_PRIVATE);
                        fos.write(text.getText().toString().getBytes());
                        // Tell user the note was saved
                        Toast.makeText(ExistingNoteActivity.this, getString(R.string.saving_note), Toast.LENGTH_SHORT).show();
                        fos.close();
                        finish();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(ExistingNoteActivity.this, getString(R.string.file_not_found_exception), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ExistingNoteActivity.this, getString(R.string.ioexception), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    void saveUpdatedNote() {
        try {
            // Open FileOutputStream
            fos = openFileOutput(title.getText().toString() + "_openJournalNote", Context.MODE_PRIVATE);
            fos.write(text.getText().toString().getBytes());
            File oldtitle = new File(getApplicationContext().getFilesDir(), noteTitle + "_openJournalNote");
            if (oldtitle.delete()) {
                Toast.makeText(ExistingNoteActivity.this, getString(R.string.new_title_saved), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ExistingNoteActivity.this, getString(R.string.general_error), Toast.LENGTH_SHORT).show();
            }
            fos.close();
            finish();
        } catch (FileNotFoundException e) {
            Toast.makeText(ExistingNoteActivity.this, getString(R.string.file_not_found_exception), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(ExistingNoteActivity.this, getString(R.string.ioexception), Toast.LENGTH_SHORT).show();
        }
    }

    void unsupportedCharacters() {
        // Show dialog box explaining the error
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
