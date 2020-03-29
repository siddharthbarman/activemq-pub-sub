// Author       : Siddharth Barman
// Date         : 29 March 2020
// Description  : Represents a stock for the Stock Ticker sample application.

package sbytestream.samples.messaging;

import java.io.Serializable;

public class Tick implements Serializable {
    public Tick() {}

    public Tick(String symbol, float price, int direction) {
        this.symbol = symbol;
        this.price = price;
        this.direction = direction;
    }

    public String getSymbol() {
        return symbol;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void set(float price, int direction) {
        this.price = price;
        this.direction = direction;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        String icon = " ";
        if (direction == 1)
            icon = "+";
        else if (direction == -1)
            icon = "-";
        return String.format("%s %s %.2f", icon, symbol, price);
    }

    private String symbol;
    private float price;
    private int direction; // 0: No change, -1: down, 1: up
}
