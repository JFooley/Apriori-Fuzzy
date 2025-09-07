package src;
import java.util.*;

public class ItemSet {
    List<Item> itemset = new ArrayList<>();
    int class_index;
    double dataset_support, class_support, lift;

    private ItemSet(ItemSet is) {
        this.itemset = new ArrayList<>(is.itemset);
        this.class_index = is.class_index;
        this.dataset_support = is.dataset_support;
        this.class_support = is.class_support;
    }

    public ItemSet(int classe) {
        this.class_index = classe;
        this.dataset_support = 0;
        this.class_support = 0;
    }

    // Calcula o suporte do itemset no dataset e na classe especificada dele
    public void getSupport(DataSet dataset, Map<Item, double[]> membership_cache) {
        this.dataset_support = 0f;
        this.class_support = 0f;
        Double degree;

        // Para cada transação
        for (int i = 0; i < dataset.data.size(); i++) {
            // Calcula o grau de pertinência do itemset na transação especificada
            degree = this.matchDegree(i, dataset, membership_cache);

            // Adiciona esse grau ao suporte geral e ao suporte de classe, caso seja da classe compatível
            this.dataset_support += degree;
            if (dataset.getTransOutput(i) == this.class_index) this.class_support += degree;
        }

        // Normaliza os valores
        this.dataset_support /= dataset.data.size();
        this.class_support /= dataset.data.size();
    }

    // Calcula o grau de pertinência do itemset na transação especificada
    public double matchDegree(int transactionIndex, DataSet dataset, Map<Item, double[]> membership_cache) { 
        double degree = 1f;
        int n_ants = 0;

        String[] transaction = dataset.data.get(transactionIndex);

        // Calcula o grau de pertinência de cada label do itemset na transação
        for (Item ant : this.itemset) {
            if (membership_cache.containsKey(ant) && membership_cache.get(ant)[transactionIndex] != 0) { 
                // Recupera do cache a pertinencia calculada da label para a transação
                degree *= membership_cache.get(ant)[transactionIndex]; 
                n_ants++;

            } else { 
                // Calcula o grau de pertinência da label
                Double value = dataset.msfunctions.get(ant.variable()).regions[ant.label()].fuzzify(Double.parseDouble(transaction[ant.variable()])); // Item = (variable, label)
                degree *= value;
                n_ants++;

                // Salva no cache o valor calculado no cache
                if (!membership_cache.containsKey(ant)) membership_cache.put(ant, new double[dataset.data.size()]);
                membership_cache.get(ant)[transactionIndex] = value;
            }
        }

        // Calcula a media geométrica dos graus de pertinência
        if (n_ants > 0) degree = Math.pow(degree, (double) 1/n_ants);

        return degree;
    }

    // Metodos auxiiliares
    public void add(Item item) {
        this.itemset.add(item);
    }

    public Item get(int index) {
        return itemset.get(index);
    }

    public Item getLast() {
        return itemset.getLast();
    }

    public int size() {
        return itemset.size();
    }

    @Override
    public ItemSet clone() {
        return new ItemSet(this);
    }

    @Override
    public String toString() {
        String str = "";
        for (Item pair : itemset) {
            str += "(" + pair.variable() + ", " + pair.label() + ") ";
        }
        str += "-> " + this.class_index;
        return str;
    }
}