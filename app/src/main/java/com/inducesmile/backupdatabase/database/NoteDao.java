package com.inducesmile.backupdatabase.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NoteDao {

    // Notes
    @Query("SELECT * FROM notes WHERE trashed IS 0 ORDER BY id DESC")
    LiveData<List<Note>> getNotes();

    @Query("SELECT COUNT(*) FROM notes WHERE trashed IS 0")
    LiveData<Integer> getNotesCount();

    @Query("SELECT * FROM notes WHERE starred IS 1 AND trashed IS 0 ORDER BY id DESC")
    LiveData<List<Note>> getStarredNotes();

    @Query("SELECT COUNT(*) FROM notes WHERE starred IS 1 AND trashed IS 0")
    LiveData<Integer> getStarredCount();

    @Query("SELECT * FROM notes WHERE trashed IS 1 ORDER BY id DESC")
    LiveData<List<Note>> getTrashedNotes();

    @Query("SELECT COUNT(*) FROM notes WHERE trashed IS 1")
    int getTrashedCount();

    @Query("SELECT * FROM notes WHERE remindAt IS NOT \"\" AND trashed IS 0 ORDER BY id DESC")
    LiveData<List<Note>> getNotesWithReminder();

    @Query("SELECT COUNT(*) FROM notes WHERE remindAt IS NOT \"\" AND trashed IS 0")
    int getReminderCount();

    @Query("SELECT * FROM notes WHERE folder IS :folder AND trashed IS 0 ORDER BY id DESC")
    LiveData<List<Note>> getNotesByFolder(String folder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addNote(Note note);

    @Query("SELECT * FROM notes WHERE id IS :id")
    LiveData<Note> getNote(int id);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Delete
    void deleteNotes(List<Note> notes);

    // Folders
    @Query("SELECT * FROM folders ORDER BY name ASC")
    LiveData<List<Folder>> getFoldersByName();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFolder(Folder folder);

    @Update
    void updateFolder(Folder folder);

    @Query("SELECT * FROM folders WHERE id IS :id ")
    Folder getFolder(int id);

    @Query("SELECT COUNT(*) FROM notes WHERE folder IS :folder")
    LiveData<Integer> getFolderCount(String folder);

    @Delete
    void deleteFolder(Folder folder);
}
