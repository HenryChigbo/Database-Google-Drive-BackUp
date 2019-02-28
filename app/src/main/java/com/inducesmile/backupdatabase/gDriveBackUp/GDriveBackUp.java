package com.inducesmile.backupdatabase.gDriveBackUp;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.inducesmile.backupdatabase.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class GDriveBackUp {

    private static final String TAG = GDriveBackUp.class.getSimpleName();

    private Context context;

    private GoogleSignInClient googleSignInClient;
    private DriveClient driveClient;
    private DriveResourceClient driveResourceClient;
    private TaskCompletionSource<DriveId> mOpenItemTaskSource;



    public GDriveBackUp(Context context) {
        this.context = context;
        googleSignInClient = buildGoogleSignInClient();
        ((Activity)context).startActivityForResult(googleSignInClient.getSignInIntent(), Constants.REQUEST_CODE_SIGN_IN);
    }


    // Google user account signin
    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .build();
        return GoogleSignIn.getClient(Objects.requireNonNull(context), signInOptions);
    }


    /**
     * Prompts the user to select a pdf file using OpenFileActivity.
     *
     * @return Task that resolves with the selected item's ID.
     */
    protected Task<DriveId> pickPdfFile(DriveClient driveClient) {
        OpenFileActivityOptions openOptions = new OpenFileActivityOptions.Builder()
                .setSelectionFilter(Filters.eq(SearchableField.MIME_TYPE, "application/pdf"))
                .setActivityTitle("Select Pdf file")
                .build();
        return pickItem(openOptions, driveClient);
    }


    /**
     * Prompts the user to select a folder using OpenFileActivity.
     *
     * @param openOptions Filter that should be applied to the selection
     * @return Task that resolves with the selected item's ID.
     */
    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions, DriveClient driveClient) {
        mOpenItemTaskSource = new TaskCompletionSource<>();
        driveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith((Continuation<IntentSender, Void>) task -> {
                    ((Activity)context).startIntentSenderForResult(task.getResult(), Constants.REQUEST_CODE_OPEN_ITEM, null, 0, 0, 0, null);
                    return null;
                });
        return mOpenItemTaskSource.getTask();
    }



    private void pickAndOpenSelectedFile(DriveResourceClient driveResourceClient){
        pickPdfFile(driveClient).addOnSuccessListener((Activity) Objects.requireNonNull(context), driveId -> {
            DriveFile file = driveId.asDriveFile();
            Log.d(TAG, "First ");
            readFileFromGoogleDrive(driveResourceClient, file);
            Log.d(TAG, "Second ");

        }).addOnFailureListener(((Activity)context), e -> {
            Log.e(TAG, "No file selected", e);
            Toast.makeText(context, "No Pdf file selected", Toast.LENGTH_LONG).show();
            ((Activity)context).finish();
        });
    }



    private void readFileFromGoogleDrive(DriveResourceClient driveResourceClient, DriveFile file) {
        Task<DriveContents> openFileTask = driveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);
        openFileTask
                .continueWithTask(task -> {
                    DriveContents contents = task.getResult();

                    InputStream inputStream = contents.getInputStream();
                    Log.d(TAG, "InputStream value " + inputStream);
                    byte[] bytes = IOUtils.toByteArray(inputStream);

                    // display selected pdf file from drive
                    //showPdf(bytes);

                    return driveResourceClient.discardContents(contents);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Unable to read contents", e);
                    Toast.makeText(context, "Pdf file reading failed", Toast.LENGTH_LONG).show();
                    ((Activity)context).finish();
                });
    }


    private void prepareAndReturnBackUpData(){

    }


    private void copyFile(String databaseName) {
        String backupDBPath = "photex_db.db";

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = context.getDatabasePath(databaseName).getAbsolutePath();


                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




//    private void showPdf(byte[] inputStream) {
//        pdfView.fromBytes(inputStream)
//                .enableDoubletap(true)
//                .enableSwipe(true)
//                .swipeHorizontal(true)
//                .spacing(2)
//                .onError(new OnErrorListener() {
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.d(TAG, "error:- " + t.getMessage());
//                        Toast.makeText(context, "Error when opening a file", Toast.LENGTH_LONG).show();
//                    }
//                }).load();
//    }



}
