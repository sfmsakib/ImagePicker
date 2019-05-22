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
            L.i("PickImage:Inside start upload");
            if(uri==null){
                L.i("PickImage:URI NULL");
                return;
            }
            L.i("PickImage:URI: "+uri);

            compressedImage = new File(CustomizeImage.compressImage(uri, mContext));
            L.i("PickImage:Found Image: " +(compressedImage!=null));


        } catch (Exception e) {
            L.i("PickImage:URI Exception: "+e.getMessage());
        }
    }
    public void into(ImageView imageView){
        imageView.setImageURI(Uri.fromFile(compressedImage));
    }

    public File getCompressedFile(){
        L.i("PickImage: compressed file:"+compressedImage);
        return compressedImage;
    }



}
