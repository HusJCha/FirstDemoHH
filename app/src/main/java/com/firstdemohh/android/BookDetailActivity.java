package com.firstdemohh.android;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import java.io.ByteArrayOutputStream;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class BookDetailActivity extends AppCompatActivity
{
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "StoreImageActivity";
    byte[] bimg_blol = {};
    private Uri selectedImageUri;
    TextView tv_bnm,tv_bprice,tv_bdate,tv_bdate_title;
    EditText ed_bnm,ed_bprice,ed_bdate;
    Button btn_update;
    DBHelper dbh;
    String og_bnm;
    ImageView bimg,edit_bimg;
    HashMap<String,Object> book_record;
    byte[] blob;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        findviewbyid();
        selectedImageUri = null;
        dbh = new DBHelper(this,"Books",null,1);
        String status = getIntent().getStringExtra("status");
        String bnm = getIntent().getStringExtra("bnm");
        book_record = new HashMap<String, Object>();
        if(status.equals("open"))
        {
            tv_bnm.setVisibility(View.VISIBLE);
            tv_bprice.setVisibility(View.VISIBLE);
            tv_bdate.setVisibility(View.VISIBLE);
            tv_bnm.setText(bnm);
            book_record = dbh.getBook(bnm);
            Object bprice = book_record.get("bprice");
            Object bdate = book_record.get("bdate");
            String str_bprice = bprice.toString();
            String str_bdate = bdate.toString();
            blob = (byte[]) book_record.get("bimg");
            tv_bprice.setText(str_bprice);
            tv_bdate.setText(str_bdate);
            bimg.setImageBitmap(Utils.getImage(blob));
        }
        else if(status.equals("edit"))
        {
            ed_bnm.setVisibility(View.VISIBLE);
            ed_bprice.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.VISIBLE);
            tv_bdate_title.setVisibility(View.GONE);
            ed_bnm.setText(bnm);
            og_bnm=ed_bnm.getText().toString();
            book_record = dbh.getBook(bnm);
            Object bprice = book_record.get("bprice");
            String str_bprice = bprice.toString();
            byte[] blob = (byte[]) book_record.get("bimg");
            ed_bprice.setText(str_bprice);
            bimg.setImageBitmap(Utils.getImage(blob));
            bimg_blol = blob;
        }
        edit_bimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(BookDetailActivity.this);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String bnm,bprice;
                bnm = ed_bnm.getText().toString();
                bprice = ed_bprice.getText().toString();
                byte[] inputData = {};
                if(null == selectedImageUri)
                {
                    inputData = bimg_blol;
                }
                else {
                    InputStream iStream = null;
                    try {
                        iStream = getContentResolver().openInputStream(selectedImageUri);
                        inputData = Utils.getBytes(iStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                HashMap<String,Object> book = new HashMap<>();
                book.put("og_bnm",og_bnm);
                book.put("bnm",bnm);
                book.put("bprice",bprice);
                book.put("bimg",inputData);
                boolean result = dbh.edit_book(book);
                if (result == true)
                {
                    Intent i =new Intent(BookDetailActivity.this,BookActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i =new Intent(BookDetailActivity.this,BookActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(getApplicationContext(),"Something is Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(BookDetailActivity.this,R.style.DBTheme);
                dialog.setContentView(R.layout.image_layout);
                ImageView dialog_img = dialog.findViewById(R.id.dialog_img);
                Button btn_cls = dialog.findViewById(R.id.btn_cls);
                dialog_img.setImageBitmap(Utils.getImage(blob));
                btn_cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            selectedImageUri = result.getUri();
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),selectedImageUri.toString(),Toast.LENGTH_SHORT).show();
                bimg.setImageURI(selectedImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void findviewbyid()
    {
        tv_bnm = findViewById(R.id.tv_bnm);
        tv_bprice = findViewById(R.id.tv_bprice);
        tv_bdate = findViewById(R.id.tv_bdate);
        tv_bdate_title = findViewById(R.id.tv_bdate_title);
        ed_bnm = findViewById(R.id.ed_bnm);
        ed_bprice = findViewById(R.id.ed_bprice);
        ed_bdate = findViewById(R.id.ed_bdate);
        btn_update = findViewById(R.id.btn_update);
        bimg = findViewById(R.id.img_bimg);
        edit_bimg = findViewById(R.id.bimg_edit);
    }
}
