import java.util.ArrayList;

public class ItemSet {
    ArrayList<String> itemset;
    int classe;
    double support, class_support;

    public ItemSet(int classe) {
        this.classe = classe;
        this.itemset = new ArrayList<String>();
        this.support = 0;
        this.class_support = 0;
    }

    public void add(String item) {
        this.itemset.add(item);
    }

    public String remove(int pos) {
        return (this.itemset.remove(pos));
    }

    public ItemSet clone() {
        ItemSet d = new ItemSet(this.classe);
        for (int i = 0; i < this.itemset.size(); i++)
          d.add(new String(itemset.get(i)));
    
        d.classe = this.classe;
        d.support = this.support;
        d.class_support = this.class_support;
    
        return (d);
    }

    public String get(int pos) {
        return (this.itemset.get(pos));
    }

    public int size() {
        return this.itemset.size();
    }

}