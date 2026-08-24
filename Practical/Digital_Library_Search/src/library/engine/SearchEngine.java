package library.engine;

import library.model.Book;
import library.search.AhoCorasick;
import library.search.KMPSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Central search engine for the Digital Library.
 *
 * Purpose:
 * Connects the loaded book dataset with the appropriate
 * string-searching algorithm.
 *
 * Algorithm Selection:
 *
 * 1. KMP
 *    Used for a single search pattern.
 *
 * 2. Aho-Corasick
 *    Used when the query contains multiple words.
 *
 * The engine searches across:
 * - Book Type
 * - Title
 * - Author
 * - ISBN
 * - Category
 *
 * Time Complexity:
 *
 * KMP:
 * O(n + m) per book
 *
 * Aho-Corasick:
 * O(n + total pattern length + matches)
 *
 * Where:
 * n = length of searchable book text
 * m = length of search pattern
 */
public class SearchEngine {

    // ==========================================
    // Book Dataset
    // ==========================================

    private final List<Book> books;

    // ==========================================
    // Constructor
    // ==========================================

    /**
     * Creates a SearchEngine using the loaded books.
     *
     * @param books list of books loaded from TXT files
     */
    public SearchEngine(List<Book> books) {

        if (books == null) {

            this.books = new ArrayList<>();

        } else {

            this.books = books;
        }
    }

    // ==========================================
    // Single-Pattern Search Using KMP
    // ==========================================

    /**
     * Searches the entire book collection using KMP.
     *
     * This is the primary search method for a single
     * keyword or phrase.
     *
     * Example:
     *
     * "gatsby"
     * "Fitzgerald"
     * "classic"
     * "9780743273565"
     *
     * @param query search keyword or phrase
     * @return matching books
     */
    public List<Book> search(String query) {

        List<Book> results = new ArrayList<>();

        // ==========================================
        // Validate Query
        // ==========================================

        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        String normalizedQuery = query.trim();

        // ==========================================
        // Search Every Book
        // ==========================================

        for (Book book : books) {

            String searchableText =
                    createSearchableText(book);

            // ==========================================
            // KMP Pattern Matching
            // ==========================================

            if (KMPSearch.contains(
                    searchableText,
                    normalizedQuery)) {

                results.add(book);
            }
        }

        return results;
    }

    // ==========================================
    // Multiple-Word Search Using Aho-Corasick
    // ==========================================

    /**
     * Searches the book collection using Aho-Corasick.
     *
     * Each word in the query becomes a pattern.
     *
     * Example:
     *
     * Query:
     * "algorithm data structure"
     *
     * Patterns:
     * algorithm
     * data
     * structure
     *
     * Aho-Corasick searches all patterns in a book
     * during one traversal.
     *
     * @param query multiple search words
     * @return matching books
     */
    public List<Book> searchMultiple(String query) {

        List<Book> results = new ArrayList<>();

        // ==========================================
        // Validate Query
        // ==========================================

        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        // ==========================================
        // Extract Search Patterns
        // ==========================================

        String[] words =
                query.toLowerCase()
                        .trim()
                        .split("\\s+");

        // ==========================================
        // Remove Duplicate Words
        // ==========================================

        List<String> patterns =
                new ArrayList<>(
                        Arrays.asList(words)
                );

        // ==========================================
        // Search Each Book
        // ==========================================

        for (Book book : books) {

            String searchableText =
                    createSearchableText(book);

            // ==========================================
            // Build Aho-Corasick Automaton
            // ==========================================

            AhoCorasick ahoCorasick =
                    new AhoCorasick();

            for (String pattern : patterns) {

                ahoCorasick.addPattern(pattern);
            }

            // ==========================================
            // Build Failure Links
            // ==========================================

            ahoCorasick.buildFailureLinks();

            // ==========================================
            // Search Book
            // ==========================================

            List<AhoCorasick.Match> matches =
                    ahoCorasick.search(searchableText);

            // ==========================================
            // Add Matching Book
            // ==========================================

            if (!matches.isEmpty()) {

                results.add(book);
            }
        }

        return results;
    }

    // ==========================================
    // Create Searchable Book Text
    // ==========================================

    /**
     * Combines all searchable fields of a book.
     *
     * This allows the user to search by:
     *
     * Book Type
     * Title
     * Author
     * ISBN
     * Category
     *
     * @param book book whose fields are combined
     * @return searchable text
     */
    private String createSearchableText(Book book) {

        return book.getBookType() + " "
                + book.getTitle() + " "
                + book.getAuthor() + " "
                + book.getIsbn() + " "
                + book.getCategory();
    }

    // ==========================================
    // Get Total Number of Books
    // ==========================================

    /**
     * Returns the total number of loaded books.
     *
     * @return number of books
     */
    public int getBookCount() {

        return books.size();
    }
}