package jwong28.github.budgetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    // [START declare_auth]
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Google sign in
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Button listeners
        findViewById(R.id.signOutButton).setOnClickListener(this);
        findViewById(R.id.btnTipCalculator).setOnClickListener(this);
        findViewById(R.id.btnCalculator).setOnClickListener(this);
        findViewById(R.id.btnBudgetInput).setOnClickListener(this);
        findViewById(R.id.btnSummary).setOnClickListener(this);

        TextView txtName = findViewById(R.id.Name);

        txtName.setText(mAuth.getCurrentUser().getEmail());
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Go back to login
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public void toTipCalculator() {
        Intent intent = new Intent(HomeActivity.this, TipCalculatorActivity.class);
        startActivity(intent);
    }

    public void toClaculator(){
        Intent intent = new Intent(HomeActivity.this, CalculatorActivity.class);
        startActivity(intent);
    }

    public void toInput(){
        Intent intent = new Intent(HomeActivity.this, InputFormActivity.class);
        startActivity(intent);
    }

    public void toSummary(){
        Intent intent = new Intent(HomeActivity.this, SummaryActivity.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signOutButton) {
            signOut();
        }
        else if(i == R.id.btnTipCalculator) {
            toTipCalculator();
        }
        else if(i == R.id.btnCalculator) {
            toClaculator();
        }
        else if(i == R.id.btnBudgetInput) {
            toInput();
        }
        else if(i == R.id.btnSummary){
            toSummary();
        }
    }


}
