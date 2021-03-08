package com.example.temanjalan.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.temanjalan.R;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class VerifyActivity extends AppCompatActivity implements IPickResult {
    private ImageView iv_Img;
    private Bitmap mSelectedImage;
    private String mSelectedImagePath ;
    private File mSelectedFileBiaya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        binding();

        //nek upload server file e mSelectedFileBiaya;
        iv_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(getSupportFragmentManager());
            }
        });

    }

    private void binding(){
        iv_Img = findViewById(R.id.iv_Img);
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            try {
                File fileku = new Compressor(this)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(new File(r.getPath()));

                mSelectedImagePath = fileku.getAbsolutePath();
                mSelectedFileBiaya = new File(mSelectedImagePath);
                mSelectedImage = r.getBitmap();
                iv_Img.setImageBitmap(mSelectedImage);

                Log.d("RBA", "onPickResult: "+ mSelectedImagePath);

                //selectedImageFile = new File(r.getPath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}