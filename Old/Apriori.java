import java.util.*;

import src.ItemSet;

public class Apriori {

    private String[][] Data;
    private double minSupport;
    private double minConfidence;

    public List<ItemSet> L2;
    public List<ItemSet> frequentItemsets;
    public List<Regra> regras;

    long ruleCountStage1;

    public DataSet2 dataSet;

    double[] minSupps;

    public Apriori(DataSet2 dataset, double minSupport, double minConfidence) {
        this.minSupport = minSupport;
        this.minConfidence = minConfidence;

        this.L2 = new ArrayList<ItemSet>();
        this.frequentItemsets = new ArrayList<ItemSet>();
        this.regras = new ArrayList<>();

        this.dataSet = dataset;

        minSupps = new double[dataset.nClasses];
        for (int i = 0; i < dataset.nClasses; i++)
            minSupps[i] = dataset.getClassFrequency(i) * minSupport;
    }

    public void generateRB() {
        int i;

        for (i = 0; i < this.dataSet.nClasses; i++) {
            double minSup = this.minSupps[i];
            this.generateL2(i, minSup); // Gera itemsets de 2 itens (1 item + classe)
            this.generateLk(this.L2, i, minSup); // Expande para itemsets maiores
        }

    }

    private void calculateSupport(ItemSet itemset, int variavel) {
        itemset.support = 0;
        itemset.class_support = 0;

        for (String[] transaction : this.dataSet.Data) {
            double degree = 1.0;
            for (String item : itemset.itemset) {
                degree *= dataSet.membershipFunctions.get(variavel).getMembership(transaction[variavel], item); // Calcula o grau de pertinência da transação em cada label do itemset
                if (degree == 0.0)
                    break;

                itemset.support += degree;
                if (dataSet.membershipFunctions.get(variavel).getClass(transaction[variavel]) == item) { // Soma em class_support se a classe da transação é a mesma do item
                    itemset.class_support += degree;
                }
            }
        }

        itemset.support /= dataSet.Data.length;
        itemset.class_support /= dataSet.Data.length;

    }

    private void generateL2(int classe, double minSup) {
        int var, uncover;

        this.L2.clear();
        ItemSet itemset = new ItemSet(classe);

        for (var = 0; var < this.dataSet.inputs.length; var++) { // para cada variavel do dataset
            if (this.dataSet.labels.get(var).length > 1) {
                for (String label : this.dataSet.labels.get(var)) { // para cada label da variavel
                    itemset.add(label);
                    this.calculateSupport(itemset, var); // Calcula o supporte fuzzy e o supporte de classe fuzzy da label

                    if (itemset.class_support >= this.minSupport) { // Se o item atende o suporte mínimo, ele entra na L2
                        this.L2.add(itemset.clone());
                    }
                    itemset.remove(0);
                }
            }
        }

        this.generateRules(this.L2, classe);
    }

    private void generateLk(List<ItemSet> Lk, int classe, double minSup) {
		int i, j, size;
		ArrayList<ItemSet> Lnew;
		ItemSet newItemset, itemseti, itemsetj;

		size = Lk.size();

		if (size > 1) {
			if (((Lk.get(0)).size() < this.dataSet.inputs.length) ) { // && ((Lk.get(0)).size() < this.depth)
				Lnew = new ArrayList<ItemSet>();

				for (i = 0; i < size - 1; i++) {
					itemseti = Lk.get(i);
					for (j = i + 1; j < size; j++) {
						itemsetj = Lk.get(j);
						if (this.isCombinable(itemseti, itemsetj)) {
							newItemset = itemseti.clone();
							newItemset.add((itemsetj.get(itemsetj.size() - 1)).clone());
							newItemset.calculateSupports(this.dataBase, this.train, this.typeAggMatch, this.tuneCut);
							if (newItemset.getSupportClass() >= this.minsup)
								Lnew.add(newItemset);
						}
					}

					this.generateRules(Lnew, classe);
					this.generateLk(Lnew, classe, this.minSupport);
					Lnew.clear();
					// System.gc();
				}
			}
		}
    }

    private void generateRules(List<ItemSet> LK, int classe) {
        double confidence, lift;
        int i;
		ItemSet itemset;

        for (i = LK.size() - 1; i >= 0; i--) {
            itemset = LK.get(i);
            System.out.println("Itemset: " + itemset.toString() + " Support: " + itemset.support);
            if (itemset.support > 0.0) {
                confidence = itemset.class_support / itemset.support;
                lift = itemset.class_support / (itemset.support * this.dataSet.getClassFrequency(classe));
            } else {
                confidence = 0.0;
                lift = 0.0;
            }

            if (lift > 1.0) { // if there is a positive relation among the items in the antecedent and the consequent
                // Está errado, corrigir 
                Regra regra = new Regra(itemset.itemset, dataSet.labels.getLast()[itemset.classe]);
                regra.confidence = itemset.class_support / itemset.support;
                this.regras.add(regra);
                ruleCountStage1++;
                LK.remove(i);
            }
        }

        // Falta o reduce 
    }
}
