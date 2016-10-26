package util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yuminchen on 16/10/25.
 */
public class OptimizedDFA {


    private EquivalClass beginClass;

    private Set<EquivalClass> endClass;

    private Set<EquivalClass> all;

    /**
     *
     * @param beginClass
     */
    public OptimizedDFA(EquivalClass beginClass) {
        this.beginClass = beginClass;
        endClass = new HashSet<>();
        all = new HashSet<>();
    }

    public EquivalClass getBeginClass() {
        return beginClass;
    }


    public void addEndClass(EquivalClass equivalClass){
        endClass.add(equivalClass);

    }

    public void addEndClasses(List<EquivalClass> equivalClass){
        endClass.addAll(equivalClass);

    }

    public Set<EquivalClass> getEndClass() {
        return endClass;
    }

    public Set<EquivalClass> getAll() {
        return all;
    }

    public void setAll(Set<EquivalClass> all) {
        this.all = all;
    }
}
