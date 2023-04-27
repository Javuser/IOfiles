import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //file1, file2
        String file1 = "file1.txt";
        String file2 = "file2.txt";

        Set<String> dictionary = createDictionary(file1, file2);
        int dictSize = dictionary.size();
        //векторы
        int[] vectorA = createVector(file1, dictionary, dictSize);
        int[] vectorB = createVector(file2, dictionary, dictSize);


        double cosineSimilarity = calculateCosineSimilarity(vectorA, vectorB);

        System.out.printf("Similarity = %.2f\n", cosineSimilarity);


        try (PrintWriter writer = new PrintWriter("dictionary.txt")) {
            for (String word : dictionary) {
                writer.println(word);
            }
        }
    }

    private static Set<String> createDictionary(String file1, String file2) throws IOException {
        Set<String> dictionary = new HashSet<>();

        try (BufferedReader reader1 = new BufferedReader(new FileReader("file1.txt"));
             BufferedReader reader2 = new BufferedReader(new FileReader("file2.txt"))) {

            String line;
            while ((line = reader1.readLine()) != null) {
                String[] words = line.split("\\W+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        dictionary.add(word);
                    }
                }
            }

            while ((line = reader2.readLine()) != null) {
                String[] words = line.split("\\W+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        dictionary.add(word);
                    }
                }
            }
        }

        return dictionary;
    }

    private static int[] createVector(String file, Set<String> dictionary, int dictSize) throws IOException {
        int[] vector = new int[dictSize];

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\W+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        int index = getIndex(dictionary, word);
                        vector[index]++;
                    }
                }
            }
        }

        return vector;
    }

    private static int getIndex(Set<String> dictionary, String word) {
        int index = 0;
        for (String dictWord : dictionary) {
            if (dictWord.equals(word)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static double calculateCosineSimilarity(int[] vectorA, int[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++){
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}