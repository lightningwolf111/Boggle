import java.util.HashSet;
import java.util.HashMap;

import edu.princeton.cs.algs4.In;

public class BoggleSolver {
    // TODO fix Q. Change during pre-processing and retrieval of words
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private Node root = new Node();
    private String[] dictionary;
    public BoggleSolver(String[] dictionary) {
        this.dictionary = dictionary.clone();
        for (String s : this.dictionary) {
            add(s);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        HashSet<String> words = new HashSet<>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                Node current = root; // get(root, 0, String.valueOf(board.getLetter(row, col)), board.getLetter(row, col) == 'Q');
                boolean[][] visited = new boolean[board.rows()][board.cols()];
                searchInBoard(current, row, col, visited, words , board);
            }
        }

        return words;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if (!inDictionary(word) || word.length() < 3) {
            return 0;
        }
        if (word.length() == 3 || word.length() == 4) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        } else {
            return 11;
        }
    }

    private void searchInBoard(Node n, int row, int col, boolean[][] visited, HashSet<String> words , final BoggleBoard board) {
        visited[row][col] = true;
        if (n.isWord && n.sequence.length() > 2) {
            words.add(n.sequence.replace("Q", "QU"));
        }
        if (col != board.cols() - 1 && !visited[row][col + 1]) {
            if (n.links.containsKey(board.getLetter(row, col + 1))) {
                searchInBoard(n.links.get(board.getLetter(row, col + 1)), row, col + 1, visited, words, board);
            }
        }
        if (col != board.cols() - 1 && row != board.rows() - 1 && !visited[row + 1][col + 1]) {
            if (n.links.containsKey(board.getLetter(row + 1, col + 1))) {
                searchInBoard(n.links.get(board.getLetter(row + 1, col + 1)), row + 1, col + 1, visited, words, board);
            }
        }
        if (col !=  board.cols() - 1 && row != 0 && !visited[row - 1][col + 1]) {
            if (n.links.containsKey(board.getLetter(row - 1, col + 1))) {
                searchInBoard(n.links.get(board.getLetter(row - 1, col + 1)), row - 1, col + 1, visited, words, board);
            }
        }
        if (col != 0 && !visited[row][col - 1]) {
            if (n.links.containsKey(board.getLetter(row, col - 1))) {
                searchInBoard(n.links.get(board.getLetter(row, col - 1)), row, col - 1, visited, words, board);
            }
        }
        if (col != 0 && row != board.rows() - 1 && !visited[row + 1][col - 1]) {
            if (n.links.containsKey(board.getLetter(row + 1, col - 1))) {
                searchInBoard(n.links.get(board.getLetter(row + 1, col - 1)), row + 1, col - 1, visited, words, board);
            }
        }
        if (col != 0 && row != 0 && !visited[row - 1][col - 1]) {
            if (n.links.containsKey(board.getLetter(row - 1, col - 1))) {
                searchInBoard(n.links.get(board.getLetter(row - 1, col - 1)), row - 1, col - 1, visited, words, board);
            }
        }
        if (row != 0 && !visited[row - 1][col]) {
            if (n.links.containsKey(board.getLetter(row - 1, col))) {
                searchInBoard(n.links.get(board.getLetter(row - 1, col)), row - 1, col, visited, words, board);
            }
        }
        if (row != board.rows() - 1 && !visited[row + 1][col]) {
            if (n.links.containsKey(board.getLetter(row + 1, col))) {
                searchInBoard(n.links.get(board.getLetter(row + 1, col)), row + 1, col, visited, words, board);
            }
        }
        visited[row][col] = false;
        //return new State(n, row, col, visited, words, board);
    }

//    private class State {
//        State(Node n, int r, int c, boolean[][] visited, HashSet<String> words, BoggleBoard board) {
//            node = n;
//            row = r;
//            col = c;
//            this.visited = visited;
//            this.words = words;
//            this.board = board;
//        }
//        Node node;
//        int row;
//        int col;
//        boolean[][] visited;
//        HashSet<String> words;
//        BoggleBoard board;
//    }


    private class Node {
        String sequence;
        boolean isWord = false;
        HashMap<Character, Node> links = new HashMap<>();
    }
    private void add(String key) {
        root = add(root, 0, key.replace("QU","Q"), false);
    }
    private Node add(Node node, int indexInKey, String key, boolean previousQ) {
        if (indexInKey == key.length()) {
            node.isWord = true;
            node.sequence = key;
            return node;
        }
        //if (!previousQ && key.charAt(indexInKey) != 'Q') {
            if (!node.links.containsKey(key.charAt(indexInKey))) {
                Node n = new Node();
                node.links.put(key.charAt(indexInKey), n);
            }
            add(node.links.get(key.charAt(indexInKey)), indexInKey + 1, key, false);
            return node;
//        } else if (previousQ) {
//            if (key.charAt(indexInKey) != 'U') {
//                return node;
//            } else {
//                if (!node.links.containsKey(key.charAt(indexInKey + 1))) {
//                    Node n = new Node();
//                    node.links.put(key.charAt(indexInKey + 1), n);
//                }
//                add(node.links.get(key.charAt(indexInKey + 1)), indexInKey + 2, key, false);
//                return node;
//            }
//        } else {
//            // current is Q
//            if (!node.links.containsKey('Q')) {
//                Node n = new Node();
//                node.links.put(key.charAt(indexInKey), n);
//            }
//            add(node.links.get(key.charAt(indexInKey)), indexInKey + 1, key, true);
//            return node;
//        }
    }

    private boolean inDictionary(final String string) {
        Node end = get(root, 0, string.replace("QU","Q"), false);
        if (end == null) return false;
        if (!end.isWord) return false;
        return true;
    }

    private Node get(Node n, int positionInKey, String key, boolean previousQ) {
        if (positionInKey == key.length()) {
            return n;
        }
//        if (previousQ && positionInKey < key.length() - 1) {
//            positionInKey = positionInKey + 1;
//        }
        if (!n.links.containsKey(key.charAt(positionInKey))) {
            return null;
        }
        return get(n.links.get(key.charAt(positionInKey)), positionInKey + 1, key, key.charAt(positionInKey) == 'Q');
    }

    public static void main(String[] args) {
        In in = new In("/Users/jan/Downloads/boggle/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("/Users/jan/Downloads/boggle/board-points777.txt");
        int totalPoints = 0;
        for (String s : solver.getAllValidWords(board)) {
            System.out.println(s);
            totalPoints += solver.scoreOf(s);
        }
        System.out.println(totalPoints);
        System.out.println(solver.scoreOf("QUOTERS"));
        System.out.println(solver.inDictionary("QUOTERS"));
        System.out.println(solver.scoreOf("QOTERS"));
//        System.out.println(solver.inDictionary("AMONGST"));
//        System.out.println(solver.scoreOf("AMONGST"));
//        System.out.println(solver.scoreOf("AMON"));
    }
}
