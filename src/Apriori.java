package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Apriori {
    public DataSet dataset;          // Conjunto de dados para mineração
    public RuleBase rule_base;       // Base de regras gerada
    
    public int depth;                // Profundidade máxima das regras (quantidade máxima de antecedentes)
    
    public List<ItemSet> L2;                        // Conjunto de itemsets frequentes de tamanho 2
    public Map<Item, double[]> membership_cache;    // Cache de graus de pertinência das labels

    public Apriori(DataSet dataset) {
        this.dataset = dataset;
        this.depth = dataset.config.depth;
        this.membership_cache = new HashMap<>();
    }
    
    // Função principal: gera a base de regras usando Apriori Fuzzy
    public void generateRuleBase() {
        RuleBase temp_rule_base;
        
        this.rule_base = new RuleBase(this.dataset);

        // Itera sobre todas as labels de classe de output
        for (int class_label_index = 0; class_label_index < this.dataset.attributeLabelCounts.get(this.dataset.inputAttributes.size()); class_label_index++) {
            temp_rule_base = new RuleBase(this.dataset);

            // Gera itemsets frequentes de tamanho 2 (L2)
            this.generateL2(temp_rule_base, class_label_index);
            // Gera itemsets frequentes de tamanho k (Lk) recursivamente
            this.generateLk(temp_rule_base, class_label_index, this.L2);

            // Adiciona as regras geradas para esta classe à base principal
            this.rule_base.addRules(temp_rule_base);
        }
    }

    // Gera itemsets frequentes de tamanho 2 (L2)
    private void generateL2(RuleBase rb, int current_class) {
        int label, attribute;
        Item item;
        ItemSet item_set;

        this.L2 = new ArrayList<>();

        // Itera sobre todos os atributos de entrada e suas labels
        for (attribute = 0; attribute < this.dataset.inputAttributes.size(); attribute++) {
            for (label = 0; label < this.dataset.attributeLabelCounts.get(attribute); label++) {
                // Cria um itemset com um único item (atributo, label)
                item = new Item(attribute, label);
                item_set = new ItemSet(current_class);
                item_set.add(item);

                // Calcula o suporte do itemset no dataset
                item_set.getSupport(this.dataset, this.membership_cache);

                // Adiciona ao L2 se atender ao suporte mínimo relativo à classe
                if (item_set.class_support >= this.dataset.config.min_support * this.dataset.classFrequency.get(current_class)) {
                    this.L2.add(item_set);
                }
            }
        }

        // Gera regras a partir dos itemsets frequentes de tamanho 2
        this.generateRules(rb, current_class, this.L2);
    }

    // Gera itemsets frequentes de tamanho k (Lk) a partir de Lk-1
    private void generateLk(RuleBase rb, int current_class, List<ItemSet> Lk) {
        List<ItemSet> Lnew;
        ItemSet newItemset, itemseti, itemsetj;

        int size = Lk.size();
        
        // Verifica se é possível gerar itemsets maiores
        if (size > 1 && (Lk.get(0)).size() < this.dataset.inputAttributes.size() && Lk.get(0).size() < this.depth) {
            Lnew = new ArrayList<>();

            // Combina pares de itemsets para gerar candidatos
            for (int i = 0; i < size - 1; i++) {
                itemseti = Lk.get(i);
                for (int j = i + 1; j < size; j++) {
                    itemsetj = Lk.get(j);
                    
                    // Debug: mostra combinação sendo testada
                    System.out.println("i = " + itemseti.toString());
                    System.out.println("j = " + itemsetj.toString());
                    System.out.println("Pode combinar? " + (itemseti.get(itemsetj.size() - 1).x() < itemsetj.get(itemsetj.size() - 1).x()));
                    
                    // Verifica se os itemsets podem ser combinados (critério de ordenação)
                    if (itemseti.get(itemsetj.size() - 1).x() < itemsetj.get(itemsetj.size() - 1).x()) {
                        // Combina os itemsets criando um novo itemset
                        newItemset = itemseti.clone();
                        newItemset.add(itemsetj.getLast());
                        newItemset.getSupport(dataset, this.membership_cache);

                        // Debug: Mostra se o novo itemset atende ao suporte mínimo
                        System.out.println(newItemset.toString());
                        System.out.println("Suporte: " + newItemset.class_support + " " + (newItemset.class_support >= this.dataset.config.min_support * this.dataset.classFrequency.get(current_class)));

                        // Adiciona ao novo conjunto se atender ao suporte mínimo relativo à classe
                        if (newItemset.class_support >= this.dataset.config.min_support * this.dataset.classFrequency.get(current_class)) {
                            Lnew.add(newItemset);
                        }
                    }
                }

                // Gera regras a partir dos novos itemsets
                this.generateRules(rb, current_class, Lnew);
                // Chamada recursiva para gerar itemsets ainda maiores
                this.generateLk(rb, current_class, Lnew);
                Lnew.clear();
            }
        }
    }

    // Gera regras de associação a partir de itemsets frequentes
    private void generateRules(RuleBase rb, int current_class, List<ItemSet> Lk) {
        double lift;
        int i;
		ItemSet itemset;

        // Itera sobre os itemsets em ordem reversa para permitir remoção
        for (i = Lk.size() - 1; i >= 0; i--) {
            itemset = Lk.get(i);

            // Calcula o lift (elevação) da regra
            if (itemset.dataset_support > 0.0) {
                lift = itemset.class_support / (itemset.dataset_support * this.dataset.classFrequency.get(current_class));
            } else {
                lift = 0.0;
            }

            // Se o lift for maior que 1 (regra relevante), adiciona à base
            if (lift > 1.0) {
                itemset.lift = lift;
                rb.addRule(itemset);
                Lk.remove(i); // Remove o itemset que virou regra para evitar regras redundantes
            }
        }
    }
}