package hu.mobilalk.repjegy_foglalas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.mobilalk.repjegy_foglalas.model.Flight;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private Context context;
    private List<Flight> flightList;

    public FlightAdapter(Context context, List<Flight> flightList) {
        this.context = context;
        this.flightList = flightList;
    }


    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flight_item, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.flightNumber.setText("Repülőjárat száma: " + flight.getFlightNumber());
        holder.departureLocation.setText("Indulás helyszín: " + flight.getDepartureLocation());
        holder.arrivalLocation.setText("Érkezés helyszín: " + flight.getArrivalLocation());
        holder.departureTime.setText("Indulási idő: " + flight.getDepartureTime());
        holder.arrivalTime.setText("Érkezési idő: " + flight.getArrivalTime());
        holder.availableSeats.setText("Elérhető ülések: " + flight.getAvailableSeats());
        holder.price.setText("Ár: " + flight.getPrice() + " HUF");


        holder.addToCart.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            holder.itemView.startAnimation(animation);
            Intent intent = new Intent(context, ReservationSummaryActivity.class);
            intent.putExtra("selectedFlight", flight);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView flightNumber;
        TextView departureLocation;
        TextView arrivalLocation;
        TextView departureTime;
        TextView arrivalTime;
        TextView availableSeats;
        TextView price;
        Button addToCart;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumber = itemView.findViewById(R.id.flight_number);
            departureLocation = itemView.findViewById(R.id.departure_location);
            arrivalLocation = itemView.findViewById(R.id.arrival_location);
            departureTime = itemView.findViewById(R.id.departure_time);
            arrivalTime = itemView.findViewById(R.id.arrival_time);
            availableSeats = itemView.findViewById(R.id.available_seats);
            price = itemView.findViewById(R.id.price);
            addToCart = itemView.findViewById(R.id.add_to_cart);
        }
    }

}

