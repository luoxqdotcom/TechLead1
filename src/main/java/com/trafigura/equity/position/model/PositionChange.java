package com.trafigura.equity.position.model;

public class PositionChange {
    private String securityCode;
    private long quantity;

    public PositionChange(String securityCode, long quantity) {
        this.securityCode = securityCode;
        this.quantity = quantity;
    }

    public PositionChange() {
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
