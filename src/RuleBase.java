package src;

import java.util.ArrayList;
import java.util.List;

public class RuleBase {
    public DataSet data; // Conjunto de dados e configurações
    public List<ItemSet> rules = new ArrayList<>(); // Regras

    public RuleBase(DataSet dataset) {
        this.data = dataset;
    }

    public void addRule(ItemSet rule) {
        this.rules.add(rule);
    }

    public void addRules(RuleBase rule_base) {
        this.rules.addAll(rule_base.rules);
    }

    public void printRules() {
        for (ItemSet itemset : this.rules) {
            System.out.println(itemset.toString(this.data));
        }
    }
}
