package org.example.demo;

import javafx.beans.property.*;

import java.util.Date;

public class Flight {
    String numero;
    String destination;
    String depart;
    Date date;
    double price;

    public Flight(String numero,Date date , String depart, String destination,double price) {
        this.numero = numero;
        this.destination = destination;
        this.depart = depart;
        this.date = date;
        this.price=price;
    }

    public String getNumero() {
        return numero;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepart() {
        return depart;
    }

    public Date getDate() {
        return date;
    }
    public double getPrice(){return price;}


}