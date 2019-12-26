package com.trafigura.equity.position;

import com.trafigura.equity.position.model.PositionChange;
import com.trafigura.equity.position.model.Trade;
import com.trafigura.equity.position.model.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TradeManager {
    List<PositionChange> update(Transaction txn);

    Collection<Trade> getAllTrades();

    default List<PositionChange> computeChanges(Trade old, Trade now) {

        List<PositionChange> changes = new ArrayList<>();

        if (old == null) {
            if (now.isCancelled()) {

            } else {
                changes.add(new PositionChange(now.getSecurityCode(),
                        now.getSignedQuantity()));
            }
            return changes;
        }

        if (old.isCancelled()) return changes;

        if (now.getTradeId() != old.getTradeId()) throw new IllegalArgumentException("trade id is not same");

        if (now.getVersion() > old.getVersion() || now.isCancelled()) {

            if (old.getSecurityCode().equals(now.getSecurityCode())) {
                changes.add(new PositionChange(now.getSecurityCode(),
                        now.getSignedQuantity() - old.getSignedQuantity()));
            } else {
                changes.add(new PositionChange(old.getSecurityCode(), -old.getSignedQuantity()));
                changes.add(new PositionChange(now.getSecurityCode(), now.getSignedQuantity()));
            }
        }
        return changes;
    }
}
