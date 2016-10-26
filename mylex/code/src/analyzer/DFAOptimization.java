package analyzer;

import util.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuminchen on 16/10/22.
 */
public class DFAOptimization {

    private DFA dfa;

    private int classIndex = 0;

    private List<EquivalClass> classList;


    public DFAOptimization(DFA dfa) {
        classList = new ArrayList<>();
        this.dfa = dfa;
    }

    /**
     * optimize dfa
     * @return
     */
    public OptimizedDFA optimization(){

        // store equivalence class
        // first divide into 2 class
        Set<State> end = new HashSet<>(dfa.getEndStates());
        Set<State> other = dfa
                .getAllStates()
                .stream()
                .filter( state -> !dfa.getEndStates().contains(state))
                .collect(Collectors.toSet());

        EquivalClass endClass = new EquivalClass(end ,classIndex++);
        classList.add(endClass);
        EquivalClass otherClass = new EquivalClass(other, classIndex++);
        classList.add(otherClass);

        Set<Character> keySet = new HashSet<>();
        keySet.add('a');
        keySet.add('b');

        /*
        set recognized equivalence class to classList
        to get last equivalence class list
         */
        for(int i =0 ;i < classList.size(); i++){
            for(char key : keySet){
                if(classList.get(i).isHasNextClass()){
                    break;
                }
                List<EquivalClass> tmp = classify(key,classList.get(i));
                if(tmp!=null) {
                    classList.addAll(tmp);
                }
            }
        }

        /*
        get final equivalence class in optimized dfa
         */
        List<EquivalClass> lastList = classList
                .stream()
                .filter( o->!o.isHasNextClass()).collect(Collectors.toList());


        EquivalClass begin = lastList
                .stream()
                .filter( o -> o.getStateSet().contains(dfa.getBeginState()))
                .collect(Collectors.toList())
                .get(0);

        OptimizedDFA optimizedDFA = new OptimizedDFA(begin);

        optimizedDFA.setAll(new HashSet<>(lastList));

        /*
        get end equivalence class basing on basical end state in initial dfa
         */
        Set<State> basicEndState = dfa.getEndStates();

        List<EquivalClass> equivalClasses = new ArrayList<>();

        for(State state : basicEndState){

            List<EquivalClass> tmp = lastList
                    .stream()
                    .filter( o -> o.getStateSet().contains(state))
                    .collect(Collectors.toList());

            equivalClasses.addAll(tmp);
        }


        optimizedDFA.addEndClasses(equivalClasses);
        return optimizedDFA;
    }

    /**
     * classify by key char and the basic equivalence class
     * @param key
     * @param equivalClass
     * @return
     */
    private List<EquivalClass> classify(char key, EquivalClass equivalClass){


        // certain related equivalence class
        Map<State,EquivalClass> stateEquivalMap = new HashMap<>();

        for(State state : equivalClass.getStateSet()){

            // blank edge
            if(state.getStateByEdge(key)==null){
                stateEquivalMap.put(state,null);
                continue;
            }

            State next = state.getStateByEdge(key).get(0);
            for (int i = 0; i < classList.size(); i++) {
                if (!classList.get(i).isHasNextClass() && classList.get(i).isInClass(next)) {
                    state.setRelatedEquivalClass(key, classList.get(i));
                    stateEquivalMap.put(state,classList.get(i));
                    break;
                }
            }

        }
        List<EquivalClass> aimClass = stateEquivalMap
                .values()
                .stream()
                .distinct()
                .collect(Collectors.toList());

        // blank edge or ring edge
        if(aimClass.size()==1){
            equivalClass.addEdgeMap(key,aimClass.get(0));
            return null;
        }

        List<State> states = stateEquivalMap.keySet().stream().collect(Collectors.toList());

        List<EquivalClass> returnedClass = new ArrayList<>();

        for(EquivalClass equivalClass1 : aimClass){
            Set<State> stateSet = new HashSet<>();
            for(State state : states){
                if(state.getRelatedEquivalClass(key)==equivalClass1){
                    stateSet.add(state);
                }
            }
            // set relationship between equivalence class
            EquivalClass tmp = new EquivalClass(stateSet,classIndex++);
            tmp.addEdgeMap(key,equivalClass1);
            returnedClass.add(tmp);
        }
        equivalClass.setHasNextClass(true);

        return returnedClass;
    }

    private void print(OptimizedDFA optimizedDFA){

        System.out.println("begin : "+optimizedDFA.getBeginClass().getClassIndex());
        optimizedDFA.getEndClass().forEach(o -> System.out.println("end : "+ o.getClassIndex()));
        optimizedDFA.getAll().forEach(o -> o.print());

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
//        dfaHandler.printDFA(dfa);
        DFAOptimization dfaOptimization = new DFAOptimization(dfa);

        dfaOptimization.print(dfaOptimization.optimization());




    }
}
