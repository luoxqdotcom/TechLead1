package com.trafigura.equity.position;

import com.trafigura.equity.position.model.Position;
import com.trafigura.equity.position.model.PositionChange;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryPositionManager implements PositionManager {

    private ConcurrentHashMap<String, Long> positions = new ConcurrentHashMap<>();

    @Override
    public int getPositionCount() {
        return positions.size();
    }

    @Override
    public void update(PositionChange pc) {
        positions.compute(pc.getSecurityCode(), (code, q) -> q == null ? pc.getQuantity() : (q + pc.getQuantity()));
    }

    @Override
    synchronized public void update(List<PositionChange> pc) {
        for (PositionChange p : pc) {
            update(p);
        }
    }

    @Override
    public long getPosition(String securityCode) {
        return positions.getOrDefault(securityCode, 0L);
    }

    @Override
    public Iterable<Position> getAllPositions() {
        return positions.entrySet().stream().map(e -> new Position(e.getKey(), e.getValue())).collect(Collectors.toList());
    }
}
