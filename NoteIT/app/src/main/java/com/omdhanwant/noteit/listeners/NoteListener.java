package com.omdhanwant.noteit.listeners;

import com.omdhanwant.noteit.entities.Note;

public interface NoteListener {
    void onNoteClicked(Note note, int position);
}
