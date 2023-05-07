package hu.mobilalk.repjegy_foglalas;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();

    private SharedPreferences preferences;

    EditText LoginEmail, LoginPassword;
    Button btnLogin;

    FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginEmail = findViewById(R.id.emailEditText);
        LoginPassword = findViewById(R.id.passwordEditText);
        btnLogin = (Button) findViewById(R.id.loginButton);


        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_arrow_back);

        Log.i(LOG_TAG, "onCreate");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loginUser(View view){
        String email = LoginEmail.getText().toString();
        String password = LoginPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            LoginEmail.setError("Email megadása kötelező!");
            LoginEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)) {
            LoginPassword.setError("Jelszó megadása kötelező!");
            LoginPassword.requestFocus();
        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Bejelentkezés sikeres, frissítsd a felhasználói adatokat és navigálj a ProfileActivity-re
                            Log.d(LOG_TAG, "Sikeres bejelentkezés!");
                            Toast.makeText(LoginActivity.this, "Bejelentkezés sikeres.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, FlightListActivity.class));
                            finish();
                        } else {
                            // Ha a bejelentkezés sikertelen, jeleníts meg egy hibaüzenetet a felhasználónak
                            Log.d(LOG_TAG, "Sikertelen bejelentkezés!");
                            Toast.makeText(LoginActivity.this, "Bejelentkezés sikertelen.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
