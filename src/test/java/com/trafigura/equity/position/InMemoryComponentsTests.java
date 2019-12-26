package com.trafigura.equity.position;

import com.trafigura.equity.position.model.Action;
import com.trafigura.equity.position.model.BuySell;
import com.trafigura.equity.position.model.Trade;
import com.trafigura.equity.position.model.Transaction;
import org.junit.Before;
import org.junit.Test;

public class InMemoryComponentsTests {

    TradeManager tradeManager = null;
    PositionManager positionManager = null;
    TransactionProcessor transactionProcessor = null;

    @Before
    public void setup() {
        tradeManager = new InMemoryTradeManager();
        positionManager = new InMemoryPositionManager();
        transactionProcessor = new DefaultTransactionProcessor(tradeManager, positionManager);
    }

    @Test
    public void testInsertUpdateCancel() throws Exception {
        Transaction insert = new Transaction(1L, 1l, 1, "REL", 10, Action.INSERT, BuySell.BUY);
        transactionProcessor.process(insert);
        assert (tradeManager.getAllTrades().size() == 1);
        Trade trade = tradeManager.getAllTrades().iterator().next();
        assert (trade.getTradeId() == 1);
        assert (trade.getVersion() == 1);
        assert (trade.getSecurityCode() == "REL");
        assert (trade.getQuantity() == 10);
        assert (trade.getSignedQuantity() == 10);
        assert (trade.getBuySell() == BuySell.BUY);

        assert (positionManager.getPositionCount() == 1);
        assert (positionManager.getPosition("REL") == 10);

        Transaction update = new Transaction(2L, 1l, 2, "REL", 15, Action.UPDATE, BuySell.BUY);
        transactionProcessor.process(update);

        trade = tradeManager.getAllTrades().iterator().next();
        assert (trade.getTradeId() == 1);
        assert (trade.getVersion() == 2);
        assert (trade.getSecurityCode() == "REL");
        assert (trade.getQuantity() == 15);
        assert (trade.getSignedQuantity() == 15);
        assert (trade.getBuySell() == BuySell.BUY);

        assert (positionManager.getPositionCount() == 1);
        assert (positionManager.getPosition("REL") == 15);

        Transaction cancel = new Transaction(3L, 1l, 3, "REL", 15, Action.CANCEL, BuySell.BUY);
        transactionProcessor.process(cancel);

        trade = tradeManager.getAllTrades().iterator().next();
        assert (trade.getTradeId() == 1);
        assert (trade.getVersion() == 3);
        assert (trade.getSecurityCode() == "REL");
        assert (trade.getQuantity() == 0);
        assert (trade.getSignedQuantity() == 0);

        assert (positionManager.getPositionCount() == 1);
        assert (positionManager.getPosition("REL") == 0);
    }

    @Test
    public void tesCancelFirst() throws Exception {
        Transaction insert = new Transaction(1L, 1l, 1, "REL", 10, Action.CANCEL, BuySell.BUY);
        transactionProcessor.process(insert);

        {
            assert (tradeManager.getAllTrades().size() == 1);
            Trade trade = tradeManager.getAllTrades().iterator().next();
            assert (trade.getQuantity() == 0);
            assert (trade.getSignedQuantity() == 0);
            assert (positionManager.getPositionCount() == 0);
            assert (positionManager.getPosition("REL") == 0);
        }

        Transaction update = new Transaction(2L, 1l, 2, "REL", 15, Action.INSERT, BuySell.BUY);
        transactionProcessor.process(update);

        {
            assert (tradeManager.getAllTrades().size() == 1);
            Trade trade = tradeManager.getAllTrades().iterator().next();
            assert (trade.getQuantity() == 0);
            assert (trade.getSignedQuantity() == 0);
            assert (positionManager.getPositionCount() == 0);
            assert (positionManager.getPosition("REL") == 0);
        }


        Transaction cancel = new Transaction(3L, 1l, 3, "REL", 15, Action.UPDATE, BuySell.BUY);
        transactionProcessor.process(cancel);


        {
            assert (tradeManager.getAllTrades().size() == 1);
            Trade trade = tradeManager.getAllTrades().iterator().next();
            assert (trade.getQuantity() == 0);
            assert (trade.getSignedQuantity() == 0);
            assert (positionManager.getPositionCount() == 0);
            assert (positionManager.getPosition("REL") == 0);
        }

    }

    @Test
    public void testIgnoreSmallerVersion() throws Exception {
        Transaction insert = new Transaction(1L, 1l, 3, "REL", 10, Action.UPDATE, BuySell.BUY);
        transactionProcessor.process(insert);

        {
            assert (tradeManager.getAllTrades().size() == 1);
            Trade trade = tradeManager.getAllTrades().iterator().next();
            assert (trade.getTradeId() == 1);
            assert (trade.getVersion() == 3);
            assert (trade.getSecurityCode() == "REL");
            assert (trade.getQuantity() == 10);
            assert (trade.getSignedQuantity() == 10);
            assert (trade.getBuySell() == BuySell.BUY);
        }

        Transaction update = new Transaction(2L, 1l, 2, "REL", 15, Action.UPDATE, BuySell.BUY);
        transactionProcessor.process(update);

        {
            assert (tradeManager.getAllTrades().size() == 1);
            Trade trade = tradeManager.getAllTrades().iterator().next();
            assert (trade.getTradeId() == 1);
            assert (trade.getVersion() == 3);
            assert (trade.getSecurityCode() == "REL");
            assert (trade.getQuantity() == 10);
            assert (trade.getSignedQuantity() == 10);
            assert (trade.getBuySell() == BuySell.BUY);
        }


        Transaction cancel = new Transaction(3L, 1l, 1, "REL", 15, Action.UPDATE, BuySell.BUY);
        transactionProcessor.process(cancel);


        {
            assert (tradeManager.getAllTrades().size() == 1);
            Trade trade = tradeManager.getAllTrades().iterator().next();
            assert (trade.getTradeId() == 1);
            assert (trade.getVersion() == 3);
            assert (trade.getSecurityCode() == "REL");
            assert (trade.getQuantity() == 10);
            assert (trade.getSignedQuantity() == 10);
            assert (trade.getBuySell() == BuySell.BUY);
        }

    }
}
