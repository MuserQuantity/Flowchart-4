package com.kyiv.flowchart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kyiv.flowchart.block.Rectangle;
import com.kyiv.flowchart.block.Rhomb;
import com.kyiv.flowchart.block.RoundRect;
import com.kyiv.flowchart.draw.DrawView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class MainActivity extends Activity {

    public enum Action{
        ADDRECT, ADDROUNDRECT, ADDRHOMB, DELETE, EDIT, MOVE, ADDLINK
    }
    public final static int DIALOG_OUT_POINT = 1;
    public final static int DIALOG_SAVE_FILE = 2;
    public static Action ACTION = Action.MOVE;
    private DrawView drawView;
    private String[] listOutPoints;
    private boolean isSaved = false;
    private String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        drawView = new DrawView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(drawView, layoutParams);
    }

    public void addRect(View view){
        ACTION = Action.ADDRECT;
    }

    public void addRoundRect(View view){
        ACTION = Action.ADDROUNDRECT;
    }

    public void addRhomb(View view){
        ACTION = Action.ADDRHOMB;
    }

    public void editBlock(View view){
        ACTION = Action.EDIT;
    }

    public void deleteBlock(View view){
        ACTION = Action.DELETE;
    }

    public void addLink(View view){
        drawView.showHint("Виберіть елемент 1");
        ACTION = Action.ADDLINK;
    }

    public void open(View view){
        /*Intent filePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        filePickerIntent.setType("file/*");
        startActivityForResult(filePickerIntent, 1);*/
        Intent intent = new Intent(this, OpenFileActivity.class);
        startActivity(intent);
    }

    public void save(View view){
        showDialog(DIALOG_SAVE_FILE);
    }

    void writeFile(String fileName) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(fileName, MODE_WORLD_WRITEABLE)));
            // пишем данные
            bw.write("Содержимое файла");
            // закрываем поток
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startDialog(String[] listOutPoints, int id){
        this.listOutPoints = listOutPoints;
        showDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    ListView lv = ((AlertDialog) dialog).getListView();
                    drawView.setLinkOutIndex(lv.getCheckedItemPosition());
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    drawView.deleteNewLink();
                    break;
            }
        }
    };

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {
            case DIALOG_OUT_POINT:

                adb.setTitle("Виберіть вихід:");
                adb.setSingleChoiceItems(listOutPoints, 0, myClickListener);
                adb.setCancelable(false);
                adb.setPositiveButton("OK", myClickListener);
                adb.setNegativeButton("Cancel", myClickListener);
                break;

            case DIALOG_SAVE_FILE:
                adb.setTitle("Зберегти файл?");
                final EditText etFileName = new EditText(MainActivity.this);
                etFileName.setHint("Назва файлу");
                if (fileName != null)
                    etFileName.setText(fileName);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                etFileName.setLayoutParams(lp);
                adb.setView(etFileName);
                adb.setPositiveButton("Зберегти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        writeFile(etFileName.getText().toString());
                        readFile(etFileName.getText().toString());
                    }
                });
                adb.setNegativeButton("Не зберігати", null);

        }
        return adb.create();
    }

    void readFile(String fileName) {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(fileName)));
            File s= getFilesDir();
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                Log.d("sssssssssssss", str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}