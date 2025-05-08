package src;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        DataSet reader = new DataSet("D:/GABRIEL/UEFS/IC/Arquivos/Apriori Fuzzy/src/config.txt", true);

        System.out.println("Configuração");
        System.out.println("Path: " + reader.config.file_path + " Train Path: " + reader.config.training_dataset_path + " Test Path: " + reader.config.testing_dataset_path);
        System.out.println("Min Support: " + reader.config.min_support + " Min Confidence: " + reader.config.min_confidence);
        System.out.println("Default Label - Size: " + reader.config.default_label_size + " Shape: " + reader.config.default_label_shape);
        for (Map.Entry<String, List<Integer>> entry: reader.config.attributes.entrySet()) {
            System.out.println(entry.getKey() + " - Size: " + entry.getValue().get(0).toString() + " Shape: " + entry.getValue().get(1).toString());
        }

        System.out.println("Nomes dos atributos: ");
        for (String[] name : reader.getAttributeNames()) {
            System.out.print(name[0] + ", ");
        }
        System.out.print("\n");
        
        System.out.println("Quantidade de labels por atributo: " + reader.getAttributeLabelCounts());

        System.out.println("Limites das labels por atributo: ");
        for (Map.Entry<Integer, DoublePair> entrada : reader.getAttributeLimits().entrySet()) {
            System.out.println(entrada.getKey() + " - " + entrada.getValue().x() + "/" + entrada.getValue().y());
        }
        System.out.println("Labels dos atributos: ");
        for (Map.Entry<Integer, List<String>> entrada : reader.getAttributeLabels().entrySet()) {
            System.out.println(entrada.getKey() + " - " + entrada.getValue().toString());
        }
        System.out.println("Inputs: " + reader.getInputAttributes());
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

        MembershipFunction ms = new MembershipFunction(reader, 4, Region.TRIANGULAR);
        System.out.println("\nVariable: " + ms.atribute + " Name: " + reader.attributeNames.get(ms.atribute)[1] + " ");
        for (Region label : ms.regions) {
            System.out.println("");
            System.out.println("Label index: " + label.label + " Name: " + reader.attributeLabels.get(ms.atribute).get(label.label));
            System.out.println("x0: " + label.x0 + " x1: " + label.x2 + " x2: " + label.x2 + " x3: " + label.x3);
        }

    }
}