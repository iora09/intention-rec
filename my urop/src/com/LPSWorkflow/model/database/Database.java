package com.LPSWorkflow.model.database;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Database that holds the current state of the program (fluents/conditions...)
 * Database holds Facts (can change) and Clauses (holds for the program).
 */
public class Database {
    private static Database instance = null;

    public final static Database getInstance() {
        if (Database.instance == null) {
            synchronized (Database.class) {
                if (Database.instance == null) {
                    Database.instance = new Database();
                }
            }
        }
        return instance;
    }

    /* Facts property */
    private StringProperty facts = new SimpleStringProperty(this, "str", "");
    public StringProperty factsProperty(){
        return facts;
    }
    public final void setFacts(String str){
        facts.set(str);
    }
    public final String getFacts(){
        return facts.get();
    }

    /* Executed Actions property */
    private StringProperty executedActions = new SimpleStringProperty(this, "str", "");
    public StringProperty executedActionsProperty(){
        return executedActions;
    }
    public final void setExecutedActions(String str){
        executedActions.set(str);
    }
    public final String getExecutedActions(){
        return executedActions.get();
    }

    /* Clauses property */
    private ListProperty<Clause> clauses = new SimpleListProperty<>(FXCollections.<Clause>observableArrayList());
    public ListProperty<Clause> clausesProperty(){
        return clauses;
    }
    public final List<Clause> getClauses(){
        return clauses.get();
    }
    public void setClauses(ObservableList<Clause> clauses) {
        this.clauses.set(clauses);
    }

    /* Satisfied Clauses property : head of the clauses that were not in Facts */
    private ListProperty<Clause> satisfiedClauses = new SimpleListProperty<>(FXCollections.<Clause>observableArrayList());
    public ListProperty<Clause> satisfiedClausesProperty(){
        return satisfiedClauses;
    }
    public final List<Clause> getSatisfiedClauses(){
        return satisfiedClauses.get();
    }
    public void setSatisfiedClauses(ObservableList<Clause> clauses) {
        this.satisfiedClauses.set(clauses);
    }
}
