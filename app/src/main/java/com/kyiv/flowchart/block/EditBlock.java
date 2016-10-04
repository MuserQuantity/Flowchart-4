package com.kyiv.flowchart.block;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import com.kyiv.flowchart.R;
import com.kyiv.flowchart.draw.DrawView;
import com.kyiv.flowchart.layout.MyFrameLayout;

public class EditBlock extends AppCompatActivity implements TextWatcher, NumberPicker.OnValueChangeListener{
    private NumberPicker numberPicker;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_block);
        editText = (EditText) findViewById(R.id.etText);
        editText.setText(DrawView.editBlock.getText());
        editText.addTextChangedListener(this);

        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        numberPicker.setValue(20);
        numberPicker.setOnValueChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        DrawView.editBlock.setText(editText.getText().toString());
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        DrawView.editBlock.setTextSize(i1);
    }

    public void Save(View view){
        DrawView.editBlock.setText(editText.getText().toString());
        DrawView.editBlock.setTextSize(numberPicker.getValue());
        finish();
    }
}
