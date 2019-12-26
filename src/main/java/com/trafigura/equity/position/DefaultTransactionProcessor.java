package com.trafigura.equity.position;

import com.trafigura.equity.position.model.Position;
import com.trafigura.equity.position.model.PositionChange;
import com.trafigura.equity.position.model.Transaction;

import java.util.List;

public class DefaultTransactionProcessor implements TransactionProcessor {

    private TradeManager tradeManager;
    private PositionManager positionManager;

    public DefaultTransactionProcessor(TradeManager tradeManager, PositionManager positionManager) {
        this.tradeManager = tradeManager;
        this.positionManager = positionManager;
    }

    public void process(Transaction txn) {
       List<PositionChange> pc = tradeManager.update(txn);
       positionManager.update(pc);
    }
}
