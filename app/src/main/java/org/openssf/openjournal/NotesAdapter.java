package org.openssf.openjournal;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
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

public class NotesAdapter extends BaseAdapter {

    private Context requiredContext;
    private LayoutInflater layoutInflater;
    private ArrayList<String> allNoteTitles;

    public NotesAdapter(Context context, ArrayList<String> notes) {
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

        String noteTitle = (String) getItem(position);
        titleTextView.setText(noteTitle);
        subtitleTextView.setText("Add subtitle text here..");

        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // TODO 4: Delete note on button press
            }
        });

        return rowView;
    }
}
