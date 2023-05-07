package hu.mobilalk.repjegy_foglalas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import hu.mobilalk.repjegy_foglalas.model.Flight;
import hu.mobilalk.repjegy_foglalas.model.Reservation;

public class ReservationSummaryActivity extends AppCompatActivity {

    private Flight selectedFlight;
    private TextView flightSummary;
    private TextView reservationSummary;
    private FirebaseFirestore db;

    private Button reserveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_summary);

        selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");

        flightSummary = findViewById(R.id.flight_summary);
        reservationSummary = findViewById(R.id.reservation_summary);

        db = FirebaseFirestore.getInstance();
        reserveButton = findViewById(R.id.save_reservation_button);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReservation();
                Intent intent = new Intent(ReservationSummaryActivity.this, ProfileActivity.class);
                startActivity(intent);
                showSuccessAnimation();
            }
        });

        updateFlightSummary();
        updateReservationSummary();
    }

    private void updateFlightSummary() {
        String summary = "Utazás:  " + selectedFlight.getDepartureLocation() + "-ból/ből " + selectedFlight.getArrivalLocation() + "-ba/be" +
                "\nIndulás: " + selectedFlight.getDepartureTime() +
                "\nÉrkezés: " + selectedFlight.getArrivalTime();
        flightSummary.setText(summary);
    }

    private void updateReservationSummary() {
        // Add your reservation details here, for example, the number of tickets and total price
        int numberOfTickets = 1; // This is just an example value. You should get the actual value based on user input.
        int totalPrice = numberOfTickets * selectedFlight.getPrice();

        String summary = "Jegyek száma: " + numberOfTickets +
                "\nTeljes Ár: " + totalPrice + " " + getString(R.string.currency);
        reservationSummary.setText(summary);
    }

    private void saveReservation() {
        // Get the flight and user details
        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        EditText seatCountEditText = findViewById(R.id.seat_count_edit_text);
        int seatCount = Integer.parseInt(seatCountEditText.getText().toString());

        // Create a Reservation object
        Reservation reservation = new Reservation(userId, selectedFlight.getFlightNumber(), seatCount);

        // Save the reservation to Firestore
        db.collection("Reservations")
                .add(reservation)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        showSuccessAnimation();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReservationSummaryActivity.this, "Error saving reservation", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showSuccessAnimation() {
        Toast.makeText(this, "Gratulálok! Sikeres foglalás!", Toast.LENGTH_LONG).show();
    }

}
