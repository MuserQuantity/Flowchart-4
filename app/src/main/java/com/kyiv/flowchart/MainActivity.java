package com.kyiv.flowchart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kyiv.flowchart.block.Model;
import com.kyiv.flowchart.draw.DrawFlowchart;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class MainActivity extends Activity {

    public enum Action{
        ADDRECT, ADDROUNDRECT, ADDRHOMB, DELETE, EDIT, MOVE, ADDLINK
    }
    public final static int DIALOG_OUT_POINT = 1;
    public final static int DIALOG_SAVE_FILE = 2;
    public final static int EDIT_BLOCK = 3;
    public static Action ACTION = Action.MOVE;
    private DrawFlowchart drawFlowchart;
    private String[] listOutPoints;
    private String fileName;
    private String filePath;
    private static final int OPENFILE = 1;
    private Model model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame);
        model = new Model();
        drawFlowchart = new DrawFlowchart(this, model);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.addView(drawFlowchart, layoutParams);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.newButton:
                if(checkSaved())
                    close();
                break;
            case R.id.openButton:
                if(checkSaved()) {
                    Intent intent = new Intent(this, OpenFileActivity.class);
                    startActivityForResult(intent, OPENFILE);
                }
                break;
            case R.id.saveButton:
                if (fileName == null | filePath == null)
                    showDialog(DIALOG_SAVE_FILE);
                else {
                    writeFile(filePath + "/" + fileName);
                    model.changeWasSaved();
                }
                break;
            case R.id.graphButton:
                if(!model.findWarnings()) {
                    Intent intent = new Intent(this, GraphActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("model", model);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                    Toast.makeText(this, "В блок-схемі виявлені помилки. Виправте їх спочатку", Toast.LENGTH_SHORT).show();
                break;
            case R.id.closeButton:
                if(checkSaved())
                    close();
                break;
            case R.id.rectButton:
                ACTION = Action.ADDRECT;
                break;
            case R.id.roundrectButton:
                ACTION = Action.ADDROUNDRECT;
                break;
            case R.id.rhombButton:
                ACTION = Action.ADDRHOMB;
                break;
            case R.id.deleteButton:
                ACTION = Action.DELETE;
                break;
            case R.id.editButton:
                ACTION = Action.EDIT;
                break;
            case R.id.linkButton:
                drawFlowchart.showHint("Виберіть елемент 1");
                ACTION = Action.ADDLINK;
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (requestCode){
            case OPENFILE:
                if (resultCode == RESULT_OK){
                    close();
                    String url = intent.getStringExtra("url");
                    readFile(this, url);
                }
                break;
            case EDIT_BLOCK:
                if (resultCode == RESULT_OK){
                    drawFlowchart.saveChangeEditBlock(intent.getStringExtra("text"), intent.getIntExtra("text_size", 20), intent.getStringExtra("nameNode"));
                }
                break;
        }
    }

    void writeFile(String fileName){
        ModelXML modelToXML = new ModelXML(this);
        try {
            modelToXML.writeXML(model, fileName);
            Toast.makeText(this, "Файл " + (new File(fileName)).getName() + " успішно збережено", Toast.LENGTH_SHORT).show();
        } catch (TransformerException | IOException e) {
            Toast.makeText(this, "Помилка запису до файлу " + (new File(fileName)).getName(), Toast.LENGTH_SHORT).show();
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
                    drawFlowchart.setLinkOutIndex(lv.getCheckedItemPosition());
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    drawFlowchart.deleteNewLink();
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
                        if (filePath == null || fileName == null)
                            writeFile(getFilesDir() + "/" + etFileName.getText().toString());
                        else
                            writeFile(filePath + "/" + etFileName.getText().toString());
                        model.changeWasSaved();
                    }
                });
                adb.setNegativeButton("Не зберігати", null);

        }
        return adb.create();
    }

    public void readFileInNewThread(final Context context, final String fileName){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    ModelXML modelXML = new ModelXML(context);
                try {
                    model = modelXML.readXML(new FileInputStream(fileName));
                    model.changeWasSaved();
                    drawFlowchart.setModel(model);
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    boolean readFile(Context context, String fileName) {
        readFileInNewThread(context, fileName);

        return false;
    }

    public void close(){
        model = new Model();
        drawFlowchart.setModel(model);
        fileName = null;
        filePath = null;
    }

    public boolean checkSaved(){
        if(!model.isChanged())
            return true;
        showDialog(DIALOG_SAVE_FILE);
        return false;
    }
}