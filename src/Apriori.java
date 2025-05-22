package src;

import java.util.HashMap;
import java.util.Map;

public class Apriori {
    public DataSet dataset;
    public RuleBase rule_base;

    public int depth;
    private int current_depth;

    public Map<Pair, Double> support_cache;

    public Apriori(DataSet dataset) {
        this.dataset = dataset;
        this.depth = dataset.config.depth;
    }
    
    public void generateRuleBase() {
        RuleBase temp_rule_base;
        
        this.rule_base = new RuleBase(this.dataset);
        this.support_cache = new HashMap<>();

        // Para cada label de cada classe de output
        for (int class_label_index = 0; class_label_index < this.dataset.attributeLabelCounts.get(this.dataset.inputAttributes.size()); class_label_index++) {
            temp_rule_base = new RuleBase(this.dataset);
            this.current_depth = 0;

            // Gera as regras para a label atual até a profundidade determinada.
            this.generateL2(temp_rule_base, class_label_index);
            this.generateLk(temp_rule_base, class_label_index);

            // Salva as regras criadas
            this.rule_base.addRules(temp_rule_base);
        }

        // Wracc global (reduz as regras)
    }

    private void generateL2(RuleBase rb, int current_class) {
        int label, attribute;
        Pair item;
        ItemSet item_set;

        // Para cada label de cada atributo de input
        for (attribute = 0; attribute < this.dataset.inputAttributes.size(); attribute++) {
            for (label = 0; label < this.dataset.attributeLabelCounts.get(attribute); label++) {
                // Cria o itemset
                item = new Pair(attribute, label);
                item_set = new ItemSet(current_class);
                item_set.add(item);

                // Calcula o suporte do itemset
                item_set.getSupport(this.dataset);

                // Salva o itemset se ele for relevante
                if (item_set.class_support >= this.dataset.config.min_support) rb.addRule(item_set);
            }
        }

        this.current_depth++;
    }

    private void generateLk(RuleBase rb, int current_class) {

    }
}
