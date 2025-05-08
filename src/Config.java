package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Config {
    public String file_path = "";
    public String training_dataset_path = "";
    public String testing_dataset_path = "";

    public Integer default_label_size = 3;
    public Integer default_label_shape = 0; // 0 = triangular, 1 = trapezoidal
    public Map<String, List<Integer>> attributes = new HashMap<>(); // atributo => {label size, shape}

    public double min_support;
    public double min_confidence;

    public Config() {}

    public Config(String file) {
        this.file_path = file;
        this.readConf(this.file_path);
    }

    public void readConf(String confPath) {
        try (Scanner scanner = new Scanner(new File(confPath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Ignore empty and comments
                if (line.isEmpty() || line.startsWith("%") || line.startsWith("#") || line.startsWith("//")) {
                    continue;
                }

                // Training path
                if (line.startsWith("Training")) {
                    String[] str_line = line.split("\\s*=\\s*");
                    this.training_dataset_path = str_line[1].replace("\"", "");
                }

                // Testing path
                if (line.startsWith("Testing")) {
                    String[] str_line = line.split("\\s*=\\s*");
                    this.testing_dataset_path = str_line[1].replace("\"", "");
                }

                // Minimum support and confidence
                if (line.startsWith("Minimum")) {
                    String[] str_line = line.split("\\s*=\\s*");

                    if (str_line[0].equals("Minimum Support")) {
                        this.min_support = Double.parseDouble(str_line[1]);

                    } else if (str_line[0].equals("Minimum Confidence")) {
                        this.min_confidence = Double.parseDouble(str_line[1]);
                    }
                }

                // Default size and shape
                if (line.startsWith("Default Attribute")) {
                    String[] str_line = line.split("\\s*=\\s*")[1].split(" ");

                    this.default_label_size = Integer.valueOf(str_line[0]);

                    switch (str_line[1]) {
                        case "Triangular" -> this.default_label_shape = 0;
                            
                        case "Trapezoidal" -> this.default_label_shape = 1;
                            
                        default -> this.default_label_shape = 0;
                    }
                }

                // Especific variable infos
                if (line.startsWith("Attribute")) {
                    String[] str_line = line.split("\\s*=\\s*")[1].split(" ");

                    String name = str_line[0];
                    int size = Integer.parseInt(str_line[1]);
                    String shape = str_line[2];

                    switch (shape) {
                        case "Triangular" -> this.attributes.put(name, new ArrayList<>(List.of(size, 0)));

                        case "Trapezoidal" -> this.attributes.put(name, new ArrayList<>(List.of(size, 1)));

                        default -> this.attributes.put(name, new ArrayList<>(List.of(size, 0)));
                    }
                }
            }
        } catch (FileNotFoundException e) { 
            System.err.println("Arquivo não encontrado: " + this.file_path);

        } catch (java.lang.NullPointerException e) { 
            System.err.println("Arquivo não encontrado: " + this.file_path);
        } 
    }
}
