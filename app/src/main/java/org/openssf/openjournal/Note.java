package org.openssf.openjournal;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.openssf.openjournal.utils.DBHelper;

/**
 * Note Class
 * Simple class for managing note deletion in Java
 */

class Note {
    // Variables
    private String nameOfNote;
    private Context context;
    // Define database helper class
    private DBHelper notesdb;

    Note(Context contextTwo, String noteName) {
        context = contextTwo;
        nameOfNote = noteName;
    }

    void delete() {
        // Initialize database helper class
        notesdb = new DBHelper(context);
        // Create & initialize new AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Set dialog title, message
        builder.setTitle(context.getResources().getString(R.string.delete_note))
                .setMessage(context.getResources().getString(R.string.delete_note_confirm))
                // Add "positive" button - discarding note
                .setPositiveButton(context.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int deleted = notesdb.deleteNote(notesdb.getNoteIdFromTitle(nameOfNote));
                        if(deleted == 1) {
                            // Close DBHelper
                            notesdb.close();
                            // Finish current activity
                            ((Activity)context).finish();
                            // If successful, tell user
                            Toast.makeText(context, context.getResources().getString(R.string.note_deleted), Toast.LENGTH_SHORT).show();
                        } else {
                            // If unsuccessful, tell user
                            Toast.makeText(context, context.getResources().getString(R.string.note_not_deleted), Toast.LENGTH_SHORT).show();
                            // Close notesdb
                            notesdb.close();
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
}
