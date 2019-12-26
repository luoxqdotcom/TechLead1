package com.trafigura.equity.position;

import com.trafigura.equity.position.model.Transaction;

public interface TransactionProcessor {
    void process(Transaction txn);
}
