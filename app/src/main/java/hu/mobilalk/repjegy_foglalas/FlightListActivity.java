package hu.mobilalk.repjegy_foglalas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

import hu.mobilalk.repjegy_foglalas.model.Flight;

public class FlightListActivity extends AppCompatActivity {

    private static final String LOG_TAG = FlightListActivity.class.getName();

    private FirebaseUser user;

    private static final int PERMISSION_REQUEST_CAMERA = 0;

    static final int REQUEST_IMAGE_CAPTURE = 1;


    private RecyclerView flightRecyclerView;
    private FlightAdapter flightAdapter;
    private List<Flight> flightList;
    private FirebaseFirestore firestore;
    private CollectionReference flightsRef;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Bejelentkezett felhasználó!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            // Ha már megvan az engedély, akkor indítsuk el a kamera használatát
        }

        flightRecyclerView = findViewById(R.id.flight_recycler_view);

        flightList = new ArrayList<>();
        // Add your Flight objects to the flightList here

        flightAdapter = new FlightAdapter(this, flightList);
        flightRecyclerView.setAdapter(flightAdapter);
        flightRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();

        flightsRef = firestore.collection("Flights");

        fetchFlights();
    }
    private void fetchFlights() {
        flightsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    flightList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Flight flight = document.toObject(Flight.class);
                        flightList.add(flight);
                    }
                    flightAdapter.notifyDataSetChanged();
                } else {
                    Log.d("FlightListActivity", "Error getting documents.", task.getException());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Az alkalmazás nem tudja használni a kamerát", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(
                requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // A kamera képet készített, itt feldolgozhatjuk a képet
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmap);
        }
    }


}
