package library.ui;

import library.engine.SearchEngine;
import library.model.Book;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Graphical search window for the Digital Library.
 *
 * Purpose:
 * Provides a simple popup-based interface for searching
 * the local digital-library corpus.
 *
 * The UI does not perform searching itself.
 * It sends the user's query to SearchEngine.
 *
 * Search selection:
 * - Single word/phrase -> KMP
 * - Multiple words -> Aho-Corasick
 */
public class SearchWindow extends JFrame {

    // ==========================================
    // UI Components
    // ==========================================

    private JTextField searchField;
    private JButton searchButton;
    private JLabel statusLabel;

    // ==========================================
    // Search Engine
    // ==========================================

    private final SearchEngine searchEngine;

    // ==========================================
    // Constructor
    // ==========================================

    /**
     * Creates the Digital Library search window.
     *
     * @param searchEngine initialized SearchEngine
     */
    public SearchWindow(SearchEngine searchEngine) {

        this.searchEngine = searchEngine;

        initializeWindow();
        initializeComponents();
    }

    // ==========================================
    // Initialize Window
    // ==========================================

    /**
     * Configures the main popup window.
     */
    private void initializeWindow() {

        setTitle("Digital Library Search");

        setSize(550, 250);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));
    }

    // ==========================================
    // Initialize UI Components
    // ==========================================

    /**
     * Creates and arranges the search components.
     */
    private void initializeComponents() {

        // ==========================================
        // Title
        // ==========================================

        JLabel titleLabel =
                new JLabel(
                        "DIGITAL LIBRARY SEARCH",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        add(
                titleLabel,
                BorderLayout.NORTH
        );

        // ==========================================
        // Search Panel
        // ==========================================

        JPanel searchPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        20
                )
        );

        JLabel searchLabel =
                new JLabel("Search:");

        searchField =
                new JTextField(25);

        searchButton =
                new JButton("SEARCH");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        add(
                searchPanel,
                BorderLayout.CENTER
        );

        // ==========================================
        // Status Label
        // ==========================================

        statusLabel =
                new JLabel(
                        "Books loaded: "
                                + searchEngine.getBookCount(),
                        SwingConstants.CENTER
                );

        add(
                statusLabel,
                BorderLayout.SOUTH
        );

        // ==========================================
        // Search Button Event
        // ==========================================

        searchButton.addActionListener(
                event -> performSearch()
        );

        // ==========================================
        // Enter Key Event
        // ==========================================

        searchField.addActionListener(
                event -> performSearch()
        );
    }

    // ==========================================
    // Perform Search
    // ==========================================

    /**
     * Gets the user's query and sends it to the
     * appropriate search algorithm through SearchEngine.
     */
    private void performSearch() {

        String query =
                searchField.getText().trim();

        // ==========================================
        // Validate Query
        // ==========================================

        if (query.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a search term.",
                    "Empty Search",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // ==========================================
        // Select Search Algorithm
        // ==========================================

        List<Book> results;

        if (query.contains(" ")) {

            // Multiple words:
            // Use Aho-Corasick.
            results =
                    searchEngine.searchMultiple(query);

        } else {

            // Single word:
            // Use KMP.
            results =
                    searchEngine.search(query);
        }

        // ==========================================
        // Display Results
        // ==========================================

        displayResults(
                query,
                results
        );
    }

    // ==========================================
    // Display Search Results
    // ==========================================

    /**
     * Displays matching books in a popup.
     *
     * @param query original search query
     * @param results matching books
     */
    private void displayResults(
            String query,
            List<Book> results) {

        if (results.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No books found for:\n\n"
                            + query,
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE
            );

            statusLabel.setText(
                    "No results found"
            );

            return;
        }

        // ==========================================
        // Build Result Text
        // ==========================================

        StringBuilder resultText =
                new StringBuilder();

        resultText.append(
                "Search Query: "
        );

        resultText.append(query);

        resultText.append(
                "\nResults Found: "
        );

        resultText.append(
                results.size()
        );

        resultText.append(
                "\n\n"
        );

        resultText.append(
                "========================================\n\n"
        );

        // ==========================================
        // Add Each Book
        // ==========================================

        for (int i = 0;
             i < results.size();
             i++) {

            Book book =
                    results.get(i);

            resultText.append(
                    "RESULT "
            );

            resultText.append(
                    i + 1
            );

            resultText.append(
                    "\n"
            );

            resultText.append(
                    book
            );

            resultText.append(
                    "\n\n"
            );

            resultText.append(
                    "========================================\n\n"
            );
        }

        // ==========================================
        // Create Scrollable Result Area
        // ==========================================

        JTextArea resultArea =
                new JTextArea(
                        resultText.toString()
                );

        resultArea.setEditable(false);

        resultArea.setLineWrap(true);

        resultArea.setWrapStyleWord(true);

        resultArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(resultArea);

        scrollPane.setPreferredSize(
                new Dimension(
                        650,
                        450
                )
        );

        // ==========================================
        // Show Results Popup
        // ==========================================

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE
        );

        // ==========================================
        // Update Status
        // ==========================================

        statusLabel.setText(
                "Results found: "
                        + results.size()
        );
    }
}