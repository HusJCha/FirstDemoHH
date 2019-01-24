package com.firstdemohh.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddBookActivity extends AppCompatActivity
{
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "StoreImageActivity";
    private Uri selectedImageUri;
    ImageView img_bimg;
    Button btn_add_book,btn_add_img;
    EditText ed_bnm,ed_bprice;
    DBHelper dbh;
    Integer i =0;
    BookDetails book;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        findviewbyid();
        dbh= new DBHelper(this,"Books",null,1);
        btn_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddBookActivity.this);
                }
        });

        btn_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bnm = ed_bnm.getText().toString();
                String bprice =ed_bprice.getText().toString();
                byte[] inputData = {};
                if(bnm.equals("") && bprice.equals("") && i == 0)
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Integer int_bprice = Integer.parseInt(bprice);
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(selectedImageUri);
                        inputData = Utils.getBytes(iStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String date = DateFormat.getDateInstance().format(new Date());
                    HashMap<String,Object> map =new HashMap<String, Object>();
                    map.put("bnm",bnm);
                    map.put("bprice",int_bprice);
                    map.put("date",date);
                    map.put("unm",HomeActivity.UNM);
                    Boolean result = dbh.insertBook(map,inputData);
                    if(result == true)
                    {
                        Intent i =new Intent(AddBookActivity.this,BookActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Someting is Wrong!",Toast.LENGTH_SHORT).show();
                        ed_bnm.setText("");
                        ed_bprice.setText("");
                        ed_bnm.requestFocus();
                    }
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                selectedImageUri = result.getUri();
                img_bimg.setImageURI(selectedImageUri);
                i = 1;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }
    private void findviewbyid()
    {
        btn_add_book = findViewById(R.id.btn_add_book);
        ed_bnm =findViewById(R.id.ed_bnm);
        ed_bprice = findViewById(R.id.ed_bprice);
        btn_add_img = findViewById(R.id.btn_img);
        img_bimg = findViewById(R.id.img_show);
    }
}
