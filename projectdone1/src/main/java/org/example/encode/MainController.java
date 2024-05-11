package org.example.encode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextArea Output;
    @FXML
    private TextField plaintext;
    @FXML
    private TextField Decodetext;

    static class Wrapper<T> {
        T value;

        Wrapper(T value) {
            this.value = value;
        }
    }

    private static Wrapper<HuffmanTree> huffmanTreeWrapper = new Wrapper<>(null);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String fileName = "tree.txt";
        try {
            Map<Character, Integer> frequencyMap = readFrequencyMapFromFile(fileName);
            HuffmanTree huffmanTree = new HuffmanTree(frequencyMap);
            System.out.println(huffmanTree);
            huffmanTreeWrapper.value = huffmanTree;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Encodebutton(ActionEvent event) {
        String inputMessage = plaintext.getText();
        Map<Character, Integer> frequencyMapEncode = getFrequencyMap(inputMessage);
        if (inputMessage.length() == 0) {
            Output.setText("Enter a plain text string to be encoded");
        } else {
            Output.setText("");
        }
        HuffmanTree huffmanTreeEncode = new HuffmanTree(frequencyMapEncode);
        String encodedMessage = huffmanTreeEncode.encode(inputMessage);

        Decodetext.setText(encodedMessage);

        huffmanTreeWrapper.value = huffmanTreeEncode;
    }

    @FXML
    private void DecodeButton(ActionEvent event) {
        if (huffmanTreeWrapper.value != null) {
            String encodedMessage = Decodetext.getText();
            if (encodedMessage.length() == 0) {
                Output.setText("Please encode a message first.");
            } else {
                String decodedMessage = huffmanTreeWrapper.value.decode(encodedMessage);
                if (isValidBinaryString(encodedMessage)) {
                    plaintext.setText(decodedMessage);
                } else {
                    Output.setText("Decode Error: You have entered invalid characters");
                    plaintext.setText("");
                }
            }
        } else {
            plaintext.setText("Please encode a message first.");
        }
    }

    @FXML
    private void DisplayFrequenciesButton(ActionEvent event) {
        String inputMessage = plaintext.getText();
        final Map<Character, Integer> frequencyMap = getFrequencyMap(inputMessage);
        Output.setText(getFrequencyText(frequencyMap));
    }

    @FXML
    private void DisplayHuffmanCodeButton(ActionEvent event) {
        System.out.println("plaintext: " + plaintext.getText());
        System.out.println("huffmanTreeWrapper.value: " + huffmanTreeWrapper.value);

        if (huffmanTreeWrapper.value != null) {
            String inputMessage = plaintext.getText();
            String encodedMessage = huffmanTreeWrapper.value.encode(inputMessage);
            Output.setText(encodedMessage);
        } else {
            Output.setText("Please encode a message first.");
        }
    }

    @FXML
    private void DisplayHuffmanTreeButton(ActionEvent event) {
        if (huffmanTreeWrapper.value != null) {
            String huffmanTreeString = huffmanTreeWrapper.value.toString();
            Output.setText(huffmanTreeString);
        } else {
            Output.setText("Please enter a message to display the Huffman tree.");
        }
    }

    @FXML
    private void SaveHuffmanTreeButton(ActionEvent event) {
        saveHuffmanTreeToFile();
    }

    @FXML
    private void LoadHuffmanTreeButton(ActionEvent event) {
        loadHuffmanTreeFromFile();
    }

    @FXML
    private void ExitButton(ActionEvent event) {
        System.exit(0);
    }

    public void saveHuffmanTreeToFile() {
        String fileName = "tree.txt";
        if (huffmanTreeWrapper.value != null) {
            try {
                huffmanTreeWrapper.value.save(fileName);
                Output.setText("Huffman tree saved to " + fileName);
            } catch (Exception e) {
                Output.setText("Error: Failed to save Huffman tree to file");
                e.printStackTrace();
            }
        } else {
            Output.setText("Please generate a Huffman tree first.");
        }
    }

    public void loadHuffmanTreeFromFile() {
        String fileName = "tree.txt";
        try {
            Map<Character, Integer> frequencyMap = readFrequencyMapFromFile(fileName);
            HuffmanTree huffmanTree = new HuffmanTree(frequencyMap);
            huffmanTreeWrapper.value = huffmanTree;

            StringBuilder output = new StringBuilder("Huffman tree loaded successfully.\n");
            output.append("Frequency Map:\n");
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                output.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
            Output.setText(output.toString());
        } catch (IOException e) {
            Output.setText("Error loading Huffman tree from file: " + fileName + " (" + e.getMessage() + ")");
            e.printStackTrace();
        }
    }

    private static Map<Character, Integer> getFrequencyMap(String message) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : message.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    private static String getFrequencyText(Map<Character, Integer> frequencyMap) {
        StringBuilder frequencyText = new StringBuilder("Character Frequency:\n");
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            frequencyText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return frequencyText.toString();
    }

    public static boolean isValidBinaryString(String str) {
        for (char c : str.toCharArray()) {
            if (c != '0' && c != '1') {
                return false;
            }
        }
        return true;
    }

    public static Map<Character, Integer> readFrequencyMapFromFile(String fileName) throws IOException {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    char character = parts[1].equals("none") ? '\0' : parts[1].charAt(0);
                    if (Character.isLetter(character)) {
                        int frequency = Integer.parseInt(parts[2]);
                        frequencyMap.put(character, frequency);
                    }
                }
            }
        }
        return frequencyMap;
    }
}
