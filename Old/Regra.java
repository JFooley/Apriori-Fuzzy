import java.util.ArrayList;
import java.util.List;

public class Regra {
    List<Item1> antecedent;
    Item1 consequent;

    double confidence;

    Regra(List<Item1> antecedentes, Item1 consequente) {
        this.antecedent = new ArrayList<Item1>(antecedentes);
        this.consequent = consequente;
    }

    @Override
    public String toString() {
        String ret = "IF ";
        for (Item1 item : antecedent) ret = ret + item.label;
        ret = ret + " THEN " + consequent.label;
        return ret;
    }
}