package com.LPSWorkflow.model.execution;

import com.LPSWorkflow.model.execution.strategy.GreedyStrategy;
import com.LPSWorkflow.model.execution.strategy.LookAheadStrategy;
import com.LPSWorkflow.model.execution.strategy.RandomStrategy;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * deals with strategies for automatic execution
 */
public class StrategyData {
    private static StrategyData instance = null;

    public static StrategyData getInstance() {
        if (StrategyData.instance == null) {
            synchronized (StrategyData.class) {
                if (StrategyData.instance == null) {
                    StrategyData.instance = new StrategyData();
                }
            }
        }
        return instance;
    }

    public StrategyData(){
        strategyList.add(new GreedyStrategy());
        strategyList.add(new RandomStrategy());
        strategyList.add(new LookAheadStrategy());
        setSelectedStrategy(strategyList.get(0));
    }

    private ListProperty<Strategy> strategyList = new SimpleListProperty<>(FXCollections.<Strategy>observableArrayList());
    public ListProperty<Strategy> strategyListProperty(){
        return strategyList;
    }
    public final void setStrategyList(ObservableList<Strategy> list){
        strategyList.set(list);
    }
    public final List<Strategy> getStrategyList(){
        return strategyList.get();
    }

    private Property<Strategy> selectedStrategy = new SimpleObjectProperty<>();
    public Strategy getSelectedStrategy() {
        return selectedStrategy.getValue();
    }
    public Property<Strategy> selectedStrategyProperty() {
        return selectedStrategy;
    }
    public void setSelectedStrategy(Strategy selectedStrategy) {
        this.selectedStrategy.setValue(selectedStrategy);
    }
}
