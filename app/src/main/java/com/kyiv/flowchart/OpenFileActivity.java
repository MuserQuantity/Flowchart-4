package com.kyiv.flowchart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenFileActivity extends Activity {
    ListView list_dir;
    ListView list_files;
    TextView textPath;
    Context _context;
    int select_id_list = -1;
    String path = "/";

    ArrayList<String> ArrayDir = new ArrayList<String>();
    List<String> ArrayFiles = new ArrayList<>();
    ArrayAdapter<String> adapterDir;
    ArrayAdapter<String> adapterFiles;

    protected void onCreate(Bundle savedInstanceState) {
        _context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_file_layout);
        path = getFilesDir().getAbsolutePath();

        list_dir = (ListView) findViewById(R.id.list_dir);
        list_files = (ListView) findViewById(R.id.list_files);
        textPath = (TextView) findViewById(R.id.textPath);

        adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayDir);
        list_dir.setAdapter(adapterDir);
        adapterFiles = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ArrayFiles);
        list_files.setAdapter(adapterFiles);

        update_list_dir();

        list_dir.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_id_list = (int) id;
                update_list_dir();
            }
        });
        list_files.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("url", path + "/" + ArrayFiles.get((int)id));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    public void onClickBack(View view) {
        path = (new File(path)).getParent() + "/";
        update_list_dir();
    }

    public void onClickGo(View view) {
        Intent intent = new Intent();
        intent.putExtra("url", path);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void update_list_dir() {
        if (select_id_list != -1) path = path + ArrayDir.get(select_id_list) + "/";
        select_id_list = -1;
        ArrayDir.clear();
        ArrayFiles.clear();
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File aFile : files) {
                if (aFile.isDirectory())
                    ArrayDir.add(aFile.getName());
                else if (aFile.isFile())
                    ArrayFiles.add(aFile.getName());
            }
        }

        adapterDir.notifyDataSetChanged();
        adapterFiles.notifyDataSetChanged();
        textPath.setText(path);
    }


}