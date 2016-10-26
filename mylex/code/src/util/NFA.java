package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/10/24.
 */
public class NFA {

    private State beginState;

    private List<State> allStates;

    private List<State> endStates;

    public State getBeginState() {
        return beginState;
    }

    public List<State> getAllStates() {
        return allStates;
    }

    public NFA(State beginState) {
        this.allStates = new ArrayList<>();
        this.endStates = new ArrayList<>();
        setBeginState(beginState);
    }

    public NFA(State beginState, State endState) {
        this(beginState);
        this.endStates.add(endState);
        this.allStates.add(endState);
    }

    public void setBeginState(State state){
        this.beginState = state;
        addAllStates(Arrays.asList(state));
    }

    public void addEndState(State endState) {
        this.endStates.add(endState);
        addAllStates(Arrays.asList(endState));
    }

    public List<State> getEndStates() {
        return endStates;
    }

    public void setEndStates(List<State> endStates) {
//        allStates.removeAll(this.endStates);
        this.endStates = endStates;
        addAllStates(endStates);
    }

    public void addEndStates(List<State> endStates) {
        addAllStates(endStates);
        this.endStates.addAll(endStates);
    }

    public void addAllStates(List<State> states){
        // filter the repeated states
        List<Integer> hasInList = allStates
                .stream()
                .map(o -> o.getStateID())
                .collect(Collectors.toList());
        List<State> addList = states
                                .stream()
                                .filter( o -> !hasInList.contains(o.getStateID()))
                                .collect(Collectors.toList());
        allStates.addAll(addList);


//        for(State state : states){
//            if (hasInList.contains(state.getStateID())) {
//
//                State hasState = allStates
//                        .stream()
//                        .filter(o -> o.getStateID()==state.getStateID())
//                        .collect(Collectors.toList())
//                        .get(0);
//
//                Map<Character, List<State>> hasNexts = hasState.getNextState();
//                Map<Character, List<State>> nexts = state.getNextState();
//
//                for (char ch : nexts.keySet()){
//
//                    if (hasNexts.keySet().contains(ch)){
//
//                        List<Integer> hasList = hasNexts
//                                .get(ch)
//                                .stream()
//                                .map( o -> o.getStateID())
//                                .collect(Collectors.toList());
//                        List<State> addList = nexts
//                                .get(ch)
//                                .stream()
//                                .filter( o -> !hasList.contains(o.getStateID()))
//                                .collect(Collectors.toList());
//                        List<State> all = hasNexts.get(ch);
//                        all.addAll(addList);
//                        hasNexts.put(ch , all);
//
//                    }
//                    else {
//                        hasNexts.put(ch, nexts.get(ch));
//                    }
//                }
//            }
//            else {
//                allStates.add(state);
//            }
    }

    public NFA merge(NFA other, State newBeginState){

        newBeginState.addEdge('e',this.beginState);
        newBeginState.addEdge('e',other.beginState);
        setBeginState(newBeginState);

        addEndStates(other.endStates);
        addAllStates(other.allStates);
        return this;
    }

    public NFA union(NFA other,  State newBeginState, State newEndState){

        newBeginState.addEdge('e',this.beginState);
        newBeginState.addEdge('e',other.beginState);
        setBeginState(newBeginState);

        this.endStates.stream().forEach(end -> end.addEdge('e', newEndState));
        other.endStates.stream().forEach(end -> end.addEdge('e', newEndState));
        List<State> newEndStates = new ArrayList<>();
        newEndStates.add(newEndState);

        setEndStates(newEndStates);
        addAllStates(other.allStates);
        return this;
    }

    public NFA connect(NFA other){

        // assume the number of end edge is 1
        other.endStates.get(0).addEdge('e',this.beginState);
        setBeginState(other.beginState);
        addAllStates(other.allStates);
        return this;
    }

    public NFA ring(State newState){

        newState.addEdge('e', beginState);
        endStates.stream().forEach(end -> end.addEdge('e', newState));
        setBeginState(newState);
        List<State> newEndStates = new ArrayList<>();
        newEndStates.add(newState);
        setEndStates(newEndStates);
        return this;
    }
}
