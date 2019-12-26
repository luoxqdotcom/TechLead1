package com.trafigura.equity.position;

import com.trafigura.equity.position.model.Action;
import com.trafigura.equity.position.model.PositionChange;
import com.trafigura.equity.position.model.Trade;
import com.trafigura.equity.position.model.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTradeManager implements TradeManager {
    ConcurrentHashMap<Long, Trade> trades = new ConcurrentHashMap<>();


    @Override
    public Collection<Trade> getAllTrades() {
        return trades.values();
    }

    public List<PositionChange> update(Transaction txn) {
        Trade trade = new Trade(txn.getTradeId(),txn.getVersion(),txn.getSecurityCode(),txn.getQuantity(),txn.getBuySell());
        trade.setCancelled(txn.getAction() == Action.CANCEL);
        List<PositionChange> change= new ArrayList<>();
        trades.compute(trade.getTradeId(),(tradeId,old)->{
            if(old!=null && old.isCancelled()) return old;

            if(old==null || trade.getVersion()>old.getVersion()){
               change.addAll(computeChanges(old,trade));
               return trade;
            }else{
                return old;
            }
        });

        return change;
    }
}
