package src;
import java.util.*;

public class ItemSet {
    // Pair = (attribute, label)
    List<Pair> itemset = new ArrayList<>();
    int class_index;
    double dataset_support, class_support, lift;

    private ItemSet(ItemSet is) {
        this.itemset = is.itemset;
        this.class_index = is.class_index;
        this.dataset_support = is.dataset_support;
        this.class_support = is.class_support;
    }

    public ItemSet(int classe) {
        this.class_index = classe;
        this.dataset_support = 0;
        this.class_support = 0;
    }

    public void getSupport(DataSet dataset) {
        this.dataset_support = 0f;
        this.class_support = 0f;
        Double degree;

        // Para cada transação
        for (int i = 0; i < dataset.data.size(); i++) {
            // Calcula o grau de pertinência do itemset na transação especificada
            degree = this.matchDegree(dataset.data.get(i), dataset);

            // Adiciona esse grau ao suporte geral e ao suporte de classe, caso seja da classe compatível
            this.dataset_support += degree;
            if (dataset.getTransOutput(i) == this.class_index) this.class_support += degree;
        }

        // Normaliza os valores
        this.dataset_support /= dataset.data.size();
        this.class_support /= dataset.data.size();
    }

    public double matchDegree(String[] transaction, DataSet dataset) { 
        double degree = 1.0;
         
        // Calcula o grau de pertinência de cada label do itemset na transação
        for (Pair ant : this.itemset) {
            degree *= dataset.msfunctions.get(ant.x()).regions[ant.y()].fuzzify(Double.parseDouble(transaction[ant.x()])); // Pair = (variable, label)
        }

        return degree;
    }

    public void add(Pair item) {
        this.itemset.add(item);
    }

    public Pair get(int index) {
        return itemset.get(index);
    }

    public int size() {
        return itemset.size();
    }

    public ItemSet clone() {
        return new ItemSet(this);
    }

    @Override
    public String toString() {
        String str = "";
        for (Pair pair : itemset) {
            str += "(" + pair.x() + ", " + pair.y() + ") ";
        }
        str += "-> " + this.class_index;
        return str;
    }
}