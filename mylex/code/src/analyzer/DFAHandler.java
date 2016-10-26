package analyzer;

import util.DFA;
import util.NFA;
import util.State;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/10/22.
 */
public class DFAHandler {

    private int stateIndex = 0;

    private Set<State> epsilonSet;
    private Set<State> charSet;


    public DFA NFA2DFA(NFA nfa){

        // to restore states that uncertain edge
        // use BFS
        Stack<State> stateStack = new Stack<>();

        epsilonSet = new HashSet<>();
        charSet = new HashSet<>();

        // initial dfa
        State beginState = nfa.getBeginState();
        getEpsilonClosure(new HashSet<>(Arrays.asList(beginState)));
        State first = new State(stateIndex++);
        first.addContainedStates(epsilonSet);
        DFA dfa = new DFA(first);
        stateStack.push(first);

        while (!stateStack.isEmpty()) {

            State popState = stateStack.pop();

            // if end state
            if(setContainOther(popState.getContainedStates(),nfa.getEndStates())){
                dfa.addEndState(popState);
            }
            // get all edge char from epsilon closure
            Set<Character> allChar = new HashSet<>();
            for (State state: popState.getContainedStates()){
                allChar.addAll(state.getNextState().keySet());
            }

            allChar.remove('e');

            // certain all edge
            for (char key : allChar){

                epsilonSet.clear();
                charSet.clear();

                getKeyEdge(key, popState.getContainedStates());
                getEpsilonClosure(charSet);

                // if the closure is repeated in exist state list
                if(closureRepeated(epsilonSet,dfa)){

                    State repeatedState = null;
                    for(State state : dfa.getAllStates()){
                       if (setEquals(epsilonSet,state.getContainedStates())){
                           repeatedState = state;
                           break;
                       }
                    }
                    popState.addEdge(key,repeatedState);
                    continue;
                }

                State next = new State(stateIndex++);
                next.addContainedStates(epsilonSet);

                popState.addEdge(key,next);
                dfa.addAllStates(Arrays.asList(next));
                stateStack.push(next);
            }
        }

        return dfa;
    }


    /**
     *
     * @param set1
     * @param set2
     * @return
     */
    private boolean setContainOther(Set<State> set1, List<State> set2){
        List<Integer> list1 = set1
                .stream()
                .map( o -> o.getStateID())
                .collect(Collectors.toList());

        for (State state : set2){
            if(list1.contains(state.getStateID())){
                return true;
            }
        }
       return false;
    }
    /**
     *
     * @param set
     * @param dfa
     * @return
     */
    private boolean closureRepeated(Set<State> set, DFA dfa){
        List<Set<State>> list = dfa
                .getAllStates()
                .stream()
                .map( state -> state.getContainedStates())
                .collect(Collectors.toList());

       for (Set<State> states : list){
           if (setEquals(set,states)){
               return true;
           }
       }

        return false;
    }

    /**
     *
     * @param set1
     * @param set2
     * @return
     */
    private boolean setEquals(Set<State> set1, Set<State> set2){

        boolean isContained = true;
        if(set1.size()!=set2.size()){
            return false;
        }

        List<Integer> indexes = set1
                .stream()
                .map(o -> o.getStateID())
                .collect(Collectors.toList());

        for(State state : set2){
            if(!indexes.contains(state.getStateID())){
                isContained = false;
                break;
            }
        }

        if(isContained){
            return true;
        }

        return false;
    }
    /**
     *
     * @param states
     */
    private void getEpsilonClosure(Set<State> states) {

        for( State state : states) {
            epsilonSet.add(state);
            if (state.haseEdge()) {
                List<State> eStates = state.getEStates();
                eStates.forEach(o -> getEpsilonClosure(new HashSet<>(Arrays.asList(o))));
            }
        }

    }

    /**
     * get a or b edge
     * @param key
     * @param states
     */
    private void getKeyEdge(char key, Set<State> states){
        for (State state : states){
            List<State> states1 = state.getStateByEdge(key);
            if(states1!=null) {
                charSet.addAll(states1);
            }
        }
    }

    /**
     * print dfa
     * @param dfa
     */
    public void printDFA(DFA dfa){
        System.out.println("begin "+ dfa.getBeginState().getStateID());
        dfa.getEndStates().forEach(o -> System.out.println("end " + o.getStateID()));
        dfa.getAllStates().forEach(o -> o.printNext());

    }

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        NFAHandler handler = new NFAHandler();
        NFA nfa = handler.mergeAll(handler.expr2NFA(new REHandler().handle()));
        DFAHandler dfaHandler = new DFAHandler();
        DFA dfa = dfaHandler.NFA2DFA(nfa);
        dfaHandler.printDFA(dfa);

    }
}
