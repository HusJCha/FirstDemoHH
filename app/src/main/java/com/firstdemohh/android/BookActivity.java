package com.firstdemohh.android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class BookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    static String bnm,unm;
    String sort = "DESC";
    static ListView lv_books;
    static DBHelper dbh;
    static ArrayList<HashMap<String,Object>> list;
    static  ArrayList<String> book_list;
    FloatingActionButton fab;
    Toolbar toolbar;
    TextView tv_edit,tv_delete,tv_bnm,tv_nav_unm;
    Button btn_search,btn_sort;
    FrameLayout footerLayout;
    EditText ed_search;
    LayoutInflater li;
    View convertView,nav_convertview;
    static Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        setSupportActionBar(toolbar);
        li = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.activity_get_all_record,null);
        nav_convertview = li.inflate(R.layout.nav_header_book,null);
        footerLayout = (FrameLayout)getLayoutInflater().inflate(R.layout.footer_layout,null);
        findviewbyid();
        unm=HomeActivity.UNM;
        tv_nav_unm.setText(unm);
        list = new ArrayList< HashMap<String,Object>>();
        con = BookActivity.this;
        dbh = new DBHelper(this,"Books",null,1);
        list = dbh.getAllRecord(unm);
        book_list = dbh.getAllBook(unm);
        lv_books.setAdapter(new getAllRecordAdapterActivity(this,list));
        lv_books.addFooterView(footerLayout);
        lv_books.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,int pos, long l)
            {
                bnm = book_list.get(pos).toString();
                Intent in =new Intent(BookActivity.this,BookDetailActivity.class);
                in.putExtra("status","open");
                in.putExtra("bnm",bnm);
                startActivity(in);
            }
        });
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i =new Intent(BookActivity.this,AddBookActivity.class);
                startActivity(i);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(ed_search.getVisibility() == View.GONE)
                {
                    ed_search.setVisibility(View.VISIBLE);
                    ed_search.findFocus();
                    btn_search.setVisibility(View.GONE);
                }
//                else
//                {
//                    String str_search = ed_search.getText().toString();
//                    if (str_search.equals(""))
//                    {
//                        onBackPressed();
//                    }
//                    else
//                    {
//                        Integer i = book_list.size();
//                        for(int j=0; j < i ; j++)
//                        {
//                            String book = book_list.get(j);
//                            if(book.equals(str_search))
//                            {
//                                list = dbh.searcheBook(unm,book);
//                                lv_books.setAdapter(new getAllRecordAdapterActivity(con,list));
//                            }
//                        }
//                    }
//                }
            }
        });
        btn_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sort.equals("DESC"))
                {
                    list = dbh.sortBook(unm,sort);
                    lv_books.setAdapter(new getAllRecordAdapterActivity(con,list));
                    sort = "ASC";
                }
                else
                {
                    list = dbh.sortBook(unm,sort);
                    lv_books.setAdapter(new getAllRecordAdapterActivity(con,list));
                    sort = "DESC";
                }
            }
        });
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                String str_search = ed_search.getText().toString();
                if(str_search.equals(""))
                {
                    list = dbh.getAllRecord(unm);
                    lv_books.setAdapter(new getAllRecordAdapterActivity(con, list));
                }
                else {
                    Integer in = str_search.length();
                    for (int j = 0; j < in; j++)
                    {
                        list = dbh.searcheBook(unm, str_search);
                        ArrayList<HashMap<String,Object>> list1 = new ArrayList<>();
                        HashMap<String,Object> map =new HashMap<>();
                        for(int c = 0 ; c<list.size();c++)
                        {
                            map = list.get(c);
                            String bnm = map.get("bnm").toString();
                                byte[] blob = (byte[]) map.get("bimg");
                                list1.add(map);
                                btn_sort.setVisibility(View.GONE);
                        }
                        lv_books.setAdapter(new getAllRecordAdapterActivity(con, list1));

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public static void edit_click(String bnm)
    {
        Intent i =new Intent(con,BookDetailActivity.class);
        i.putExtra("status","edit");
        i.putExtra("bnm",bnm);
        con.startActivity(i);
    }

    public static void delete_click(final String bnm, final int position)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(con);
        dialog.setTitle("Are you Sure ??");
        dialog.setMessage("Are you Sure to delete this Book ??");
        dialog.setIcon(R.mipmap.ic_launcher_round);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = dbh.del_book(bnm);
                if (result == true)
                {
                    list = dbh.getAllRecord(unm);
                    lv_books.setAdapter(new getAllRecordAdapterActivity(con,list));
                    Toast.makeText(con,"Book Deleted",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(con,"Something is Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void findviewbyid()
    {
        toolbar = findViewById(R.id.toolbar);
        fab =  findViewById(R.id.fab);
        lv_books = findViewById(R.id.lv_books);
        tv_edit = convertView.findViewById(R.id.ed_edit_book);
        tv_delete = convertView.findViewById(R.id.ed_del_book);
        tv_bnm = convertView.findViewById(R.id.tv_bnm);
        tv_nav_unm = nav_convertview.findViewById(R.id.nav_unm);
        btn_search = findViewById(R.id.btn_search);
        ed_search = findViewById(R.id.ed_search);
        btn_sort = footerLayout.findViewById(R.id.btn_sort);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (ed_search.getVisibility() == View.VISIBLE)
        {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) con.getSystemService(BookActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                ed_search.setVisibility(View.GONE);
                btn_search.setVisibility(View.VISIBLE);
                btn_sort.setVisibility(View.VISIBLE);
                list = dbh.getAllRecord(unm);
                lv_books.setAdapter(new getAllRecordAdapterActivity(con,list));

        }
        else if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
            {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_logout)
        {
            SharedPreferences sharedpreferences = getSharedPreferences(HomeActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            Intent i =new Intent(BookActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
