package hu.mobilalk.repjegy_foglalas.model;

import java.util.Date;

public class Reservation {

    private String id;
    private String userId;
    private int flightNumber;
    private int seatCount;

    private int totalPrice;
    private String whereFrom;
    private String whereTo;
    private String flightDate;

    public Reservation() {
    }

    public Reservation(String userId, int flightNumber, int seatCount) {
        this.userId = userId;
        this.flightNumber = flightNumber;
        this.seatCount = seatCount;
    }

    public Reservation(String userId, int flightNumber, int seatCount, int totalPrice,
                       String flightDate, String whereFrom, String whereTo) {
        this.userId = userId;
        this.flightNumber = flightNumber;
        this.seatCount = seatCount;
        this.totalPrice = totalPrice;
        this.flightDate = flightDate;
        this.whereFrom = whereFrom;
        this.whereTo = whereTo;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getWhereFrom() {
        return whereFrom;
    }

    public void setWhereFrom(String whereFrom) {
        this.whereFrom = whereFrom;
    }

    public String getWhereTo() {
        return whereTo;
    }

    public void setWhereTo(String whereTo) {
        this.whereTo = whereTo;
    }
}
