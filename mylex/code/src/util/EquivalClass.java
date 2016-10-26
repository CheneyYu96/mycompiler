package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by yuminchen on 16/10/25.
 */
public class EquivalClass {

    private Set<State> stateSet;

    private int classIndex;

    /**
     * judge if it has been devided
     */
    private boolean hasNextClass;

    private Map<Character, EquivalClass> edgeMap;

    /**
     *
     * @param stateSet
     * @param classIndex
     */
    public EquivalClass(Set<State> stateSet,int classIndex) {
        this.classIndex = classIndex;
        this.stateSet = stateSet;
        hasNextClass = false;
        edgeMap = new HashMap<>();
    }

    public Set<State> getStateSet() {
        return stateSet;
    }

    public int getClassIndex() {

        return classIndex;
    }

    public Map<Character, EquivalClass> getEdgeMap() {
        return edgeMap;
    }

    public boolean isHasNextClass() {
        return hasNextClass;
    }

    public void addEdgeMap(char key, EquivalClass equivalClass){
        edgeMap.put(key,equivalClass);
    }

    public void setHasNextClass(boolean hasNextClass) {
        this.hasNextClass = hasNextClass;
    }

    /**
     *
     * @param state
     * @return
     */
    public boolean isInClass(State state){
        if(stateSet.contains(state)){
            return true;
        }
        return false;
    }

    public void print(){
        for (char key : edgeMap.keySet()){
            if(edgeMap.get(key)!=null) {
                System.out.println(classIndex + " " + key + " => " + edgeMap.get(key).getClassIndex());
            }
        }

    }


}
