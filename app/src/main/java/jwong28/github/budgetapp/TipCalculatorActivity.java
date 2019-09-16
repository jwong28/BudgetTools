package jwong28.github.budgetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class TipCalculatorActivity extends AppCompatActivity implements View.OnClickListener{

    //Edit texts
    public static EditText preCost;
    public static EditText tax;
    public static EditText taxPercent;
    public static EditText tipPercent;
    public static EditText tip;
    public static TextView totalCost;
    public static Button btnBack;

    //Round format
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        //Call touch listener
        checkTouch(findViewById(R.id.tip_calculator_layout));

        btnBack = findViewById(R.id.btnBack);
        //Button Listeners
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.btnAddTip).setOnClickListener(this);
        findViewById(R.id.btnMinusTip).setOnClickListener(this);

        //Views
        preCost = findViewById(R.id.txtPrice);
        tax = findViewById(R.id.txtTax);
        taxPercent = findViewById(R.id.txtTaxPercent);
        tip = findViewById(R.id.txtTip);
        tipPercent = findViewById(R.id.txtTipPercent);
        totalCost = findViewById(R.id.txtTotal);

        //Set initial tax and tip
        taxPercent.setText(String.valueOf(8.875));
        tipPercent.setText(String.valueOf(15));

        preCost.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && preCost.getText() != null && preCost.getText().length() != 0){
                    changePreCost();

                }
            }
        });
        tax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && tax.getText() != null && tax.getText().length() != 0){
                    getTaxPercent();
                }
            }
        });
        taxPercent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && taxPercent.getText() != null && taxPercent.getText().length() != 0){
                    getTax();
                }
            }
        });
        tip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && tip.getText() != null && tip.getText().length() != 0){
                    getTipPercent();
                }
            }
        });
        tipPercent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && tipPercent.getText() != null && tipPercent.getText().length() != 0){
                    getTip();
                }
            }
        });

        //Check if intent has been passed in
//        if( getIntent() != null){
        Intent intent = getIntent();
        preCost.setText(intent.getStringExtra("Price"));
        if(preCost.getText().length() > 0){
            changePreCost();
        }
//        }

    }

    public void toHome(){
        Intent intent = new Intent(TipCalculatorActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void tipDecrease() {
        int tipNum = Integer.valueOf(tipPercent.getText().toString()) - 1;
        tipPercent.setText(Integer.toString(tipNum));
        getTip();
        calculateTotal();
    }

    public void tipIncrease() {
        int tipNum = Integer.valueOf(tipPercent.getText().toString()) + 1;
        tipPercent.setText(Integer.toString(tipNum));
        getTip();
        calculateTotal();
    }

    //If the initial price changes, update all
    public void changePreCost() {
        double taxCost =  Double.valueOf(preCost.getText().toString()) *
                Double.valueOf(taxPercent.getText().toString()) / 100;
        double tipCost =  Double.valueOf(preCost.getText().toString()) *
                Double.valueOf(tipPercent.getText().toString()) / 100;
        tax.setText(df.format(taxCost));
        tip.setText(df.format(tipCost));
        calculateTotal();
    }

    //If tax changes, update percent
    public void getTaxPercent(){
        if(preCost.getText().length() > 0){
            double percent =  Double.valueOf(tax.getText().toString()) * 100 /
                    Double.valueOf(preCost.getText().toString());
            taxPercent.setText(df.format(percent));
            calculateTotal();
        }

    }

    //If percent changes, update tax
    public void getTax() {
        if(preCost.getText().length() > 0){
            double taxCost =  Double.valueOf(preCost.getText().toString()) *
                    Double.valueOf(taxPercent.getText().toString()) / 100;
            tax.setText(df.format(taxCost));
            calculateTotal();
        }
    }

    //If tax changes, update percent
    public void getTipPercent(){
        if(preCost.getText().length() > 0){
            double percent =  Double.valueOf(tip.getText().toString()) * 100 /
                    Double.valueOf(preCost.getText().toString());
            tipPercent.setText(df.format(percent));
            calculateTotal();
        }
    }

    //If percent changes, update tax
    public void getTip() {
        if(preCost.getText().length() > 0){
            double tipCost =  Double.valueOf(preCost.getText().toString()) *
                    Double.valueOf(tipPercent.getText().toString()) / 100;
            tip.setText(df.format(tipCost));
            calculateTotal();
        }
    }

    public void calculateTotal(){
        if(preCost.getText().length() > 0) {
            double total = Double.valueOf(preCost.getText().toString()) +
                    Double.valueOf(tax.getText().toString()) +
                    Double.valueOf(tip.getText().toString());
            totalCost.setText(df.format(total));
        }
    }


    // Set up touch listener for non-text box views to hide keyboard.
    public void checkTouch(View view){
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(TipCalculatorActivity.this);
                    v.clearFocus();
                    return false;
                }
            });
        }
    }

    //Hide keyboard when touched outside
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnBack) {
            toHome();
        }
        else if(i == R.id.btnAddTip) {
            tipIncrease();
        }
        else if(i == R.id.btnMinusTip) {
            tipDecrease();
        }
    }
}
