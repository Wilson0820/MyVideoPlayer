package com.fxc.myvideoplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String path= Environment.getExternalStorageDirectory().getPath();
    private ArrayList<HashMap<String,String>> name;
    private ArrayList<String> moviepathlist;
    private ListView lv;
    private String moviepath;
    Toolbar mToolbar;
    int filesum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_normal);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"backtolastview",Toast.LENGTH_SHORT).show();
            }
        });
        lv = (ListView) findViewById(R.id.lv);
        name = new ArrayList<HashMap<String,String>>();
        moviepathlist = new ArrayList<String>();
        vedio();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                Toast.makeText(MainActivity.this,"search",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void click(View v){
        updateView();
    }

    private void updateView() {
        name.clear();
        getFileName(path);
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), name, R.layout.list_id, new String[]{"視頻名稱"}, new int[]{R.id.mp4});
        lv.setAdapter(adapter);

    }

    private void vedio() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            System.out.println("path---->>"+ path);
            getFileName(path);
        }
        Log.i("Log", "------>end to getFileName");
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, name, R.layout.list_id, new String[]{"視頻名稱"}, new int[]{R.id.mp4});
        Log.i("Log", "------>adapter error");
        lv.setAdapter(adapter);
        for (int i = 0; i < name.size(); i++) {
            Log.i("Log", "list.name:"+ name.get(i));
        }
        filesum =name.size();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String moviename = moviepathlist.get(position);
                Intent intent =new Intent(MainActivity.this, MovieActivity.class);
                intent.putExtra("moviename", moviename);
                intent.putExtra("filesum",filesum);
                intent.putExtra("fileno",(position+1));
                Bundle bundle = new Bundle();
                bundle.putSerializable("Arraylist",moviepathlist);
                intent.putExtra("moviepathlist",bundle);
                startActivity(intent);

            }
        });
    }


    private void getFileName(String path2) {
        File files = new File(path2);
        File[] file = files.listFiles();
        Log.i(path2, "---->start to getFileName");
        for (File f : file) {
		/*	if (f.isDirectory()) {
				getFileName(f.getAbsolutePath());
				Log.i(path2, "---->getFileName is empty");
			} else*/ {
                String fileName =f.getName();
                if (fileName.endsWith(".mp4")||fileName.endsWith(".3gp")) {
                    HashMap<String,String> map = new HashMap<String,String>();
                    //String s = fileName.substring(0, fileName.lastIndexOf("."));
                    moviepath = f.getPath();
                    System.out.println("fileName------"+ fileName);
                    Log.i(path2, "---->getFileName is fffffff");
                    map.put("視頻名稱", fileName);
                    moviepathlist.add(moviepath);
                    name.add(map);
                }
            }
            Log.i(path2, "---->success to getFileName");
        }

    }



}
