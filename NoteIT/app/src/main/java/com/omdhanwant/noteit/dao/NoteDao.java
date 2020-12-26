package com.omdhanwant.noteit.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.omdhanwant.noteit.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("Select * from notes order by id desc")
    List<Note> getAllNotes();

    // Replace strategy will replace the note data if the id with same note is inserted!
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Void insertNote(Note note);

    @Delete
    Void deleteNote(Note note);
}
