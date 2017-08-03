package org.openssf.openjournal;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.openssf.openjournal.utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExistingNoteActivity extends AppCompatActivity {

    // Define title & id variables to be retrieved from Intent
    String noteTitle;
    int noteId;
    // Define note class
    Note note;
    // Define EditTexts
    EditText title;
    EditText text;
    // Define database helper class
    DBHelper notesdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to activity_existing_note layout
        setContentView(R.layout.activity_existing_note);

        // Initialize database helper class
        notesdb = new DBHelper(this);

        // Initialize EditText's from layout
        title = (EditText) findViewById(R.id.existing_note_title_edittext);
        text = (EditText) findViewById(R.id.existing_note_edittext);

        // Set Input types of note + title EditText
        text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        title.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);

        // Initialize "Last Modified" TextView from layout
        TextView lm = (TextView) findViewById(R.id.note_last_modified_textview);

        // Initialize noteTitle
        noteTitle = this.getIntent().getExtras().getString("note_title");
        // Initialize noteId
        noteId = this.getIntent().getExtras().getInt("note_id");
        // Initialize note
        note = new Note(this,noteTitle);

        // Set title EditText to noteTitle
        title.setText(noteTitle);
        // Set text EditText to text of note
        text.setText(notesdb.getData(noteId));

        // Set text of "last modified" TextView
        lm.setText(String.format(getString(R.string.last_modified), notesdb.getTimestamp(noteId)));

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
                note.delete(false);
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
        if(title.getText().toString().equals(noteTitle) && text.getText().toString().equals(notesdb.getData(noteId))) {
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
        if(title.getText().toString().equals(noteTitle) && text.getText().toString().equals(notesdb.getData(noteId))) {
            // Note is unchanged, exit activity
            finish();
        } else {
            if(title.getText().toString().matches("[a-zA-Z0-9!?. ]+")) {
                // Update note
                notesdb.updateNote(noteId, title.getText().toString(), text.getText().toString(), new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.US).format(new Date()));
                finish();
            } else {
                unsupportedCharacters();
            }
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
