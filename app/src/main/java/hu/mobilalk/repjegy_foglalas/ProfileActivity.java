package hu.mobilalk.repjegy_foglalas;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import hu.mobilalk.repjegy_foglalas.model.Reservation;

public class ProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ProfileActivity.class.getName();

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button logoutButton;

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private Button editUserDataButton;

    private Button deleteProfileButton;
    private RecyclerView reservationsRecyclerView;

    private CollectionReference flightsRef;

    private TextView userInfo;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
            String uid = user.getUid();
            Log.d(LOG_TAG, "Bejelentkezett felhasználó!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // A felhasználó be van jelentkezve, lekérdezzük a felhasználói adatokat
                    getUserData();
                } else {
                    // A felhasználó nincs bejelentkezve, navigálunk a LoginActivity-re
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        deleteProfileButton = findViewById(R.id.deleteButton);
        deleteProfileButton.setOnClickListener(view -> {
            deleteUser();
        });
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
        });

        reservationList = new ArrayList<>();
        reservationAdapter = new ReservationAdapter(reservationList);

        userInfo = findViewById(R.id.user_info);
        reservationsRecyclerView = findViewById(R.id.reservations_recycler_view);

        flightsRef = db.collection("Reservations");


         nameTextView = findViewById(R.id.name);
         emailTextView = findViewById(R.id.email);
         phoneTextView = findViewById(R.id.phone);


        editUserDataButton = findViewById(R.id.editUserDataButton);


        getUserData();
        getReservations();

        //homplex lekérdezések
        loadFlightsFromCity();
        loadUserReservations();

        editUserDataButton.setOnClickListener(view -> {
            // Navigate to the activity for editing user data
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void getUserData() {

        db.collection("Users")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String name = documentSnapshot.getString("fullName");
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phoneNumber");

                        String userInfo = "Név: " + name + "\nE-mail: " + email + "\nTelefonszám: " + phone;
                        nameTextView.setText(userInfo);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting documents.", e);
                });
    }


    private void getReservations() {
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        db.collection("Reservations")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            reservationList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Reservation reservation = document.toObject(Reservation.class);
                                reservationList.add(reservation);
                            }

                            reservationAdapter = new ReservationAdapter(reservationList);
                            reservationsRecyclerView.setAdapter(reservationAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void loadFlightsFromCity() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Flights")
                .whereEqualTo("departureLocation", "Budapest")
                .orderBy("flightNumber", Query.Direction.ASCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    private void loadUserReservations() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        Timestamp dateFilter = new Timestamp(new Date());

        db.collection("Reservations")
                .whereEqualTo("userId", userId)
                .whereGreaterThan("reservationDate", dateFilter)
                .orderBy("reservationDate", Query.Direction.ASCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }


    private void deleteUser(){
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        }
                    }
                });
    }

    private void deleteReservation(String reservationId) {
        db.collection("Reservations")
                .document(reservationId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    getReservations();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }
    /*lekérdezés

    FirebaseFirestore db = FirebaseFirestore.getInstance();

db.collection("flights")
    .whereEqualTo("departureLocation", "Budapest")
    .orderBy("flightNumber", Query.Direction.ASCENDING)
    .limit(10)
    .get()
    .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Log.d(TAG, document.getId() + " => " + document.getData());
            }
        } else {
            Log.w(TAG, "Error getting documents.", task.getException());
        }
    });

    *
    * */


    /*
    * lekérdezés
    * FirebaseFirestore db = FirebaseFirestore.getInstance();
FirebaseAuth mAuth = FirebaseAuth.getInstance();
String userId = mAuth.getCurrentUser().getUid();

Timestamp dateFilter = new Timestamp(new Date()); // Set this to the date you want to filter from

db.collection("reservations")
    .whereEqualTo("userId", userId)
    .whereGreaterThan("reservationDate", dateFilter)
    .orderBy("reservationDate", Query.Direction.ASCENDING)
    .limit(5)
    .get()
    .addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Log.d(TAG, document.getId() + " => " + document.getData());
            }
        } else {
            Log.w(TAG, "Error getting documents.", task.getException());
        }
    });

    *
    * */
}
