import java.util.ArrayList;
import java.util.List;

public class Regra {
    List<Item> antecedent;
    Item consequent;

    double confidence;

    Regra(List<Item> antecedentes, Item consequente) {
        this.antecedent = new ArrayList<Item>(antecedentes);
        this.consequent = consequente;
    }

    @Override
    public String toString() {
        String ret = "IF ";
        for (Item item : antecedent) ret = ret + item.label;
        ret = ret + " THEN " + consequent.label;
        return ret;
    }
}