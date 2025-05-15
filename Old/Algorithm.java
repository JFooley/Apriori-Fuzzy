import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import src.Apriori;
import src.ItemSet;

public class Algorithm {

    public static void run(String config_name, String dataset_tra, String dataset_tst, int labels_size, double min_sup, double min_confidence) {       
        long startTime, totalTime, startTimeApriori, totalTimeApriori, totalTimeConstruction, startTimeEvTest, totalTimeEvTest;

        startTime = System.currentTimeMillis();
        
        // Debug debug = new Debug();
        // debug.run_tests();

        // Lê o dataset e transforma em um dataset com as classe fuzzy
            // Definir automaticamente as regiões fuzzy e seus valores de pontas e pico
            // Calcular o grau de pertinencia do valor do atributo para cada região da função de pertinencia e selenciona a com maior grau
            // Transformar a tabela com os valores originais em uma em que cada valor é substituido pela região selecionada

        System.out.println("\n\nRunning FARC-NEW on " + config_name);
        System.out.println("Train: " + dataset_tra + " Teste: " + dataset_tst);
        System.out.println("Label size: " + labels_size + " Min_sup: " + min_sup + " Min_confidence: " + min_confidence);
        
        DataSet2 dataset = new DataSet2(dataset_tra, labels_size);

        System.out.println("Name: " + dataset.label);
        System.out.println("Arquivo: " + dataset.archive);
        System.out.println("Tamanho do dataset: " + dataset.lin + " x " + dataset.col);

        System.out.println("\nMatriz de dados original (10 primeiras linhas)");
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < dataset.col; j++){
                System.out.print(dataset.Data[i][j] + " ");
            }
            System.out.print("\n");
        }

        System.out.println("\nMatriz de dados fuzzyficada (10 primeiras linhas)");
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < dataset.col; j++){
                System.out.print(dataset.outputData[i][j] + " ");
            }
            System.out.print("\n");
        }
        
        // Executa o apriori para gerar as regras de associação
        startTimeApriori = System.currentTimeMillis();

        Apriori apriori = new Apriori(dataset, min_confidence, min_sup);
        apriori.generateRB();
        
        List<ItemSet> frequentItemsets = apriori.frequentItemsets;
        List<Regra> ruleBase = apriori.regras;

        System.out.println("Itens frequentes: \n");
        for (ItemSet itemset : frequentItemsets) {
            System.out.println(itemset.itemset);
        }

        System.out.println("Regras geradas: \n");
        for (Regra regra : ruleBase) {
            System.out.println(regra.toString());
        }

        totalTimeApriori = System.currentTimeMillis();

        // Utiliza o algorítimo genético para pegar a melhor a base de regras gerada

        // Faz o processo de inferencia usando a base de regras

        totalTime = System.currentTimeMillis();
    }    
}
