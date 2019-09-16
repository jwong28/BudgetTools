package jwong28.github.budgetapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class InputFormActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase database
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    //Form items
    DatePicker date;
    EditText location;
    EditText item;
    EditText description;
    EditText amount;

    //Database posts
    static String postTitle;
    String dateOfPost;
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_form);

        //Database reference
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        //Get the reference with the user table
        myRef = mFirebaseDatabase.getReference().child(user.getUid());

        //Call touch listener
        checkTouch(findViewById(R.id.input_form_layout));

        //Button listener
        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);

        //Form listeners
        date = findViewById(R.id.pickerDate);
        location = findViewById(R.id.txtLocation);
        item = findViewById(R.id.txtItem);
        description = findViewById(R.id.txtDescription);
        amount = findViewById(R.id.txtAmount);
        description.setText("");
        postTitle = "Test2";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // If database gets updated
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TAG", dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                updateInfoTitle();
            }
        });

        //Post title
        updateInfoTitle();
    }

    public boolean  validateDate() {
        calendar = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDayOfMonth());
        Date today = Calendar.getInstance().getTime();
        Date picker = calendar.getTime();
        if(location.getText().toString().length() == 0 || location.getText().toString() == null){
            return false;
        }
        else if(item.getText().toString().length() == 0 || item.getText().toString() == null){
            return false;
        }
        else if(amount.getText().toString().length() == 0 || amount.getText().toString() == null){
            return false;
        }
        else if(picker.compareTo(today) > 0) {
            return false;
        }
//        updateInfoTitle();
        return true;
    }

    public void updateInfoTitle(){
        dateOfPost = dateFormat.format(calendar.getTime());
        Query query = myRef.orderByChild("dateOfPost").equalTo(dateOfPost);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount() + 1;
                postTitle = dateOfPost+"_"+count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void saveToDatabase(){
        if(validateDate()){
            Date transactionDay = calendar.getTime();
            Post transaction = new Post(transactionDay, location.getText().toString(), item.getText().toString(), description.getText().toString(), amount.getText().toString());
            myRef.child(postTitle).setValue(transaction);
            location.setText("");
            item.setText("");
            description.setText("");
            amount.setText("");
            Snackbar.make(findViewById(R.id.input_form_layout), "Spending Saved", Snackbar.LENGTH_SHORT).show();

        }

    }

    public void toHome(){
        Intent intent = new Intent(InputFormActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    // Set up touch listener for non-text box views to hide keyboard.
    public void checkTouch(View view){
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(InputFormActivity.this);
                    v.clearFocus();
                    return false;
                }
            });
        }
    }

    //Hide keyboard when touched outside
    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus() != null){
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.btnSave){
            saveToDatabase();
        }
        else if(i == R.id.btnBack){
            toHome();
        }
    }
}
