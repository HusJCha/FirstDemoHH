package com.firstdemohh.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    EditText ed_fnm,ed_unm,ed_pwd,ed_retype_pwd,ed_ans;
    Button btn_reg;
    TextView tv_log;
    Spinner sec_que;
    ArrayAdapter<CharSequence> adapter;
    DBHelper dbh;
    HashMap<String,String> record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findviewbyid();
        adapter = ArrayAdapter.createFromResource(this, R.array.sec_que, android.R.layout.simple_spinner_dropdown_item);
        sec_que.setAdapter(adapter);
        tv_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fnm,unm,pwd,retype_pwd,sec,ans;
                fnm = ed_fnm.getText().toString();
                unm = ed_unm.getText().toString();
                pwd = ed_pwd.getText().toString();
                retype_pwd = ed_retype_pwd.getText().toString();
                sec = sec_que.getSelectedItem().toString();
                ans = ed_ans.getText().toString();
                if(pwd.equals(retype_pwd))
                {
                    if (fnm.equals("") && unm.equals("") && pwd.equals("") && retype_pwd.equals("") && ans.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please Enter Credentials..",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        record =new HashMap<String, String>();
                        record.put("fnm",fnm);
                        record.put("unm",unm);
                        record.put("pwd",pwd);
                        record.put("ans",ans);
                        Boolean result = dbh.insert(record);
                        if(result==true)
                        {
                            Intent i = new Intent(RegisterActivity.this,HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Something is Wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Password is not equal",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findviewbyid()
    {
        dbh = new DBHelper(getApplicationContext(),"Books",null ,1);
        sec_que = (Spinner)findViewById(R.id.sec_que);
        ed_fnm = (EditText)findViewById(R.id.ed_fnm);
        ed_unm = (EditText)findViewById(R.id.ed_unm);
        ed_pwd = (EditText)findViewById(R.id.ed_pwd);
        ed_retype_pwd = (EditText)findViewById(R.id.ed_retype_pwd);
        ed_ans = (EditText)findViewById(R.id.ed_ans);
        btn_reg = (Button)findViewById(R.id.btn_reg);
        tv_log = (TextView)findViewById(R.id.tv_reg);
    }
}
