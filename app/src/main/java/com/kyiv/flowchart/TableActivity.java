package com.kyiv.flowchart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kyiv.flowchart.graph.GraphModel;
import com.kyiv.flowchart.graph.LinkGraph;
import com.kyiv.flowchart.graph.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private TextView functionsTV;
    private Table table;
    private String fileName = null;
    private static final int OPENFILE = 0;
    private static final int DIALOG_SAVEFILE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        functionsTV = (TextView) findViewById(R.id.functionsTV);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        GraphModel graphModel = (GraphModel) bundle.get("graphModel");
        table = new Table();
        table.createTable(graphModel, tableLayout, this);
        for (TableRow row : table.getRows())
            tableLayout.addView(row);
        List<Function> functions = table.getFunctions();
        String output = "До мінімізації:\n";
        for (Function function : functions)
            output += function.getName() + " = " + function.getStringFunction() + "\n";

        output += "Після мінімізації:\n";
        String allFunctions = "";
        for (Function function : functions) {
            output += function.getName() + " = " + Kwaine_Minimization.minimize(function.getStringFunction()) + "\n";
            allFunctions += getFunctionInTextForm(function.getName(), Kwaine_Minimization.minimize(function.getStringFunction()), function.getTermTemplate()) + ";\n";
        }
        output += "Після перетворення:\n";
        BooleanFunctionNotAndOr booleanFunctionNotAndOr = new BooleanFunctionNotAndOr(allFunctions);
        List<String> funcs = booleanFunctionNotAndOr.functions;
        for (String f : funcs)
            output += f + '\n';
        functionsTV.setText(output);
        try{
            PrintWriter writer = new PrintWriter(Environment.getExternalStorageDirectory().getPath() + "/flowchart.vhdl", "UTF-8");
            writer.println(booleanFunctionNotAndOr.generateVHDL());
            writer.close();
        } catch (IOException e) {
            // do something
            Log.d("sfa", "sfe");
        }
    }



    private String getFunctionInTextForm(String name, String function, String[] terms){
        String[] implicantes = function.split("v");
        String resFunction = name + " = (";
        for (String implicante : implicantes){
            for (int i = 0; i < implicante.length(); i++){
                if (implicante.charAt(i) == '1')
                    resFunction += terms[i] + Kwaine_Minimization.AND_CHAR;
                else if(implicante.charAt(i) == '0'){
                    resFunction += "not(" + terms[i] + ")" + Kwaine_Minimization.AND_CHAR;
                }
            }
            resFunction = resFunction.substring(0, resFunction.length() - 1);
            resFunction += Kwaine_Minimization.V_CHAR;
        }
        resFunction = resFunction.substring(0, resFunction.length() - 1) + ")";
        return resFunction;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case OPENFILE:
                if (resultCode == RESULT_OK){
                    String filePath = data.getStringExtra("filePath");
                    try {
                        loadFromFile(filePath);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void loadFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        fileName = file.getName();
        FileInputStream fis = openFileInput(fileName);
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(fis);
            table = (Table) is.readObject();
            tableLayout.removeAllViews();
            for (TableRow row : table.getRows())
                tableLayout.addView(row);
            is.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.openButton:
                Intent intent = new Intent(this, OpenFileActivity.class);
                intent.putExtra("nameFilter", ".table");
                startActivityForResult(intent, OPENFILE);
                break;
            case R.id.saveButton:
                if (fileName != null) {
                    saveInFile(fileName);
                    showMessage("Файл " + fileName + " успіщно збережено");
                }
                else{
                    showDialog(DIALOG_SAVEFILE);
                }
                break;
        }
    }

    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {

            case DIALOG_SAVEFILE:
                adb.setTitle("Зберегти файл?");
                final EditText etFileName = new EditText(TableActivity.this);
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
                        fileName = etFileName.getText().toString();

                        if (!fileName.equals("")) {
                            saveInFile(fileName);
                        }
                        else {
                            showMessage("Error! Try again");
                        }
                    }
                });
                adb.setNegativeButton("Не зберігати", null);

        }
        return adb.create();
    }

    public boolean saveInFile(String fileName) {
        try {
            if (!fileName.contains("."))
                fileName += ".table";
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(table);
            os.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
