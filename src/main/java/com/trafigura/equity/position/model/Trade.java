package com.trafigura.equity.position.model;

public class Trade {
    private long tradeId;
    private int version;
    private String securityCode;
    private long quantity;
    private BuySell buySell;
    private boolean cancelled = false;

    public Trade() {
    }

    public Trade(long tradeId, int version, String securityCode, long quantity, BuySell buySell) {
        this.tradeId = tradeId;
        this.version = version;
        this.securityCode = securityCode;
        this.quantity = quantity;
        this.buySell = buySell;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        if (cancelled) this.quantity = 0;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public long getQuantity() {
        if (isCancelled()) return 0;
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public BuySell getBuySell() {
        return buySell;
    }

    public void setBuySell(BuySell buySell) {
        this.buySell = buySell;
    }

    public long getSignedQuantity() {
        if (isCancelled()) return 0;

        if (buySell == BuySell.SELL)
            return -quantity;
        else return quantity;
    }
}
