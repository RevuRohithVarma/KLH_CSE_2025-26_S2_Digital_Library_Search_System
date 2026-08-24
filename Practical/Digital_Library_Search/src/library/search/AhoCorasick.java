package library.search;

import java.util.*;

/**
 * Aho-Corasick Multiple Pattern Matching Algorithm.
 *
 * Purpose:
 * Searches for multiple keywords/patterns simultaneously
 * in a single text.
 *
 * Main Steps:
 * 1. Build the Trie.
 * 2. Number the Trie nodes implicitly using node objects.
 * 3. Build failure links using BFS.
 * 4. Build output information.
 * 5. Search the text.
 *
 * Example:
 *
 * Patterns:
 * algorithm
 * data
 * structure
 *
 * The algorithm can search for all three patterns
 * during a single traversal of the text.
 *
 * Time Complexity:
 * Building Trie: O(total pattern length)
 * Failure links: O(total pattern length)
 * Searching: O(text length + number of matches)
 *
 * Space Complexity:
 * O(total pattern length)
 */
public class AhoCorasick {

    // ==========================================
    // Trie Node
    // ==========================================

    /**
     * Represents one node in the Aho-Corasick Trie.
     */
    private static class Node {

        // Stores child nodes for characters.
        Map<Character, Node> children = new HashMap<>();

        // Failure link points to the longest valid suffix state.
        Node failure;

        // Stores patterns ending at this node.
        List<String> outputs = new ArrayList<>();
    }

    // ==========================================
    // Root Node
    // ==========================================

    private final Node root;

    /**
     * Creates an empty Aho-Corasick automaton.
     */
    public AhoCorasick() {

        root = new Node();
        root.failure = root;
    }

    // ==========================================
    // Step 1: Build Trie
    // ==========================================

    /**
     * Adds one pattern to the Trie.
     *
     * @param pattern pattern to insert
     */
    public void addPattern(String pattern) {

        if (pattern == null || pattern.isEmpty()) {
            return;
        }

        String normalizedPattern = pattern.toLowerCase();

        Node current = root;

        for (char ch : normalizedPattern.toCharArray()) {

            current.children.putIfAbsent(ch, new Node());

            current = current.children.get(ch);
        }

        // ==========================================
        // Step 4: Output Information
        // ==========================================

        if (!current.outputs.contains(normalizedPattern)) {
            current.outputs.add(normalizedPattern);
        }
    }

    // ==========================================
    // Step 2 & 3: Build Failure Links Using BFS
    // ==========================================

    /**
     * Builds failure links for all Trie nodes.
     *
     * BFS is used so that failure links of parent levels
     * are available before processing deeper levels.
     */
    public void buildFailureLinks() {

        Queue<Node> queue = new LinkedList<>();

        // ==========================================
        // Initialize Root Children
        // ==========================================

        for (Node child : root.children.values()) {

            child.failure = root;
            queue.offer(child);
        }

        // ==========================================
        // BFS Traversal
        // ==========================================

        while (!queue.isEmpty()) {

            Node current = queue.poll();

            for (Map.Entry<Character, Node> entry
                    : current.children.entrySet()) {

                char ch = entry.getKey();
                Node child = entry.getValue();

                queue.offer(child);

                Node failureNode = current.failure;

                // Follow failure links until a matching
                // transition is found or root is reached.
                while (failureNode != root
                        && !failureNode.children.containsKey(ch)) {

                    failureNode = failureNode.failure;
                }

                if (failureNode.children.containsKey(ch)
                        && failureNode.children.get(ch) != child) {

                    child.failure =
                            failureNode.children.get(ch);

                } else {

                    child.failure = root;
                }

                // ==========================================
                // Merge Failure Outputs
                // ==========================================

                for (String output
                        : child.failure.outputs) {

                    if (!child.outputs.contains(output)) {
                        child.outputs.add(output);
                    }
                }
            }
        }
    }

    // ==========================================
    // Search Result Class
    // ==========================================

    /**
     * Represents one pattern match found in the text.
     */
    public static class Match {

        private final String pattern;
        private final int position;

        public Match(String pattern, int position) {

            this.pattern = pattern;
            this.position = position;
        }

        public String getPattern() {
            return pattern;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public String toString() {

            return "Pattern: " + pattern
                    + ", Position: " + position;
        }
    }

    // ==========================================
    // Step 5: Search Text
    // ==========================================

    /**
     * Searches the given text for all added patterns.
     *
     * @param text text to search
     * @return list of all matches
     */
    public List<Match> search(String text) {

        List<Match> matches = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return matches;
        }

        String normalizedText = text.toLowerCase();

        Node current = root;

        // ==========================================
        // Traverse Text
        // ==========================================

        for (int i = 0; i < normalizedText.length(); i++) {

            char ch = normalizedText.charAt(i);

            // Follow failure links if no transition exists.
            while (current != root
                    && !current.children.containsKey(ch)) {

                current = current.failure;
            }

            // Move to matching child if available.
            if (current.children.containsKey(ch)) {

                current = current.children.get(ch);

            } else {

                current = root;
            }

            // ==========================================
            // Check Output Patterns
            // ==========================================

            for (String pattern : current.outputs) {

                int startPosition =
                        i - pattern.length() + 1;

                matches.add(
                        new Match(pattern, startPosition)
                );
            }
        }

        return matches;
    }
}