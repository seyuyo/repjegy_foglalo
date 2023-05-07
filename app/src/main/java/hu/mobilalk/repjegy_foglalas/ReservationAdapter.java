package hu.mobilalk.repjegy_foglalas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;

import hu.mobilalk.repjegy_foglalas.model.Reservation;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private List<Reservation> reservationList;
    private Context context;

    public ReservationAdapter(List<Reservation> reservationList, Context context) {
        this.reservationList = reservationList;
        this.context = context;
    }

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;

    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        // Set the data in the TextViews based on your Reservation properties

        // Set other TextViews with reservation data

    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder {

        TextView reservationId;
        TextView flightNumber;
        // Declare other views in the reservation item layout

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            reservationId = itemView.findViewById(R.id.reservation_id);
            flightNumber = itemView.findViewById(R.id.flight_number);
            // Initialize other views in the reservation item layout
        }
    }
}
