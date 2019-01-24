package com.firstdemohh.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper
{

    Context con;
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        con = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String query = "create table authors(id INTEGER PRIMARY KEY AUTOINCREMENT , fnm VARCHAR , unm VARCHAR , pwd VARCHAR , sec_que VARCHAR , ans VARCHAR)";
        String query2 = "create table books(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER , bnm VARCHAR, bprice INTEGER , bdate VARCHAR , bimg BLOB )";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        String query = "drop table authors if exists";
        String query2 = "drop table books if exists";
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
        onCreate(sqLiteDatabase);
    }

    public Boolean insert(HashMap<String,String> record)
    {
        SQLiteDatabase db = getWritableDatabase();
        String fnm,unm,pwd,sec_que,ans;
        fnm = record.get("fnm");
        unm = record.get("unm");
        pwd = record.get("pwd");
        sec_que = record.get("sec_que");
        ans = record.get("ans");
        ContentValues cv = new ContentValues();
        cv.put("fnm",fnm);
        cv.put("unm",unm);
        cv.put("pwd",pwd);
        cv.put("sec_que",sec_que);
        cv.put("ans",ans);
        long result = db.insert("authors",null,cv);
        if(result == -1)
        {
            return false;
        }
        else
            return true;
    }


    public ArrayList<HashMap<String,Object>> getAllRecord( String unm)
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
        //ArrayList<Object> list = new ArrayList<Object>();
        String query2="select * from authors where unm = '"+ unm +"'";
        Cursor c1 = db.rawQuery(query2,null);
        Integer user_id=1;
        if(c1.moveToFirst())
        {
            do {
                user_id = c1.getInt(0);
            }while (c1.moveToNext());
        }
        String query = "select * from books where user_id = '"+ user_id + "'";
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst())
        {
            do {
               String bnm = c.getString(2);
               byte[] blob = c.getBlob(5);
               HashMap<String,Object> map = new HashMap<>();
               map.put("bnm",bnm);
               map.put("bimg",blob);
                list.add(map);
            }while (c.moveToNext());
        }

        return list;
    }

    public Boolean checkUser(HashMap<String, String> data)
    {
        SQLiteDatabase db =getReadableDatabase();
        String unm = data.get("unm");
        String pwd = data.get("pwd");
        String query = "select * from authors where unm = '" + unm + "' and pwd = '" + pwd + "'";
        Cursor c = db.rawQuery(query,null);
        String get_unm = "",get_pwd = "";
        if(c.moveToFirst())
        {
            do
            {
                get_unm = c.getString(2);
                get_pwd =c.getString(3);
            }while (c.moveToNext());
        }
        if(unm.equals(get_unm) && pwd.equals(get_pwd))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean insertBook(HashMap<String,Object> map, byte[] inputData)
    {
        SQLiteDatabase db = getWritableDatabase();
        String bnm = map.get("bnm").toString();
        Integer bprice = (Integer) map.get("bprice");
        String date = map.get("date").toString();
        String unm = map.get("unm").toString();
        String query="select id from authors where unm = '"+ unm + "'";
        Cursor c= db.rawQuery(query,null);
        Integer user_id=1;
        if(c.moveToFirst())
        {
            do
            {
                user_id = c.getInt(0);
            }while (c.moveToNext());
        }
        ContentValues cv =new ContentValues();
        cv.put("user_id",user_id);
        cv.put("bnm",bnm);
        cv.put("bprice",bprice);
        cv.put("bdate",date);
        cv.put("bimg",inputData);
        long result = db.insert("books",null,cv);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public HashMap<String,Object> getBook(String bnm)
    {
        SQLiteDatabase db = getReadableDatabase();
        HashMap<String,Object> record = new HashMap<>();
        String query = "select * from Books where bnm = '"+bnm+"'";
        Cursor c =db.rawQuery(query,null);
        if(c.moveToFirst())
        {
            do {
                Integer bprice = c.getInt(3);
                String bdate = c.getString(4);
                byte[] blob = c.getBlob(5);
                record.put("bprice",bprice);
                record.put("bdate",bdate);
                record.put("bimg",blob);
            }while (c.moveToNext());
        }
        return record;
    }

    public boolean del_book(String bnm)
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from books where bnm = '"+bnm+"'";
        Cursor c = db.rawQuery(query,null);
        Integer id=0;
        if (c.moveToFirst())
        {
            do {
                id = c.getInt(0);
            }while (c.moveToNext());
        }
        long result = db.delete("books","id = "+id,null);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean edit_book(HashMap<String, Object> book)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv=new ContentValues();
        String og_bnm = book.get("og_bnm").toString();
        byte[] inputdata = (byte[])book.get("bimg");
        String query = "select * from books where bnm = '"+og_bnm+"'";
        Cursor c = db.rawQuery(query,null);
        Integer id=0;
        if (c.moveToFirst())
        {
            do {
                id = c.getInt(0);
            }while (c.moveToNext());
        }
        String bnm = book.get("bnm").toString();
        String obj_bpice = book.get("bprice").toString();
        Integer bprice = Integer.parseInt(obj_bpice);
        cv.put("bnm",bnm);
        cv.put("bprice",bprice);
        cv.put("bimg",inputdata);
        long result = db.update("books",cv,"id="+ id,null);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public ArrayList<String> getAllBook(String unm)
    {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> list = new ArrayList<String>();
        String query2="select * from authors where unm = '"+ unm +"'";
        Cursor c1 = db.rawQuery(query2,null);
        Integer user_id=1;
        if(c1.moveToFirst())
        {
            do {
                user_id = c1.getInt(0);
            }while (c1.moveToNext());
        }
        String query = "select * from books where user_id = '"+ user_id + "'";
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst())
        {
            do {
                String bnm = c.getString(2);
                list.add(bnm);
            }while (c.moveToNext());
        }

        return list;
    }

    public ArrayList<HashMap<String,Object>> searcheBook(String unm, String book)
    {
        SQLiteDatabase db= getReadableDatabase();
        ArrayList<HashMap<String ,Object>> list =new ArrayList<>();
        String query2="select * from authors where unm = '"+ unm +"'";
        Cursor c1 = db.rawQuery(query2,null);
        Integer user_id=1;
        if(c1.moveToFirst())
        {
            do {
                user_id = c1.getInt(0);
            }while (c1.moveToNext());
        }
        String query = "select * from books where user_id = '"+user_id+"' and bnm like '%" + book + "%'";
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst())
        {
            do{
                String bnm = c.getString(2);
                byte[] blob = c.getBlob(5);
                HashMap<String,Object> map = new HashMap();
                      map.put("bnm", bnm);
                    map.put("bimg", blob);
                    list.add(map);

            }while (c.moveToNext());
        }
        return  list;
    }

    public ArrayList<HashMap<String,Object>> sortBook(String unm, String sort)
    {
        SQLiteDatabase db= getReadableDatabase();
        ArrayList<HashMap<String ,Object>> list =new ArrayList<>();
        String query2="select * from authors where unm = '"+ unm +"'";
        Cursor c1 = db.rawQuery(query2,null);
        Integer user_id=1;
        if(c1.moveToFirst())
        {
            do {
                user_id = c1.getInt(0);
            }while (c1.moveToNext());
        }
        String query ="";
        if (sort.equals("DESC")) {
            query = "select * from books ORDER BY bnm desc";
        }
        else
        {
            query = "select * from books ORDER BY bnm asc";
        }
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst())
        {
            do{
                String bnm = c.getString(2);
                byte[] blob = c.getBlob(5);
                HashMap<String,Object> map = new HashMap();
                map.put("bnm",bnm);
                map.put("bimg",blob);
                list.add(map);
            }while (c.moveToNext());
        }
        return  list;
    }
}