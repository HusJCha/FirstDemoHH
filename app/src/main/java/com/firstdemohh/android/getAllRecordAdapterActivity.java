package com.firstdemohh.android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class getAllRecordAdapterActivity extends BaseAdapter
{

    Context con;
    ArrayList<HashMap<String,Object>> list;
    LayoutInflater li;

    public getAllRecordAdapterActivity(Context context, ArrayList< HashMap<String,Object>> array )
    {
        con = context;
        list = array;
        li = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        convertView = li.inflate(R.layout.activity_get_all_record,null);
        TextView tv_edit = convertView.findViewById(R.id.ed_edit_book);
        TextView tv_delete = convertView.findViewById(R.id.ed_del_book);
        TextView tv_list = convertView.findViewById(R.id.tv_bnm);
        ImageView img_bimg = convertView.findViewById(R.id.bimg);
        HashMap<String,Object> map =new HashMap<>();
        map = list.get(position);
        final String bnm = map.get("bnm").toString();
        byte[] blob = (byte[])map.get("bimg");
        tv_list.setText(bnm);
        img_bimg.setImageBitmap(Utils.getImage(blob));
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookActivity.edit_click(bnm);
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookActivity.delete_click(bnm,position);
            }
        });
        return convertView;
    }
}
