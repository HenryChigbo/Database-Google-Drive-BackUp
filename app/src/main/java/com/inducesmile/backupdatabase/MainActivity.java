package com.inducesmile.backupdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.inducesmile.backupdatabase.gDriveBackUp.GDriveUpload;
import com.inducesmile.backupdatabase.utils.Constants;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DriveClient driveClient;
    private DriveResourceClient driveResourceClient;
    private TaskCompletionSource<DriveId> mOpenItemTaskSource;

    private GDriveUpload uploadData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("BackUp To GDriveUpload");

        Button backUpBtn = (Button)findViewById(R.id.backup_data);
        backUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData = new GDriveUpload(MainActivity.this);
            }
        });
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE_SIGN_IN:
                Log.i(TAG, "Sign in request code " + resultCode + " " + RESULT_OK);
                // Called after user is signed in.
                if (resultCode == RESULT_OK) {
                    Log.i(TAG, "Signed in successfully.");
                    // Use the last signed in account here since it already have a Drive scope.
                    driveClient = Drive.getDriveClient(MainActivity.this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(MainActivity.this)));
                    // Build a drive resource client.
                    driveResourceClient = Drive.getDriveResourceClient(MainActivity.this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(MainActivity.this)));

                    //open and read pdf file
                    //pickAndOpenSelectedFile(driveResourceClient);
                    //File databaseFile = DatabaseUtils.getLocalDatabaseFile(MainActivity.this);
                    uploadData.saveFileToDrive(driveResourceClient, null);

                }else{
                    Log.i(TAG, "Can't signin.");
                }
                break;

            case Constants.REQUEST_CODE_OPEN_ITEM:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = data.getParcelableExtra(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    Toast.makeText(MainActivity.this, "File created successfully", Toast.LENGTH_LONG).show();
                    mOpenItemTaskSource.setResult(driveId);

                } else {
                    Toast.makeText(MainActivity.this, "Unable to open file", Toast.LENGTH_LONG).show();
                    mOpenItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }
                break;
        }
    }

}
