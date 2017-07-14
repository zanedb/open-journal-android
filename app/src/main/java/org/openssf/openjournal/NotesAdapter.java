package org.openssf.openjournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * NotesAdapter Class
 * Custom Adapter class for ListView in HomeActivity.java
 * Used for listing note titles and adding icons, etc.
 * @version 1.0
 * @author github.com/zanedb
 */

class NotesAdapter extends BaseAdapter {

    private Context requiredContext;
    private LayoutInflater layoutInflater;
    private ArrayList<String> allNoteTitles;

    NotesAdapter(Context context, ArrayList<String> notes) {
        requiredContext = context;
        allNoteTitles = notes;
        layoutInflater = (LayoutInflater) requiredContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return allNoteTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return allNoteTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Set layout and inflate view
        View rowView = layoutInflater.inflate(R.layout.notes_list, parent, false);
        // Initialize title TextView from layout
        TextView titleTextView = (TextView) rowView.findViewById(R.id.notes_list_title);
        // Initialize subtitle TextView from layout
        TextView subtitleTextView = (TextView) rowView.findViewById(R.id.notes_list_subtitle);
        // Initialize note deletion icon from layout
        ImageButton deleteNoteButton = (ImageButton) rowView.findViewById(R.id.notes_list_delete_button);

        // Get title of note from ArrayList
        final String noteTitle = (String) getItem(position);
        titleTextView.setText(noteTitle);
        // Get text of note
        Note note = new Note(requiredContext,noteTitle);
        // Remove all but 8 characters and set subtitle text to that
        int numberOfCharacters = note.readNote().length();
        if(numberOfCharacters > 50) {
            subtitleTextView.setText(note.readNote().substring(0,50)+"..");
        } else if(numberOfCharacters > 30) {
            subtitleTextView.setText(note.readNote().substring(0,30)+"..");
        } else if(numberOfCharacters > 15) {
            subtitleTextView.setText(note.readNote().substring(0,15)+"..");
        } else if(numberOfCharacters < 15) {
            subtitleTextView.setText(note.readNote()+"..");
        }


        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Note note = new Note(requiredContext,noteTitle);
                note.delete();
            }
        });

        return rowView;
    }
}
