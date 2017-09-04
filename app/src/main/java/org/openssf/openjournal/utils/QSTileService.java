package org.openssf.openjournal.utils;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;

import org.openssf.openjournal.AddNoteActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class QSTileService extends TileService {

    @Override
    public void onClick() {
        if (!isSecure()) {
            // If device is unlocked, open AddNoteActivity
            addNote();
        } else {
            unlockAndRun(new Runnable() {
                @Override
                public void run() {
                    // If not, request unlock, and if unlocked, open AddNoteActivity
                    addNote();
                }
            });
        }
    }

    private void addNote() {
        // Create new Intent to open AddNoteActivity
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }
}