package edu.kit.kastel.tva.eebc.typesystem;

import edu.kit.kastel.tva.eebc.lang.ast.BinOp;

import java.util.HashMap;
import java.util.Map;

public class TestHardwareModel implements HardwareModel {
    private int timeConst;
    private final Map<BinOp.Op, Integer> timeBinOp;
    private int timeIf;
    private int timeVarAssignment;
    private int timeVar;
    private final Map<String, Integer> timeCallCompFun;

    public TestHardwareModel() {
        this.timeConst = 0;
        this.timeBinOp = new HashMap<>();
        this.timeIf = 0;
        this.timeVarAssignment = 0;
        this.timeVar = 0;
        this.timeCallCompFun = new HashMap<>();
    }

    @Override
    public int timeDependentEnergyConsumption(ComponentState state, int time) {
        return time; // todo
    }

    @Override
    public int timeConst() {
        return timeConst;
    }

    public void setTimeConst(int timeConst) {
        this.timeConst = timeConst;
    }

    @Override
    public int timeBinOp(BinOp.Op op) {
        return timeBinOp.get(op);
    }

    public void putTimeBinOp(BinOp.Op op, int time) {
        timeBinOp.put(op, time);
    }

    @Override
    public int timeIf() {
        return timeIf;
    }

    public void setTimeIf(int timeIf) {
        this.timeIf = timeIf;
    }

    @Override
    public int timeVarAssignment() {
        return timeVarAssignment;
    }

    public void setTimeVarAssignment(int timeVarAssignment) {
        this.timeVarAssignment = timeVarAssignment;
    }

    @Override
    public int timeVar() {
        return timeVar;
    }

    public void setTimeVar(int timeVar) {
        this.timeVar = timeVar;
    }

    @Override
    public int timeCallCompFun(String name) {
        return timeCallCompFun.get(name);
    }

    public void putTimeCallCompFun(String name, int time) {
        timeCallCompFun.put(name, time);
    }

    @Override
    public boolean isDeterministic(String funName) {
        return false;
    }
}
