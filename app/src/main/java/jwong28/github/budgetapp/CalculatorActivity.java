package jwong28.github.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    public ArrayList<String> equation = new ArrayList<>();
    public String currentVal = "";
    public TextView resultView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //Button Listeners
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
        findViewById(R.id.btnDec).setOnClickListener(this);
        findViewById(R.id.btnMin).setOnClickListener(this);
        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.btnDiv).setOnClickListener(this);
        findViewById(R.id.btnMul).setOnClickListener(this);
        findViewById(R.id.btnEq).setOnClickListener(this);
        findViewById(R.id.btnDel).setOnClickListener(this);
        findViewById(R.id.btnC).setOnClickListener(this);
        findViewById(R.id.btnTipCalculator).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);

        //View listeners
        resultView = findViewById(R.id.txtResults);

        //Add empty value to arraylist
        equation.add("");

    }

    public void addNumber(String s){
        //Check if string limit reached
        if(resultView.getText().toString().length() == 30){
            Snackbar.make(findViewById(R.id.calculator_layout), "Limit reached, please shorten equation", Snackbar.LENGTH_SHORT).show();
            return;
        }
        int lastIndex = equation.size()-1;
        if(currentVal.length() == 0 && s == "."){
            currentVal += "0.";
        }
        else {
            currentVal += s;
            //        resultView.setText(currentVal);
        }

        if(isOp(equation.get(lastIndex))){
            equation.add(currentVal);
        }
        else {
            equation.set(lastIndex, currentVal);
        }
            showEquation();
    }

    public void addOperation(String s){
        //Check if string limit reached
        if(resultView.getText().toString().length() == 30){
            Snackbar.make(findViewById(R.id.calculator_layout), "Limit reached, please shorten equation", Snackbar.LENGTH_SHORT).show();
            return;
        }
        int lastIndex = equation.size()-1;
        if(currentVal.length() == 0 && isOp(equation.get(lastIndex))){
            equation.set(lastIndex, s);
            showEquation();
        }
        else if(lastIndex != 0 || currentVal.length() != 0){
            currentVal = "";
            equation.add(s);
            showEquation();
        }

    }

    public void showEquation() {
        String text = "";
        for(String entry : equation){
            text+= entry;
        }
        resultView.setText(text);
    }

    public void solveEquation() {
        double result = Double.parseDouble(equation.get(0));
        if(isOp(equation.get(equation.size()-1))){
            Snackbar.make(findViewById(R.id.calculator_layout), "Cannot end with operation", Snackbar.LENGTH_SHORT).show();
            return;
        }
        for(int i = 1; i<equation.size(); i+=2){
            if(equation.get(i) == "+"){
                result += Double.parseDouble(equation.get(i+1));
            }
            else if(equation.get(i) == "-"){
                result -= Double.parseDouble(equation.get(i+1));
            }
            else if(equation.get(i) == "*"){
                result *= Double.parseDouble(equation.get(i+1));
            }
            else if(equation.get(i) == "/"){
                result /= Double.parseDouble(equation.get(i+1));
            }
        }
        if ((result == Math.floor(result)) && !Double.isInfinite(result)) {
            // integer type
            currentVal = Integer.toString((int)result);
        }
        else {
            currentVal = Double.toString(result);

        }
        equation.clear();
        equation.add(currentVal);
        showEquation();
    }

    public void onClear(){
        equation.clear();
        equation.add("");
        currentVal = "";
        showEquation();
    }

    public void onDelete() {
        int lastIndex = equation.size()-1;
        if(isOp(equation.get(lastIndex))){
            equation.remove(lastIndex);
            showEquation();
        }
        else{
            currentVal = equation.get(lastIndex).substring(0,equation.get(lastIndex).length()-1);
            if(currentVal == null || currentVal.length() == 0){
                equation.remove(lastIndex);
            }
            else{
                equation.set(lastIndex, currentVal);
            }
            showEquation();
        }

    }

    public boolean isOp(String s){
        if(s == "+" || s == "-" || s == "/" || s == "*"){
            return true;
        }
        return false;
    }

    public void toTipCalculator(){
        Intent intent = new Intent(CalculatorActivity.this, TipCalculatorActivity.class);
        intent.putExtra("Price", resultView.getText().toString());
        startActivity(intent);
    }

    public void toHome(){
        Intent intent = new Intent(CalculatorActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.btn0){
            addNumber("0");
        }
        else if(i == R.id.btn1){
            addNumber("1");
        }
        else if(i == R.id.btn2){
            addNumber("2");
        }
        else if(i == R.id.btn3){
            addNumber("3");
        }
        else if(i == R.id.btn4){
            addNumber("4");
        }
        else if(i == R.id.btn5){
            addNumber("5");
        }
        else if(i == R.id.btn6){
            addNumber("6");
        }
        else if(i == R.id.btn7){
            addNumber("7");
        }
        else if(i == R.id.btn8){
            addNumber("8");
        }
        else if(i == R.id.btn9){
            addNumber("9");
        }
        else if(i == R.id.btnDec){
            addNumber(".");
        }
        else if(i == R.id.btnMin){
            addOperation("-");
        }
        else if(i == R.id.btnAdd){
            addOperation("+");
        }
        else if(i == R.id.btnDiv){
            addOperation("/");
        }
        else if(i == R.id.btnMul){
            addOperation("*");
        }
        else if(i == R.id.btnEq){
            solveEquation();
        }
        else if(i == R.id.btnDel){
            if(equation.size() > 0 && equation.get(0).length() > 0){
                onDelete();
            }
        }
        else if(i == R.id.btnC){
            onClear();
        }
        else if(i == R.id.btnTipCalculator){
            toTipCalculator();
        }
        else if (i == R.id.btnBack) {
            toHome();
        }
    }
}
