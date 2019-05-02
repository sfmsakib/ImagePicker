package com.sprout.imagepickersample;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sprout.imagepicker.PickImage;

public class ImageUpload extends AppCompatActivity {
    ImageView imageView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload);

         imageView = (ImageView) findViewById(R.id.image_view);
         button = (Button) findViewById(R.id.upload_button);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 AlertDialog.Builder builder1 = new AlertDialog.Builder(ImageUpload.this);
                 builder1.setTitle("Choose Photo")
                         .setItems(R.array.pick_array, new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 if(which==0){
                                     selectFromCamera();
                                 }
                                 if(which==1){
                                     selectFromGallery();
                                 }
                             }
                         });
                 AlertDialog alert11 = builder1.create();
                 alert11.show();
             }
         });

    }
    String tag = "ImagePicker";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CAMERA_PERMISSION){
            boolean CAMERA = false;
            for(int result:grantResults){
                if(result== PackageManager.PERMISSION_DENIED){
                    CAMERA = true;
                }
            }
            if(!CAMERA){
                Log.i(tag,"CAMERA:Permission allowed");
                openCamera();
            }else{
                Log.i(tag,"CAMERA:Permission Denied");
            }
        }
        if(requestCode==REQUEST_STORAGE_PERMISSION){
            boolean STORAGE = false;
            for(int result:grantResults){
                if(result== PackageManager.PERMISSION_DENIED){
                    STORAGE = true;
                }
            }
            if(!STORAGE){
                Log.i(tag,"STORAGE:Permission allowed");
                openGallery();
            }else{
                Log.i(tag,"STORAGE:Permission Denied");
            }
        }
    }
    static final int REQUEST_STORAGE_PERMISSION = 55;
    static final int REQUEST_CAMERA_PERMISSION = 66;

    private void selectFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                    getApplicationContext().getPackageManager().PERMISSION_GRANTED||
                    getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=
                            getApplicationContext().getPackageManager().PERMISSION_GRANTED){
                Log.i(tag,"Taking request...");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_STORAGE_PERMISSION);
                return;
            }
        }
        openGallery();
    }

    private void selectFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                    getApplicationContext().getPackageManager().PERMISSION_GRANTED||
                    getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA)!=
                            getApplicationContext().getPackageManager().PERMISSION_GRANTED){
                Log.i(tag,"Taking request...");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
                return;
            }
        }
        openCamera();
    }
    private void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAPTURE_CODE);
        }
    }
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_CODE);
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode ) {
            case REQUEST_PICK_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    // Get the Uri of the selected file
                    uri = data.getData();
                    Log.i(tag, "Image pick URI : "+uri);

                    startUpload(uri);

                }
                break;
            case REQUEST_CAPTURE_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    // Get the Uri of the selected file
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    uri = getImageUri(getApplicationContext(), imageBitmap);

                    Log.i(tag,"Image Capture URI : "+uri);

                    startUpload(uri);
                }

                break;
        }
    }
    static final int REQUEST_CAPTURE_CODE = 1;
    static final int REQUEST_PICK_CODE = 2;

    private Uri uri;

    private void startUpload(Uri uri) {

        try {
            if(uri==null){
                Log.i(tag,"URI NULL");
                return;

            }



            PickImage pickImage = new PickImage();
            pickImage.create(getApplicationContext());
            pickImage.compressImage(uri);
            pickImage.into(imageView);




        } catch (Exception e) {
            Log.i(tag,"URI Exception: "+e.getMessage());
        }
    }


}
