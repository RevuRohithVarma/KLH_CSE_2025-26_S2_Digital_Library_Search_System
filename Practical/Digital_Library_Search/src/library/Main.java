package library;

import library.engine.SearchEngine;
import library.loader.BookLoader;
import library.model.Book;
import library.ui.SearchWindow;

import javax.swing.*;
import java.util.List;

/**
 * Main entry point of the Digital Library Search System.
 *
 * Purpose:
 * 1. Load the digital-library corpus from local TXT files.
 * 2. Create the SearchEngine.
 * 3. Start the Java Swing search interface.
 *
 * No database is used.
 */
public class Main {

    // ==========================================
    // Application Entry Point
    // ==========================================

    public static void main(String[] args) {

        try {

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "      DIGITAL LIBRARY SEARCH SYSTEM"
            );

            System.out.println(
                    "=========================================="
            );

            // ==========================================
            // Load Dataset
            // ==========================================

            System.out.println(
                    "Loading digital library dataset..."
            );

            List<Book> books =
                    BookLoader.loadFromFolder("data");

            // ==========================================
            // Validate Dataset
            // ==========================================

            if (books.isEmpty()) {

                System.out.println(
                        "No books found in the data folder."
                );

                JOptionPane.showMessageDialog(
                        null,
                        "No books were found.\n\n"
                                + "Please check the TXT files inside:\n"
                                + "data/",
                        "Dataset Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // ==========================================
            // Display Dataset Information
            // ==========================================

            System.out.println(
                    "Total books loaded: "
                            + books.size()
            );

            System.out.println(
                    "Dataset loaded successfully."
            );

            // ==========================================
            // Create Search Engine
            // ==========================================

            SearchEngine searchEngine =
                    new SearchEngine(books);

            // ==========================================
            // Start Swing UI
            // ==========================================

            SwingUtilities.invokeLater(() -> {

                SearchWindow searchWindow =
                        new SearchWindow(searchEngine);

                searchWindow.setVisible(true);
            });

        } catch (Exception e) {

            // ==========================================
            // Handle Application Errors
            // ==========================================

            System.err.println(
                    "Failed to start Digital Library Search."
            );

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "The application could not start.\n\n"
                            + e.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}