package src;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {

        // Este código é apenas para testar a leitura do dataset e a geração das regras
        // O uso do algoritmo Apriori Fuzzy deve ser feito utilizando a classe Apriori e a classe DataSet
        // A classe main apenas está exemplificando o uso com um dataset de exemplo
    
        DataSet reader = new DataSet("src/exemplo/config.txt", true);

        System.out.println("Configuração");
        System.out.println("Path: " + reader.config.file_path + " Train Path: " + reader.config.training_dataset_path + " Test Path: " + reader.config.testing_dataset_path);
        System.out.println("Min Support: " + reader.config.min_support + " Min Confidence: " + reader.config.min_confidence);
        System.out.println("\nLabels configs: ");
        System.out.println("Default - Size: " + reader.config.default_label_size + " Shape: " + reader.config.default_label_shape);
        for (Map.Entry<String, Item> entry: reader.config.attributes.entrySet()) {
            System.out.println(entry.getKey() + " - Size: " + entry.getValue().variable() + " Shape: " + entry.getValue().label());
        }

        System.out.println("\nNomes dos atributos: ");
        for (String[] name : reader.getAttributeNames()) {
            System.out.print(name[0] + ", ");
        }
        System.out.print("\n");
        
        System.out.println("Quantidade de labels por atributo: " + reader.getAttributeLabelCounts());

        System.out.println("\nLimites dos atributo: ");
        for (Map.Entry<Integer, DoublePair> entrada : reader.getAttributeLimits().entrySet()) {
            System.out.println(entrada.getKey() + " - " + entrada.getValue().x() + "/" + entrada.getValue().y());
        }
        System.out.println("\nLabels dos atributos: ");
        for (Map.Entry<Integer, List<String>> entrada : reader.getAttributeLabels().entrySet()) {
            System.out.println(entrada.getKey() + " - " + entrada.getValue().toString());
        }
        System.out.println("\nInputs: " + reader.getInputAttributes());
        System.out.println("Outputs: " + reader.getOutputAttributes());

        System.out.println("\nDados RAW: ");
        for (Integer j = 0; j < 10; j++) {
            String[] line = reader.getRawData().get(j);
            for (int i = 0; i < reader.attributeNames.size(); i++) {
                System.out.print(line[i]+", ");
            }
            System.out.print("\n");
        }

        System.out.println("\nDados tratados: ");
        for (Integer j = 0; j < 10; j++) {
            String[] line = reader.getData().get(j);
            for (int i = 0; i < reader.attributeNames.size(); i++) {
                System.out.print(line[i]+", ");
            }
            System.out.print("\n");
        }
        
        var ms = reader.msfunctions.get(1);
        System.out.println("\nVariable: " + ms.atribute_name + " - Index: " + ms.atribute_index);
        for (Region label : ms.regions) {
            System.out.println("");
            System.out.println("Label index: " + label.label + " - Label name: " + reader.attributeLabels.get(ms.atribute_index).get(label.label) + " - Shape: " + label.shape);
            System.out.println("x0: " + label.x0 + ", x1: " + label.x1 + ", x2: " + label.x2 + ", x3: " + label.x3);
        }

        Apriori apriori = new Apriori(reader);
        apriori.generateRuleBase();
        
        System.out.println("Apriori Fuzzy");
        System.out.println("Rules: ");
        apriori.rule_base.printRules();
    
    }
}