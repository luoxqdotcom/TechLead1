package com.trafigura.equity.position;

import com.trafigura.equity.position.model.Position;
import com.trafigura.equity.position.model.PositionChange;

import java.util.List;

public interface PositionManager {
    void update(PositionChange pc);
    void update(List<PositionChange> pc);

    long getPosition(String securityCode);
    Iterable<Position> getAllPositions();
    int getPositionCount();
}
