package library.search;

import java.util.ArrayList;
import java.util.List;

/**
 * KMP (Knuth-Morris-Pratt) Pattern Matching Algorithm.
 *
 * Purpose:
 * Searches for a pattern inside a text without repeatedly
 * comparing characters that have already been matched.
 *
 * Why KMP is used:
 * - Efficient for single-pattern searches.
 * - Avoids unnecessary comparisons.
 * - Suitable for keyword/pattern searching in the library corpus.
 *
 * Time Complexity:
 * - LPS construction: O(m)
 * - Pattern searching: O(n)
 * - Overall: O(n + m)
 *
 * Space Complexity:
 * - O(m)
 *
 * Where:
 * n = length of text
 * m = length of pattern
 */
public class KMPSearch {

    // ==========================================
    // Build LPS (Longest Prefix Suffix) Array
    // ==========================================

    /**
     * Builds the LPS array for the given pattern.
     *
     * LPS[i] represents the length of the longest proper
     * prefix which is also a suffix for pattern[0...i].
     *
     * @param pattern search pattern
     * @return LPS array
     */
    private static int[] buildLPS(String pattern) {

        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {

                    length = lps[length - 1];

                } else {

                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // ==========================================
    // KMP Search
    // ==========================================

    /**
     * Searches for all occurrences of a pattern inside a text.
     *
     * The search is case-insensitive so that:
     *
     * "Harry"
     *
     * and
     *
     * "harry"
     *
     * produce the same result.
     *
     * @param text text in which the pattern is searched
     * @param pattern search pattern
     * @return list of starting indexes where the pattern occurs
     */
    public static List<Integer> search(String text, String pattern) {

        List<Integer> occurrences = new ArrayList<>();

        // ==========================================
        // Input Validation
        // ==========================================

        if (text == null || pattern == null) {
            return occurrences;
        }

        if (pattern.isEmpty() || text.isEmpty()) {
            return occurrences;
        }

        // ==========================================
        // Normalize Text and Pattern
        // ==========================================

        String normalizedText = text.toLowerCase();
        String normalizedPattern = pattern.toLowerCase();

        // ==========================================
        // Build LPS Array
        // ==========================================

        int[] lps = buildLPS(normalizedPattern);

        int i = 0;
        int j = 0;

        // ==========================================
        // KMP Searching Process
        // ==========================================

        while (i < normalizedText.length()) {

            if (normalizedText.charAt(i)
                    == normalizedPattern.charAt(j)) {

                i++;
                j++;

                // Complete pattern found
                if (j == normalizedPattern.length()) {

                    occurrences.add(
                            i - normalizedPattern.length()
                    );

                    // Continue searching for overlapping matches
                    j = lps[j - 1];
                }

            } else {

                if (j != 0) {

                    // Use previously calculated LPS information
                    j = lps[j - 1];

                } else {

                    i++;
                }
            }
        }

        return occurrences;
    }

    // ==========================================
    // Check Whether Pattern Exists
    // ==========================================

    /**
     * Returns true if the pattern occurs at least once in the text.
     *
     * This method is useful for the Digital Library Search Engine
     * because we usually only need to know whether a book matches.
     *
     * @param text text to search
     * @param pattern pattern to find
     * @return true if a match exists
     */
    public static boolean contains(String text, String pattern) {

        return !search(text, pattern).isEmpty();
    }
}