package src;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Override
    public String toString() {
        String output = "Regras:\n";
        for (ItemSet itemset : this.rules) {
            output += itemset.toString(this.data) + "\n";
        }
        return output;
    }

    public void exportRules(String path) throws Exception {
        Files.write(Paths.get(path), this.toString().getBytes(StandardCharsets.UTF_8));
    }
}
