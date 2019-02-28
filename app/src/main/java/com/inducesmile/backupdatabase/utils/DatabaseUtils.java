package com.inducesmile.backupdatabase.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class DatabaseUtils {

    private static final String TAG = DatabaseUtils.class.getSimpleName();

    private DatabaseUtils() {
        //cannot instantiate
    }

    public static final String DATABASE_NAME = "notes_db";
    public static final String MIME_TYPE = "application/x-sqlite-3";
    public static final String DATABASE_TITLE = "DB_BackUp";


    public static File getLocalDatabaseFile(Context context){
        Log.d(TAG, "Room database path " + context.getDatabasePath(DATABASE_NAME).getAbsolutePath());
        return context.getDatabasePath(DATABASE_NAME);
    }


}
