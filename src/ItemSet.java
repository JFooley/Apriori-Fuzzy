package src;
import java.util.*;

public class ItemSet {
    // Item = (x,y)
    Set<Pair> itemset;
    int class_index;
    double dataset_support, class_support;

    public ItemSet(int classe) {
        this.class_index = classe;
        this.itemset = new HashSet<>();
        this.dataset_support = 0;
        this.class_support = 0;
    }

    // Calcula o grau de pertinência dos valores da transação em cada label do itemset
    public double matchDegree(double[] transaction, MembershipFunction ms_function) { 
        double degree = 1.0;
        
        // Calcula o grau de pertinência da transação em cada label do itemset
        for (Pair it : this.itemset) {
            degree *= ms_function.regions[it.y()].fuzzify(transaction[it.x()]); // Pair = (variable, label)
        }

        return degree;
    }
}