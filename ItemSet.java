import java.util.*;

public class ItemSet {
    Set<int[]> itemset;
    int classIndex;
    double datasetSupport, classSupport;

    public ItemSet(int classe) {
        this.classIndex = classe;
        this.itemset = new HashSet<>();
        this.datasetSupport = 0;
        this.classSupport = 0;
    }

    public double matchDegree(double[] transaction, Region[][] membershipFunctions) {
        double degree = 1.0;
        
        // Calcula o grau de pertinência da transação em cada label do itemset
        for (int[] item : this.itemset) {
            degree *= membershipFunctions[item[0]][item[1]].fuzzify(transaction[item[0]]);
        }

        return degree;
    }

    public boolean equalsTo(ItemSet target) {
        
        return false;
    }
}