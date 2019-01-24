package com.firstdemohh.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
{
    EditText ed_unm,ed_pwd;
    Button btn_log;
    TextView tv_reg;
    DBHelper dbh;
    HashMap<String,String> map;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static String UNM = "";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findviewbyid();
        map = new HashMap<String, String>();
        dbh = new DBHelper(this,"Books",null,1);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains("unm")) {
            Intent i = new Intent(HomeActivity.this, BookActivity.class);
            startActivity(i);
            finish();
        } else {
            btn_log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String unm = ed_unm.getText().toString();
                    String pwd = ed_pwd.getText().toString();
                    if (unm.equals("") && pwd.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter Credetials", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                            HashMap<String,String> data = new HashMap<String, String>();
                            data.put("unm",unm);
                            data.put("pwd",pwd);
                            Boolean result = dbh.checkUser(data);
                            if(result == true)
                            {
                                UNM = unm;
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("unm", unm);
                                editor.commit();
                                Intent i = new Intent(HomeActivity.this, BookActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Entered Credentials are wrong",Toast.LENGTH_SHORT).show();
                                ed_unm.setText("");
                                ed_pwd.setText("");
                                ed_unm.requestFocus();
                            }
                    }
                }
            });
            tv_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(HomeActivity.this, RegisterActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    private void findviewbyid()
    {
        ed_unm =  findViewById(R.id.ed_unm);
        ed_pwd = findViewById(R.id.ed_pwd);
        btn_log = findViewById(R.id.btn_log);
        tv_reg =  findViewById(R.id.tv_reg);
    }

}
