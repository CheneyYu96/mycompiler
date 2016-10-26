package analyzer;

import util.NFA;
import util.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by yuminchen on 16/10/22.
 */
public class NFAHandler {

    private int stateIndex = 0;

    private Stack<NFA> nfaStack;

    public NFAHandler() {
        nfaStack = new Stack<>();
    }

    /**
     * make RE to NFA one by one
     * @param exprs
     * @return
     */
    public List<NFA> expr2NFA(List<String> exprs){

        List<NFA> nfas = new ArrayList<>();

        for( String expr : exprs){

            for(int i=0; i<expr.length(); i++){

                // union
                if(expr.charAt(i)=='|'){
                    NFA nfa1 = nfaStack.pop();
                    NFA nfa2 = nfaStack.pop();
                    nfaStack.push(nfa1.union(nfa2,new State(stateIndex++), new State(stateIndex++)));
                    continue;
                }

                // connect
                else if(expr.charAt(i)=='.'){
                    NFA nfa1 = nfaStack.pop();
                    NFA nfa2 = nfaStack.pop();
                    nfaStack.push(nfa1.connect(nfa2));
                    continue;
                }

                // ring
                else if(expr.charAt(i)=='*'){
                    NFA nfa = nfaStack.pop();
                    NFA nfa1 = nfa.ring(new State(stateIndex++));
                    nfaStack.push(nfa1);
                    continue;
                }

                // state
                else {

                    State state1 = new State(stateIndex++);
                    State state2 = new State(stateIndex++);
                    state1.addEdge(expr.charAt(i), state2);
                    NFA newNFA = new NFA(state1);
                    List<State> newEndStates = new ArrayList<>();
                    newEndStates.add(state2);
                    newNFA.setEndStates(newEndStates);
                    nfaStack.push(newNFA);
                }
            }

            NFA nfa = nfaStack.pop();
            nfas.add(nfa);
            nfaStack.clear();

        }
        return nfas;
    }

    /**
     * merge all nfa to a nfa
     * @param nfas
     * @return
     */
    public NFA mergeAll(List<NFA> nfas){
        for (int i = nfas.size()-1; i > 0; i--){
            nfas.get(i-1).merge(nfas.get(i),new State(stateIndex));
        }
        return nfas.get(0);
    }

    public void printNFA(NFA nfa){
        System.out.println("begin "+ nfa.getBeginState().getStateID());
        nfa.getEndStates().forEach(o -> System.out.println("end " + o.getStateID()));
        nfa.getAllStates().forEach(o -> o.printNext());

    }

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        NFAHandler handler = new NFAHandler();
        handler.printNFA(handler.mergeAll(handler.expr2NFA(new REHandler().handle())));
    }
}
