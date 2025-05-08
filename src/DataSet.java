package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSet {
    public Config config = new Config();
    public boolean is_training = false;

    public Map<Integer, Integer> attributeLabelCounts = new HashMap<>(); // Quantidade de labels por atributo
    public Map<Integer, List<String>> attributeLabels = new HashMap<>(); // Nomes das labels por atributo
    public Map<Integer, DoublePair> attributeLimits = new HashMap<>(); // Limites numérico das labels

    public List<String[]> attributeNames = new ArrayList<>(); // Nomes dos atributos (nome, tipo)

    public List<String[]> rawData = new ArrayList<>(); // Transações
    public List<String[]> data = new ArrayList<>(); // Transações tratadas

    public List<String> inputAttributes = new ArrayList<>(); // Lista de atributos de entrada
    public List<String> outputAttributes = new ArrayList<>(); // Lista de atributos de saída

    // Definições de configuração
    public String filePath;
    public String configPath;

    public DataSet(String config_path, boolean is_training) {
        this.config = new Config(config_path);
        this.is_training = is_training;

        if (this.is_training) this.filePath = this.config.training_dataset_path;
        else this.filePath = this.config.testing_dataset_path;

        this.configPath = this.config.file_path;

        readARFF(this.filePath);
    }

    public DataSet(Config conf, boolean is_training) {
        this.config = conf;
        this.is_training = is_training;

        if (this.is_training) this.filePath = this.config.training_dataset_path;
        else this.filePath = this.config.testing_dataset_path;
        
        this.configPath = this.config.file_path;

        readARFF(this.filePath);
    }

    // Método para ler o arquivo ARFF
    private void readARFF(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            boolean isDataSection = false;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Ignorar linhas vazias ou comentários
                if (line.isEmpty() || line.startsWith("%")) {
                    continue;
                }

                // Seção de dados
                if (line.toLowerCase().startsWith("@data")) {
                    isDataSection = true;
                    continue;
                }

                // Seção de atributos
                if (!isDataSection && line.toLowerCase().startsWith("@attribute")) {
                    processAttributeLine(line);
                }

                // Identificar atributos de entrada
                if (line.toLowerCase().startsWith("@input")) {
                    String[] inputs = line.substring(line.indexOf(" ") + 1).split(",\\s*");
                    inputAttributes.addAll(Arrays.asList(inputs));
                    continue;
                }

                // Identificar atributos de saída
                if (line.toLowerCase().startsWith("@output")) {
                    String[] outputs = line.substring(line.indexOf(" ") + 1).split(",\\s*");
                    outputAttributes.addAll(Arrays.asList(outputs));
                    continue;
                }

                // Ler dados
                if (isDataSection && !line.isEmpty()) {
                    rawData.add(line.split(",\\s*"));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + filePath);
            e.printStackTrace();
        }
    
        // Salva o raw data
        data = new ArrayList<>();
        for (String[] array : rawData) {
            data.add(array.clone()); // Adiciona o array clonado à nova lista
        }

        // Tratar os dados
        for (Integer i = 0; i < attributeNames.size(); i++) {
            if (attributeNames.get(i)[1] == "Nominal") { // verifica se o atributo é nominal
                for (String[] line : data) {
                    // Busca a lista de labels do atributo e substitui a linha pelo indice da label daquela linha ex: {M, F, I} o M na transação se torna 0
                    line[i] = Integer.toString(attributeLabels.get(i).indexOf(line[i]));
                }
            }
        }

    }

    // Método para processar uma linha de atributo
    private void processAttributeLine(String line) {
        String[] parts = line.split("\\s+", 3); // Dividir a linha em partes
        if (parts.length < 3) {
            return; // Ignorar linhas mal formatadas
        }

        String attributeName = parts[1]; // Nome do atributo
        String attributeType = parts[2]; // Tipo do atributo / valores limite

        // Processar atributos nominais
        if (attributeType.startsWith("{")) {
            attributeNames.add(new String[] {attributeName, "Nominal"});
            processNominalAttribute(attributeNames.size() - 1, attributeType);
            
        } else {
            attributeNames.add(new String[] {attributeName, "Numeric"});
            processNumericAttribute(attributeNames.size() - 1, attributeType);
        }
    }

    // Método para processar atributos nominais
    private void processNominalAttribute(Integer index, String attributeType) {
        // Extrair valores nominais
        String[] nominalValues = attributeType.replace("{", "").replace("}", "").split(",");

        // Armazenar quantidade de labels
        attributeLabelCounts.put(index, nominalValues.length);

        // Armazenar os limites
        attributeLimits.put(index, new DoublePair(0d, nominalValues.length-1));

        // Armazenar nomes das labels
        List<String> labels = new ArrayList<>();
        for (String value : nominalValues) {
            labels.add(value.trim());
        }
        attributeLabels.put(index, labels);
    }

    // Método para processar atributos não nominais
    private void processNumericAttribute(Integer index, String attributeType) {
        Pattern pattern = Pattern.compile("\\[([\\d\\.E\\-]+),\\s*([\\d\\.E\\-]+)\\]");
        Matcher matcher = pattern.matcher(attributeType);
        if (matcher.find()) {
            double lowerLimit = Double.parseDouble(matcher.group(1));
            double upperLimit = Double.parseDouble(matcher.group(2));

            // Armazenar os limites
            attributeLimits.put(index, new DoublePair(lowerLimit, upperLimit));
        }

        // Definir quantidade padrão de labels
        attributeLabelCounts.put(index, this.config.default_label_size);

        // Criar labels no formato L_0, L_1, ..., L_(default_atribute_size-1)
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < this.config.default_label_size; i++) {
            labels.add("L_" + (i + 1) + "/" + this.config.default_label_size);
        }
        attributeLabels.put(index, labels);
    }

    // Métodos para acessar as informações
    public List<String[]> getAttributeNames() {
        return attributeNames;
    }

    public Map<Integer, Integer> getAttributeLabelCounts() {
        return attributeLabelCounts;
    }

    public Map<Integer, DoublePair> getAttributeLimits() {
        return attributeLimits;
    }

    public Map<Integer, List<String>> getAttributeLabels() {
        return attributeLabels;
    }

    public List<String> getInputAttributes() {
        return inputAttributes;
    }

    public List<String> getOutputAttributes() {
        return outputAttributes;
    }

    public List<String[]> getData() {
        return data;
    }

    public List<String[]> getRawData() {
        return rawData;
    }

}