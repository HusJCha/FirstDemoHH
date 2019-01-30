package com.firstdemohh.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class getAllRecordAdapterActivity extends RecyclerView.Adapter<getAllRecordAdapterActivity.MyViewHolder>
{
    Context con;
    ArrayList<HashMap<String,Object>> list = new ArrayList<>();

    public getAllRecordAdapterActivity(Context context, ArrayList< HashMap<String,Object>> array )
    {
        con = context;
        list = array;
    }
    @Override
    public getAllRecordAdapterActivity.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType)
    {
        View itemview = LayoutInflater.from (parent.getContext()).inflate(R.layout.activity_get_all_record,null);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull getAllRecordAdapterActivity.MyViewHolder holder, final int position) {
        HashMap<String,Object> map = list.get(position);
        final String bnm = map.get("bnm").toString();
        byte[] blob = (byte[])map.get("bimg");
        holder.tv_bnm.setText(bnm);
        holder.tv_bnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookActivity.open_list(position);
            }
        });
        holder.img_bimg.setImageBitmap(Utils.getImage(blob));
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookActivity.edit_click(bnm);
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookActivity.delete_click(bnm,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_bimg;
        TextView tv_bnm,tv_edit,tv_delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            img_bimg= itemView.findViewById(R.id.bimg);
            tv_bnm = itemView.findViewById(R.id.tv_bnm);
            tv_edit = itemView.findViewById(R.id.ed_edit_book);
            tv_delete = itemView.findViewById(R.id.ed_del_book);
        }
    }
}
