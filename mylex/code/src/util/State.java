package util;

import java.util.*;

/**
 * Created by yuminchen on 16/10/24.
 */
public class State {

    private int stateID;

    public int getStateID() {
        return stateID;
    }


    /**
     * used in dfa optimization
     */
    private Map<Character,EquivalClass> equivalClassMap;

    /**
     * used in construct dfa
     */
    private Set<State> containedStates;

    private Map<Character, List<State>> nextState;

    public Map<Character, List<State>> getNextState() {
        return nextState;
    }

    public State(int stateID) {
        this.stateID = stateID;
        containedStates = new HashSet<>();
        nextState = new HashMap<>();
        equivalClassMap = new HashMap<>();
    }

    public EquivalClass getRelatedEquivalClass(char key){
        return equivalClassMap.get(key);
    }

    public void setRelatedEquivalClass(char key, EquivalClass equivalClass){
        equivalClassMap.put(key,equivalClass);
    }

    public void addContainedStates(Set<State> addStates){
        containedStates.addAll(addStates);
    }

    public Set<State> getContainedStates() {
        return containedStates;
    }

    /**
     * add edges
     * @param edges
     */
    public void addEdge(Map<Character, List<State>> edges){
        nextState.putAll(edges);
    }

    /**
     * add edge by key and next
     * @param key
     * @param next
     */
    public void addEdge(char key, State next){
        List <State> list = this.nextState.get(key);

        if (list == null) {
            list = new ArrayList<>();
            this.nextState.put(key, list);
        }

        list.add(next);
    }

    /**
     * get edge
     * @param ch
     * @return
     */
    public List<State> getStateByEdge(char ch){
        return nextState.get(ch);
    }

    /**
     * test
     */
    public void printNext(){
        for(char key : nextState.keySet()){
            List<State> states = nextState.get(key);
            states.stream().forEach(o -> System.out.println(stateID+ " "+ key+ "=> " + o.getStateID()));
        }
    }

    /**
     *
     * @return
     */
    public boolean haseEdge(){
        return nextState.keySet().contains('e');
    }

    /**
     * get epsilon edge
     * @return
     */
    public List<State> getEStates(){

        if (haseEdge()){
            return nextState.get('e');
        }
        System.err.println("no eplison edge");
        return null;
    }
}