package com.inducesmile.backupdatabase.gDriveBackUp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Task;
import com.inducesmile.backupdatabase.MainActivity;
import com.inducesmile.backupdatabase.utils.Constants;
import com.inducesmile.backupdatabase.utils.DatabaseUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;


public class GDriveUpload {

    private static final String TAG = GDriveUpload.class.getSimpleName();

    private Context context;

    private DriveClient driveClient;

    private GoogleSignInClient googleSignInClient;



    public GDriveUpload(Context context) {
        this.context = context;
        googleSignInClient = buildGoogleSignInClient();
        ((MainActivity)context).startActivityForResult(googleSignInClient.getSignInIntent(), Constants.REQUEST_CODE_SIGN_IN);
    }


    // Google user account signin
    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .build();
        return GoogleSignIn.getClient(Objects.requireNonNull(context), signInOptions);
    }



    /** Create a new file and save it to Drive. */
    public void saveFileToDrive(DriveResourceClient driveResourceClient, File file) {
        // Start by creating a new contents, and setting a callback.
        Log.i(TAG, "Creating new contents.");

        driveResourceClient
                .createContents()
                .continueWithTask(task -> createFileIntentSender(task.getResult(), file))
                .addOnFailureListener(e -> Log.w(TAG, "Failed to create new contents.", e));
    }


    public Task<Void> createFileIntentSender(DriveContents driveContents, File file){

        Log.i(TAG, "New contents created.");
        // Get an output stream for the contents.
        OutputStream outputStream = driveContents.getOutputStream();
        // Write the bitmap data from it.
        ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
        //image.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);

        //***** file to binary here *******//
        try {
            outputStream.write(bitmapStream.toByteArray());
        } catch (IOException e) {
            Log.w(TAG, "Unable to write file contents.", e);
        }

        // Create the initial metadata - MIME type and title.
        // Note that the user will be able to change the title later.
        MetadataChangeSet metadataChangeSet =
                new MetadataChangeSet.Builder()
                        .setMimeType(DatabaseUtils.MIME_TYPE)
                        .setTitle(DatabaseUtils.DATABASE_TITLE)
                        .setStarred(true)
                        .build();

        // Set up options to configure and display the create file activity.
        CreateFileActivityOptions createFileActivityOptions =
                new CreateFileActivityOptions.Builder()
                        .setInitialMetadata(metadataChangeSet)
                        .setInitialDriveContents(driveContents)
                        .build();


        return driveClient
                .newCreateFileActivityIntentSender(createFileActivityOptions)
                .continueWith(task -> {((Activity)context).startIntentSenderForResult(task.getResult(), Constants.REQUEST_CODE_CREATOR, null, 0, 0, 0);
                    return null;
                });
    }
}
