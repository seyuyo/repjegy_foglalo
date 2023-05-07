package hu.mobilalk.repjegy_foglalas;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import hu.mobilalk.repjegy_foglalas.model.Flight;
import hu.mobilalk.repjegy_foglalas.model.Ticket;
import hu.mobilalk.repjegy_foglalas.model.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();

    EditText RegisterEmail, RegisterPassword, RegisterName, RegisterPhone;
    Button btnRegister;

    private SharedPreferences preferences;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        RegisterEmail = findViewById(R.id.emailEditText);
        RegisterPassword = findViewById(R.id.passwordEditText);
        RegisterName = findViewById(R.id.nameEditText);
        RegisterPhone = findViewById(R.id.phoneEditText);
        btnRegister = findViewById(R.id.registerButton);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");


        RegisterEmail.setText(email);
        RegisterPassword.setText(password);


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

    public void register(View view) {
        String email = RegisterEmail.getText().toString();
        String name = RegisterName.getText().toString();
        String password = RegisterPassword.getText().toString();
        String phone = RegisterPhone.getText().toString();

        if (TextUtils.isEmpty(email)){
            RegisterEmail.setError("Email megadása kötelező!");
            RegisterEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)) {
            RegisterPassword.setError("Jelszó megadása kötelező!");
            RegisterPassword.requestFocus();
        }


        Log.i(LOG_TAG, "Regisztrált: " + name + ", e-mail: " + email + ", telefon: " + phone);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Log.d(LOG_TAG, "Sikeres regisztráció!");
                Toast.makeText(RegisterActivity.this, "Sikeres regisztráció!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, FlightListActivity.class);
                intent.putExtra("user", mAuth.getCurrentUser());
                startActivity(intent);
            } else {
                Log.d(LOG_TAG, "Regisztráció nem sikerült");
                Toast.makeText(RegisterActivity.this, "Regisztráció nem sikerült: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }


    private void saveUserToFirestore(String name, String email, String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        User user = new User(name, email, phone);

        db.collection("Users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    user.setUserId(documentReference.getId());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Hiba a dokumentumhoz adáshoz", e));
    }


//    private void createUser(){
//        String email = RegisterEmail.getText().toString();
//        String password = RegisterPassword.getText().toString();
//        String name = RegisterName.getText().toString();
//        String phone = RegisterPhone.getText().toString();
//
//        if (TextUtils.isEmpty(email)){
//            RegisterEmail.setError("Email megadása kötelező!");
//            RegisterEmail.requestFocus();
//        }else if (TextUtils.isEmpty(password)) {
//            RegisterPassword.setError("Jelszó megadása kötelező!");
//            RegisterPassword.requestFocus();
//        }else if(TextUtils.isEmpty(name)){
//            RegisterName.setError("Név megadása kötelező!");
//            RegisterName.requestFocus();
//        }else if(TextUtils.isEmpty(phone)){
//            RegisterPhone.setError("Telefonszám megadása kötelező!");
//            RegisterPhone.requestFocus();
//        }else {
//            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()){
//                        Toast.makeText(RegisterActivity.this, "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                    }else {
//                        Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//        saveUserToFirestore(name, email, phone);
//    }
}

