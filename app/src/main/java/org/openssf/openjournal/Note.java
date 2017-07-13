package org.openssf.openjournal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Note Class
 * Simple class for managing note deletion and reading in Java
 */

class Note {
    // Variables
    private String nameOfNote;
    private Context context;

    Note(Context contextTwo, String noteName) {
        context = contextTwo;
        nameOfNote = noteName;
    }

    void delete() {
        // Locate file directory
        final File dir = context.getFilesDir();
        // Create & initialize new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set dialog title, message
        builder.setTitle(context.getResources().getString(R.string.delete_note))
                .setMessage(context.getResources().getString(R.string.delete_note_confirm))
                // Add "positive" button - discarding note
                .setPositiveButton(context.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Append identifier for removing note
                        nameOfNote += "_openJournalNote";
                        // Create new file from name
                        File file = new File(dir, nameOfNote);
                        // Attempt to delete
                        boolean deleted = file.delete();
                        if(deleted) {
                            // WORKAROUND TO ListView refreshing - serves purpose of HomeActivity.onRestart() function
                            // Start new Activity
                            context.startActivity(new Intent(context, HomeActivity.class));
                            // Finish current activity
                            ((Activity)context).finish();
                            // TODO 6: Fix this workaround so it automatically updates ListView and doesn't use a workaround
                            // If successful, tell user
                            Toast.makeText(context, context.getResources().getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
                        } else {
                            // If unsuccessful, tell user
                            Toast.makeText(context, context.getResources().getString(R.string.note_not_deleted), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                // Set "negative" button - not discarding note
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, because the note should NOT be deleted
                    }
                })
                // Show dialog box
                .show();
    }

    String readNote() {
        // Set noteText to error code in case try/catch fails
        String noteText = context.getResources().getString(R.string.ioexception);
        try {
            // Initialize and create FileInputStream with filename
            FileInputStream fis = context.openFileInput(nameOfNote+"_openJournalNote");
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
