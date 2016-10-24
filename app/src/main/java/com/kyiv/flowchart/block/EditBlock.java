package com.kyiv.flowchart.block;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.kyiv.flowchart.R;

public class EditBlock extends AppCompatActivity{
    private NumberPicker numberPicker;
    private EditText editText;
    private EditText editTextNameNode;
    private String blockType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_block);
        Intent intent = getIntent();
        editText = (EditText) findViewById(R.id.etText);
        editText.setText(intent.getStringExtra("text"));
        editTextNameNode = (EditText) findViewById(R.id.etTextNameNode);
        blockType = intent.getStringExtra("blockType");
        if (!blockType.equals(BlockType.RHOMB.toString())){
            String nameNode = intent.getStringExtra("nameNode");
            editTextNameNode.setText(nameNode);
        }
        else {
            editTextNameNode.setEnabled(false);
            editTextNameNode.setText("Даний блок не може бути вузлом");
        }



        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        numberPicker.setValue(intent.getIntExtra("text_size", 20));
    }

    public void Save(View view){
        Intent intent = new Intent();
        intent.putExtra("text", editText.getText().toString());
        intent.putExtra("text_size", numberPicker.getValue());
        if (!blockType.equals(BlockType.RHOMB.toString()))
            intent.putExtra("nameNode", editTextNameNode.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
