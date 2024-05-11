package org.example.encode;

class Node implements java.io.Serializable {
    char character;
    Node left, right;

    Node(char character, int frequency) {
        this.character = character;
        left = right = null;
    }

    Node(int label, String s, int frequency) {
        if (s.equals("none")) {
            character = '\0';
        } else if (s.equals("space")) {
            character = ' ';
        } else {
            character = s.charAt(0);
        }
        left = right = null;
    }

    public String getCharacterAsString() {
        if (character == '\0') {
            return "none";
        } else if (character == ' ') {
            return "space";
        } else {
            return Character.toString(character);
        }
    }
}
