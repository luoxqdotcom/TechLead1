package com.trafigura.equity.position;

import com.trafigura.equity.position.model.Action;
import com.trafigura.equity.position.model.BuySell;
import com.trafigura.equity.position.model.Transaction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PositionMaintenanceApplication {

    public static void main(String... args) {
        new PositionMaintenanceApplication().start();
    }

    TradeManager tradeManager = new InMemoryTradeManager();
    PositionManager positionManager = new InMemoryPositionManager();

    TransactionProcessor transactionProcessor = new DefaultTransactionProcessor(tradeManager, positionManager);

    void start() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Please enter transaction:");
                System.out.println("TransactionID\tTradeID\tVersion\tSecurityCode\tQuantity\tACTION\tBuy/Sell");
                String line = in.readLine();
                String[] values = line.split("\\s");
                List<String> errors = validate(values);
                if (errors.isEmpty()) {
                    Transaction t = new Transaction(Long.valueOf(values[0]), Long.valueOf(values[1]), Integer.valueOf(values[2]),
                            values[3].toUpperCase(), Long.valueOf(values[4]), Action.valueOf(values[5].toUpperCase()), BuySell.valueOf(values[6].toUpperCase()));
                    transactionProcessor.process(t);
                    positionManager.getAllPositions().forEach(p -> System.out.println(p.getSecurityCode() + ": " + p.getQuantity()));
                } else {
                    errors.forEach(s -> System.err.println(s));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    List<String> validate(String[] values) {
        Set<String> validActions = Arrays.asList(Action.values()).stream().map(a -> a.name()).collect(Collectors.toSet());
        List<String> buySell = Arrays.asList("BUY", "SELL");
        List<String> errors = new ArrayList<>();
        if (values.length != 7) errors.add("Require 7 input fields. But got " + values.length);
        else {
            if (!values[0].matches("[0-9]{1,10}")) errors.add("TransactionID need to be 1 to 10 digits.");
            if (!values[1].matches("[0-9]{1,10}")) errors.add("TradeID need to be 1 to 10 digits.");
            if (!values[2].matches("[0-9]{1,5}")) errors.add("TradeID need to be 1 to 5 digits.");
            if (!values[3].matches("[A-Za-z]{3}")) errors.add("SecurityCode need to be 3 letters.");
            if (!values[4].matches("[+\\-]?[0-9]{1,10}"))
                errors.add("Quantity need to be 1 to 10 digits with optional sign.");
            if (!validActions.contains(values[5].toUpperCase())) errors.add("Invalid action.");
            if (!buySell.contains(values[6])) errors.add("Invalid Buy/Sell.");
        }
        return errors;
    }
}
