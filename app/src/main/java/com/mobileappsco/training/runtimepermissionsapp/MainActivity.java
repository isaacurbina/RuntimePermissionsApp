package com.mobileappsco.training.runtimepermissionsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;

public class MainActivity extends AppCompatActivity implements OnPermissionCallback {

    final int MY_PERMISSIONS_REQUEST_1 = 1;
    final int MY_PERMISSIONS_REQUEST_2 = 2;
    final String MY_PERMISSION_TEXT_1 = Manifest.permission.CAMERA;
    final String MY_PERMISSION_TEXT_2 = Manifest.permission.READ_CONTACTS;
    final int MY_PERMISSION_CODE_1 = 1;
    final int MY_PERMISSIONS_CODE_2 = 2;
    ImageView imageView;
    PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionHelper = PermissionHelper.getInstance(this);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSION_CODE_1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void takePhoto(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                MY_PERMISSION_TEXT_1);

        String message = (permissionCheck == 0) ? "Permission Check: Granted" : "Permission Check: Not Granted";

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d("MYTAG", message);
        // -1 No permission
        if (permissionCheck == 0) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, MY_PERMISSION_CODE_1);
        } else {
            permissionHelper.setForceAccepting(false)
                    //.requestAfterExplanation(MY_PERMISSION_TEXT_1);
                    .request(MY_PERMISSION_TEXT_1);
        }
    }

    // PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("MYTAG", "onRequestPermissionsResult "+requestCode);
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_CODE_1 && grantResults[0] == 0) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, MY_PERMISSION_CODE_1);
        }
    }

    @Override
    public void onPermissionGranted(String[] permissionName) {
        Log.d("MYTAG", "onPermissionGranted "+permissionName.toString());
    }

    @Override
    public void onPermissionDeclined(String[] permissionName) {
        Log.d("MYTAG", "onPermissionDeclined " + permissionName.toString());
    }

    @Override
    public void onPermissionPreGranted(String permissionName) {
        Log.d("MYTAG", "onPermissionPreGranted " + permissionName);
    }

    @Override
    public void onPermissionNeedExplanation(String permissionName) {
        Log.d("MYTAG", "onPermissionNeedExplanation " + permissionName);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Grant me access")
                .setMessage("Do I really need to explain you why?, just click on 'Allow'")
                .setPositiveButton("Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionHelper.requestAfterExplanation(MY_PERMISSION_TEXT_1);
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onPermissionReallyDeclined(String permissionName) {
        Log.d("MYTAG", "onPermissionReallyDeclined " + permissionName);
    }

    @Override
    public void onNoPermissionNeeded() {
        Log.d("MYTAG", "onNoPermissionNeeded");
    }

    public void readContacts(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                MY_PERMISSION_TEXT_2);

        String message = (permissionCheck == 0) ? "Permission Check: Granted" : "Permission Check: Not Granted";

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.d("MYTAG", message);
        // -1 No permission
        if (permissionCheck == 0) {
            Toast.makeText(this, "Read contacts goes here", Toast.LENGTH_SHORT).show();
        } else {
            permissionHelper.setForceAccepting(false)
                    //.requestAfterExplanation(MY_PERMISSION_TEXT_1);
                    .request(MY_PERMISSION_TEXT_2);
        }
    }
}
