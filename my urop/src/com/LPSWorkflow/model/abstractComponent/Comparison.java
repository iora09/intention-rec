package com.LPSWorkflow.model.abstractComponent;

import com.LPSWorkflow.common.EntityType;

import java.util.Arrays;
import java.util.List;

/**
 * Comparison entity (==, !=, <, >, <=, >=)
 */
public class Comparison extends Entity{
    private String name1;
    private String op;
    private String name2;

    public Comparison(String name1, String op, String name2) {
        super(name1 + op + name2, Arrays.asList(name1, name2));
        this.name1 = name1;
        this.op = op;
        this.name2 = name2;
    }

    public String getName1() {
        return name1;
    }

    public String getOp() {
        return op;
    }

    public String getName2() {
        return name2;
    }

    @Override
    public String getFullName() {
        return getName();
    }

    @Override
    public List<String> getTerms() {
        return Arrays.asList(name1, name2);
    }

    @Override
    public List<String> getTermsClone() {
        return getTerms();
    }

    @Override
    public EntityType getType() {
        return EntityType.COMPARISON;
    }

    @Override
    public Entity clone() {
        return new Comparison(name1, op, name2);
    }
}
