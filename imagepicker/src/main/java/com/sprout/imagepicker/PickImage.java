package com.sprout.imagepicker;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.sprout.imagepicker.logs.L;
import com.sprout.imagepicker.models.CustomizeImage;

import java.io.File;

public class PickImage {

    private Context mContext;
    public void create(Context context){

        mContext = context;
    }

    private File compressedImage;

    public void compressImage(Uri uri) {

        try {
            L.i("Inside start upload");
            if(uri==null){
                L.i("URI NULL");
                return;
            }
            compressedImage = new File(CustomizeImage.compressImage(uri, mContext));
            L.i("Found Image: " +(compressedImage!=null));


        } catch (Exception e) {
            L.i("URI Exception: "+e.getMessage());
        }
    }
    public void into(ImageView imageView){
        imageView.setImageURI(Uri.fromFile(compressedImage));
    }
}
