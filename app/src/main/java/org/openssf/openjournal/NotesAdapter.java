package org.openssf.openjournal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.openssf.openjournal.utils.DBHelper;

import java.util.ArrayList;

/**
 * NotesAdapter Class
 * Custom Adapter class for RecyclerView in HomeActivity.java
 * Used for listing note titles and adding icons, etc.
 * @version 2.0
 * @author github.com/zanedb
 */

class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context requiredContext;
    private ArrayList<String> allNoteTitles;

    // Define database helper class
    private DBHelper notesdb;

    NotesAdapter(Context context, ArrayList<String> notes) {
        requiredContext = context;
        allNoteTitles = notes;

        // Initialize database helper class
        notesdb = new DBHelper(requiredContext);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, subtitleTextView;
        ImageButton deleteNoteButton;

        NotesViewHolder(View view) {
            super(view);
            // Initialize title TextView from layout
            titleTextView = (TextView) view.findViewById(R.id.notes_list_title);
            // Initialize subtitle TextView from layout
            subtitleTextView = (TextView) view.findViewById(R.id.notes_list_subtitle);
        }
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list, parent, false);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.NotesViewHolder holder, int position) {
        // Get note title
        final String noteTitle = allNoteTitles.get(position);
        // Set note title to display
        holder.titleTextView.setText(noteTitle);
        // Get number of characters of text
        int numberOfCharacters = notesdb.getData(notesdb.getNoteIdFromTitle(noteTitle)).replace(System.getProperty("line.separator"), " ").length();
        // Based on string length, shorten string
        String subtitle = (notesdb.getData(notesdb.getNoteIdFromTitle(noteTitle))).replace(System.getProperty("line.separator"), " ");
        if(numberOfCharacters > 90) {
            subtitle = subtitle.substring(0,100)+"..";
        } else if(numberOfCharacters > 60) {
            subtitle = subtitle.substring(0,60)+"..";
        } else if(numberOfCharacters > 30) {
            subtitle = subtitle.substring(0,30)+"..";
        } else if(numberOfCharacters < 30) {
            if(numberOfCharacters != 0) {
                subtitle += "..";
            }
        }
        // Set subtitle TextView to shortened string
        holder.subtitleTextView.setText(subtitle);
    }

    @Override
    public int getItemCount() {
        return allNoteTitles.size();
    }
}
