package com.ec205.dnd.control;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ec205.dnd.R;

import java.util.Random;


public class DiceFragment extends Fragment {

    private LayoutInflater inflater;
    private Button randomButton;
    private NumberPicker numberPicker;
    private TextView numberTextView;
    private RadioGroup diceRadioGroup;

    private int randomLimit;
    private int numberPickerValue;
    Random random = new Random();
    private int randomNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_dice, container, false);

        randomButton = (Button) view.findViewById(R.id.roll_button);
        numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberTextView = (TextView)view.findViewById(R.id.textView);
        diceRadioGroup = (RadioGroup) view.findViewById(R.id.radio_button_dice_select);

        diceRadioGroup.check(R.id.d4_radioButton);
        randomLimit = 4;

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setEnabled(true);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        diceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.d4_radioButton:
                        randomLimit = 4;
                        break;
                    case R.id.d6_radioButton:
                        randomLimit = 6;
                        break;
                    case R.id.d8_radioButton:
                        randomLimit = 8;
                        break;
                    case R.id.d10_radioButton:
                        randomLimit = 10;
                        break;
                    case R.id.d12_radioButton:
                        randomLimit = 12;
                        break;
                    case R.id.d20_radioButton:
                        randomLimit = 20;
                        break;
                    case R.id.d100_radioButton:
                        randomLimit = 100;
                        break;
                }
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomButton.setEnabled(false);
                numberPickerValue = numberPicker.getValue();
                new CountDownTimer(500, 10) {
                    public void onTick(long millisUntilFinished) {
                        numberTextView.setText(String.valueOf(random.nextInt(randomLimit*numberPickerValue)+1));
                    }
                    public void onFinish() {
                        numberPickerValue = numberPicker.getValue();
                        for(int i = 0; i<numberPickerValue; i++){
                            randomNumber = randomNumber + random.nextInt(randomLimit)+1;
                        }
                        numberTextView.setText(String.valueOf(randomNumber));
                        randomNumber = 0;
                        randomButton.setEnabled(true);
                    }
                }.start();
            }
        });
        return view;
    }
}
