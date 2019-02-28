package com.inducesmile.backupdatabase.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note implements Parcelable {

    public static final Creator<Note> CREATOR = new Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String note;
    private String createdAt;
    private String folder;
    private String remindAt;
    private int starred;
    private int trashed;
    private int done;

    @Ignore
    public Note(String title, String note, String createdAt, String folder, String remindAt,
                int starred, int trashed, int done) {
        this.title = title;
        this.note = note;
        this.createdAt = createdAt;
        this.folder = folder;
        this.remindAt = remindAt;
        this.starred = starred;
        this.trashed = trashed;
        this.done = done;
    }

    public Note(int id, String title, String note, String createdAt, String folder, String remindAt,
                int starred, int trashed, int done) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.createdAt = createdAt;
        this.folder = folder;
        this.remindAt = remindAt;
        this.starred = starred;
        this.trashed = trashed;
        this.done = done;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        note = in.readString();
        createdAt = in.readString();
        folder = in.readString();
        remindAt = in.readString();
        starred = in.readInt();
        trashed = in.readInt();
        done = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(String remindAt) {
        this.remindAt = remindAt;
    }

    public int getStarred() {
        return starred;
    }

    public void setStarred(int starred) {
        this.starred = starred;
    }

    public int getTrashed() {
        return trashed;
    }

    public void setTrashed(int trashed) {
        this.trashed = trashed;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(note);
        parcel.writeString(createdAt);
        parcel.writeString(folder);
        parcel.writeString(remindAt);
        parcel.writeInt(starred);
        parcel.writeInt(trashed);
        parcel.writeInt(done);
    }
}
