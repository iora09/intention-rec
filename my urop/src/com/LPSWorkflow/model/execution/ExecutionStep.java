package com.LPSWorkflow.model.execution;

/**
 * Steps of operating semantics taken in execution
 */
public enum ExecutionStep {
    NONE, EXECUTE_EVENTS, PROCESS_REACTIVE_RULE, PROCESS_GOAL_CLAUSE
}
