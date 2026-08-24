package library.loader;

import library.model.Book;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads book records from TXT files.
 *
 * Purpose:
 * Reads the local digital-library corpus from the data folder
 * and converts each book record into a Book object.
 *
 * Expected TXT format:
 *
 * BOOK_TYPE: Fiction
 * TITLE: The Great Gatsby
 * AUTHOR: F. Scott Fitzgerald
 * ISBN: 9780743273565
 * CATEGORY: Classic
 *
 * Each book is separated by a blank line.
 *
 * Time Complexity:
 * O(n), where n is the total number of characters/lines
 * processed from the TXT files.
 */
public class BookLoader {

    // ==========================================
    // Load Books From a Single TXT File
    // ==========================================

    /**
     * Reads one TXT file and returns all books found in it.
     *
     * @param filePath path of the TXT file
     * @return list of Book objects
     * @throws IOException if the file cannot be read
     */
    public static List<Book> loadFromFile(String filePath) throws IOException {

        List<Book> books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            String bookType = "";
            String title = "";
            String author = "";
            String isbn = "";
            String category = "";

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                // ==========================================
                // Blank Line = End of Current Book
                // ==========================================

                if (line.isEmpty()) {

                    if (!title.isEmpty()) {

                        books.add(new Book(
                                bookType,
                                title,
                                author,
                                isbn,
                                category
                        ));

                    }

                    // Reset fields for the next book
                    bookType = "";
                    title = "";
                    author = "";
                    isbn = "";
                    category = "";

                    continue;
                }

                // ==========================================
                // Read Book Fields
                // ==========================================

                if (line.startsWith("BOOK_TYPE:")) {

                    bookType = getValue(line);

                } else if (line.startsWith("TITLE:")) {

                    title = getValue(line);

                } else if (line.startsWith("AUTHOR:")) {

                    author = getValue(line);

                } else if (line.startsWith("ISBN:")) {

                    isbn = getValue(line);

                } else if (line.startsWith("CATEGORY:")) {

                    category = getValue(line);
                }
            }

            // ==========================================
            // Handle Last Book
            // ==========================================

            if (!title.isEmpty()) {

                books.add(new Book(
                        bookType,
                        title,
                        author,
                        isbn,
                        category
                ));
            }
        }

        return books;
    }

    // ==========================================
    // Extract Value From a Line
    // ==========================================

    /**
     * Converts:
     *
     * TITLE: The Great Gatsby
     *
     * into:
     *
     * The Great Gatsby
     */
    private static String getValue(String line) {

        int separatorIndex = line.indexOf(':');

        if (separatorIndex == -1) {
            return "";
        }

        return line.substring(separatorIndex + 1).trim();
    }

    // ==========================================
    // Load Books From the Entire Data Folder
    // ==========================================

    /**
     * Reads all TXT files inside the specified folder.
     *
     * This allows the project to automatically load:
     *
     * books1.txt
     * books2.txt
     * books3.txt
     *
     * without hardcoding individual files.
     *
     * @param folderPath path of the data folder
     * @return list containing all books
     * @throws IOException if a file cannot be read
     */
    public static List<Book> loadFromFolder(String folderPath) throws IOException {

        List<Book> allBooks = new ArrayList<>();

        File folder = new File(folderPath);

        // ==========================================
        // Validate Folder
        // ==========================================

        if (!folder.exists() || !folder.isDirectory()) {

            throw new IOException(
                    "Data folder not found: " + folderPath
            );
        }

        // ==========================================
        // Find TXT Files
        // ==========================================

        File[] files = folder.listFiles((directory, name) ->
                name.toLowerCase().endsWith(".txt")
        );

        if (files == null) {
            return allBooks;
        }

        // ==========================================
        // Read Every TXT File
        // ==========================================

        for (File file : files) {

            System.out.println("Loading: " + file.getName());

            List<Book> books = loadFromFile(file.getPath());

            allBooks.addAll(books);
        }

        return allBooks;
    }
}