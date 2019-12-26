package com.trafigura.equity.position.model;

public class Position {
    private String securityCode;
    private long quantity;

    public Position() {
    }

    public Position(String securityCode, long quantity) {
        this.securityCode = securityCode;
        this.quantity = quantity;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
