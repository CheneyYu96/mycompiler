package util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/10/24.
 */
public class DFA {

    private State beginState;

    private Set<State> allStates;

    private Set<State> endStates;

    public State getBeginState() {
        return beginState;
    }

    /**
     *
     * @param beginState
     */
    public DFA(State beginState) {
        allStates = new HashSet<>();
        endStates = new HashSet<>();
        setBeginState(beginState);
    }

    public DFA() {
        allStates = new HashSet<>();
        endStates = new HashSet<>();
    }

    public Set<State> getAllStates() {
        return allStates;
    }

    public void setBeginState(State state){
        this.beginState = state;
        addAllStates(Arrays.asList(state));
    }

    public void addEndState(State endState) {
        this.endStates.add(endState);
        addAllStates(Arrays.asList(endState));
    }

    public Set<State> getEndStates() {
        return endStates;
    }

    public void addEndStates(List<State> endStates) {
        addAllStates(endStates);
        this.endStates.addAll(endStates);
    }

    public void addAllStates(List<State> states){
        // filter the repeated states
        allStates.addAll(states);

    }

}
