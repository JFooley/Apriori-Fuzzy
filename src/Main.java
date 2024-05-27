import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception { 
        // Defina o caminho para a pasta raiz de configs
        Path configsDir = Paths.get("../configs");

        try {
            // Itera sobre cada subdiretório dentro da pasta de configs
            Files.walk(configsDir)
                    .filter(Files::isDirectory) // Filtra apenas os diretórios
                    .forEach(datasetDir -> {
                        try (DirectoryStream<Path> stream = Files.newDirectoryStream(datasetDir, "config*.txt")) {
                            // Itera sobre cada arquivo "config(numero).txt" dentro do diretório do dataset
                            for (Path configFile : stream) {
                                readConfigFile(configFile);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void readConfigFile(Path configFile) throws IOException {
        // Lê o nome do arquivo de configuração
        String config_name = configFile.getFileName().toString().replaceFirst("[.][^.]+$", "");

        // Lê o conteúdo do arquivo de configuração
        String content = Files.readString(configFile);

        // Define os padrões para encontrar as informações
        Pattern trainingPattern = Pattern.compile("Training\\s*=\\s*\"([^\"]+)\"");
        Pattern testingPattern = Pattern.compile("Testing\\s*=\\s*\"([^\"]+)\"");
        Pattern numberOfLabelsPattern = Pattern.compile("Number of Labels\\s*=\\s*(\\d+)");
        Pattern minSupportPattern = Pattern.compile("Minimum Support\\s*=\\s*(\\d+\\.\\d+)");
        Pattern minConfidencePattern = Pattern.compile("Minimum Confidence\\s*=\\s*(\\d+\\.\\d+)");

        // Procura pelas informações no conteúdo do arquivo
        Matcher trainingMatcher = trainingPattern.matcher(content);
        Matcher testingMatcher = testingPattern.matcher(content);
        Matcher numberOfLabelsMatcher = numberOfLabelsPattern.matcher(content);
        Matcher minSupportMatcher = minSupportPattern.matcher(content);
        Matcher minConfidenceMatcher = minConfidencePattern.matcher(content);

        // Extrai as informações, se encontradas
        String trainingPath = trainingMatcher.find() ? trainingMatcher.group(1) : "";
        String testingPath = testingMatcher.find() ? testingMatcher.group(1) : "";
        int numberOfLabels = numberOfLabelsMatcher.find() ? Integer.parseInt(numberOfLabelsMatcher.group(1)) : 0;
        double minSupport = minSupportMatcher.find() ? Double.parseDouble(minSupportMatcher.group(1)) : 0.0;
        double minConfidence = minConfidenceMatcher.find() ? Double.parseDouble(minConfidenceMatcher.group(1)) : 0.0;

        // Chama o algorítimo nesses arquivos
        Algorithm.run(config_name, trainingPath, testingPath, numberOfLabels, minSupport, minConfidence);
    }
}
