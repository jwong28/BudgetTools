package jwong28.github.budgetapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SummaryActivity extends AppCompatActivity {
    //Firebase database
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    TableLayout table;
    TextView amount;
    Double monthlySpending = 0.0;
    Post post = new Post();
    Calendar calendar = Calendar.getInstance();
    String today;
    DateFormat dateFormat;
    PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        table = findViewById(R.id.table);
        amount = findViewById(R.id.txtAmount);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        popupWindow = new PopupWindow(this);

        //Database reference
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        //Get the reference with the user table
        myRef = mFirebaseDatabase.getReference().child(user.getUid());

        //Post transactions of the month
        today = dateFormat.format(calendar.getTime());
        Query query = myRef.orderByChild("dateOfPost").startAt(today.substring(0,7));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("Tag", "starting");
                long count = dataSnapshot.getChildrenCount();
                Log.v("Tag", count+"");
                for(DataSnapshot dataSnapshots : dataSnapshot.getChildren()){
                    Log.v("Tag",dataSnapshots.getValue()+"");
                    post = dataSnapshots.getValue(Post.class);
                    //Each post is a row
                    TableRow row = new TableRow(SummaryActivity.this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp.setMargins(2,4,2,4);
//                    row.setLayoutParams(lp);
                    //Each row is a table with 2 rows
                    LinearLayout table1 = new LinearLayout(SummaryActivity.this);
                    table1.setOrientation(LinearLayout.VERTICAL);
//                    table1.setLayoutParams(tp);
                    TableRow row1 = new TableRow(SummaryActivity.this);
                    row1.setLayoutParams(lp);
                    TableRow row2 = new TableRow(SummaryActivity.this);
                    row2.setWeightSum(2);
                    row2.setLayoutParams(lp);
                    //Show date first
                    TextView textDate = new TextView(SummaryActivity.this);
                    textDate.setBackgroundResource(R.color.colorWhite);
                    textDate.setTextSize(20);
//                    textDate.setLayoutParams(lp);
                    textDate.setText(post.getDate());
                    //Empty text
                    TextView empty = new TextView(SummaryActivity.this);
                    empty.setText("");
                    //Show location and price
                    final TextView location = new TextView(SummaryActivity.this);
                    location.setLayoutParams(lp);
                    location.setBackgroundResource(R.color.colorWhite);
                    location.setTextSize(32);
                    if(post.getLocation().length() >  12){
                        location.setText(post.getLocation().substring(0,10));
                    }
                    else {
                        location.setText(post.getLocation());
                    }
                    //Show price
                    TextView price = new TextView(SummaryActivity.this);
                    price.setBackgroundResource(R.color.colorWhite);
                    price.setTextSize(32);
                    price.setText("$ "+  post.getAmount());
                    row1.addView(textDate);
                    row2.addView(location);
                    row2.addView(price);
                    //Add to table
                    table1.addView(row1);
                    table1.addView(row2);
                    row.addView(table1);
                    table.addView(row);
                    monthlySpending+=post.getAmount();
                    updateAmount();

                    //Users can see full details on row click
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String loc = location.getText().toString();
                            Log.v("tag", loc);


//                            Intent intent = new Intent(SummaryActivity.this, DetailActivity.class);
//                            startActivity(intent);
                            LayoutInflater layoutInflater = (LayoutInflater) SummaryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View popupView = layoutInflater.inflate(R.layout.activity_detail,null);
                            // create the popup window
                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            boolean focusable = true; // lets taps outside the popup also dismiss it
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                            TextView store = popupWindow.getContentView().findViewById(R.id.txtStore);
                            store.setText(post.getLocation());
                            // show the popup w
                            // indow
                            // which view you pass in doesn't matter, it is only used for the window tolken
                            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                            // dismiss the popup window when touched
                            popupView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    popupWindow.dismiss();
                                    return true;
                                }
                            });

//                            closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void updateAmount(){
        amount.setText("You have spent $"+ monthlySpending);
    }
}
