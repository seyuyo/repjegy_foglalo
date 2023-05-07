package hu.mobilalk.repjegy_foglalas.model;

import java.util.Date;

public class Ticket {
    private String reservationId;
    private Date bookingDate;
    private User user;
    private Flight flight;
    private int numberOfPassengers;
    private String destination;

    public Ticket() {
    }

    public Ticket(String reservationId, Date bookingDate, User user, Flight flight,
                  int numberOfPassengers, String destination) {
        this.reservationId = reservationId;
        this.bookingDate = bookingDate;
        this.user = user;
        this.flight = flight;
        this.numberOfPassengers = numberOfPassengers;
        this.destination = destination;
    }

    public Ticket(Date bookingDate, User user, Flight flight, int numberOfPassengers, String destination) {
        this.bookingDate = bookingDate;
        this.user = user;
        this.flight = flight;
        this.numberOfPassengers = numberOfPassengers;
        this.destination = destination;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


}
