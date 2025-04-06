// Import necessary libraries for input/output, GUI, and system operations
import java.util.*;
import java.io.*;
import java.awt.Desktop;
import javax.swing.JFileChooser;

/**
 * Main class for the User Management System.
 * Handles customer data management including reading/writing CSV files,
 * serialization, sorting, and searching.
 */
public class Users {
    // Shared scanner object for console input throughout the class
    static Scanner input = new Scanner(System.in);
    
    /**
     * Main program entry point.
     * @param args Command line arguments (not used)
     * @throws Exception Propagates exceptions for file operations and GUI interactions
     */
    public static void main(String[] args) throws Exception {
        // List to store customer objects loaded from CSV
        List<Customer> customer = new ArrayList<>();

        // Initial system setup and file loading
        System.out.println("------------WELCOME------------");
        System.out.println("Enter CSV file path:\n");
        String path = pathSelector();  // Get initial CSV file through GUI
        customer = CSV.read(path);     // Load customers from CSV

        // Main program loop for user interaction
        while(true) {
            // Display menu options
            System.out.println("\n1. Add Member");
            System.out.println("2. View CSV file");
            System.out.println("3. Serialise");
            System.out.println("4. Deserialise");
            System.out.println("5. Sort Users by water usage");
            System.out.println("6. Search ");
            System.out.println("---------------------------------");

            System.out.print("Enter Option : ");
            int Option = input.nextInt();
        
            // Handle user selection using switch expression
            switch(Option) {
                case 1 -> CSV.append(path);  // Add new customer to CSV
                case 2 -> Open(path);         // Open CSV in default app
                case 3 -> serialize.ser(customer, pathSelector());  // Save serialized data
                case 4 -> serialize.deser(customer, pathSelector()); // Load serialized data
                case 5 -> writeCustomersToFile(filter(sort(customer)), "file.txt");  // Process data
                case 6 -> linearSearch(customer);  // Search by customer ID
                default -> System.out.println("INVALID OPTION");    
            }
            
            // Continuation prompt
            System.out.println("\npress x to EXIT;\npress any other key to continue");
            if(input.next().equals("x")) {
                break;  // Exit main loop
            }
        }
    }
    
    /**
     * Writes processed customer data to a text file.
     * @param customers List of customers to write
     * @param filePath Destination file path
     */
    public static void writeCustomersToFile(List<Customer> customers, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("===== Processed Customer Data =====\n");
            for (Customer customer : customers) {
                writer.write(customer.formatTXT() + System.lineSeparator());
            }
            System.out.println("Customer data has been written to the file.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    /**
     * Opens a file using the system's default application.
     * @param fileName Path to file to open
     */
    public static void Open(String fileName) throws Exception {
        File fileToOpen = new File(fileName);
        if(Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(fileToOpen);
        } else {
            System.out.println("File opening not supported in your system");
        }
    }
    
    /**
     * Shows GUI dialog for file selection.
     * @return Absolute path of selected file
     */
    public static String pathSelector() throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null);
        String path = "";
        if(result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            System.out.println("Selected save location:" + path);
        } else {
            System.out.println("Save operation cancelled.");
        }
        return path;
    }
    
    /**
     * Linear search through customers by ID.
     * @param data List of customers to search
     */
    public static void linearSearch(List<Customer> data) {
        System.out.println("Enter ID number to search by e.g 100");
        int target = input.nextInt();
        for (Customer customer : data) {
            if (customer.getId() == target) {
                System.out.println("Found the customer:\n" + customer);
                return;
            }
        }
        System.out.println("Customer with ID " + target + " not found.");
    }
    
    /**
     * Sorts customers by water usage in descending order using insertion sort.
     * @param unsortedList Input list of customers
     * @return Sorted list of customers
     */
    public static List<Customer> sort(List<Customer> unsortedList) {
        for (int i = 1; i < unsortedList.size(); i++) {
            Customer current = unsortedList.get(i);
            double currentUsage = current.getWaterUsage();
            int j = i - 1;

            // Shift elements greater than current to the right
            while (j >= 0 && unsortedList.get(j).getWaterUsage() < currentUsage) {
                unsortedList.set(j + 1, unsortedList.get(j));
                j--;
            }
            unsortedList.set(j + 1, current);
        }
        System.out.println("Users sorted successfully by water usage");
        return unsortedList;
    }

    /**
     * Filters customers with water usage >= 30 m³.
     * @param unfiltered Full list of customers
     * @return Filtered list meeting water usage threshold
     */
    public static List<Customer> filter(List<Customer> unfiltered) {
        List<Customer> filtered = new ArrayList<>();
        for (Customer customer : unfiltered) {
            if (customer.getWaterUsage() >= 30) {
                filtered.add(customer);
            }
        }
        System.out.println("Filtered out users with usage <30 m³");
        return filtered;
    }
}

/**
 * Represents a customer with water usage data.
 * Implements Serializable for object persistence.
 */
class Customer implements Serializable {
    // Static counter tracks total created instances
    private static int tracker = 0;
    
    // Immutable customer properties
    private final int customerID;
    private final String customerName;
    private final String address;
    private final double waterUsage;

    /**
     * Customer constructor.
     * @param customerID Unique identifier
     * @param customerName Full name
     * @param address Physical address
     * @param waterUsage Cubic meters used
     */
    public Customer(int customerID, String customerName, 
                   String address, double waterUsage) {
        tracker++;
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.waterUsage = waterUsage;
    }

    // Accessor methods
    public static int getLastId() { return tracker; }
    public int getId() { return customerID; }
    public String getCustomerName() { return customerName; }
    public double getWaterUsage() { return waterUsage; }

    /**
     * Formats customer data for text file output.
     * @return Formatted string with key details
     */
    public String formatTXT() {
        return String.format("ID: %d | Name: %-20s | Usage: %.1f m³", 
            customerID, customerName, waterUsage);
    }
    
    /**
     * Full string representation for debugging.
     * @return Complete customer details
     */
    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Address: %s, Usage: %.1f m³",
            customerID, customerName, address, waterUsage);
    }
}

/**
 * Handles CSV file operations for customer data.
 */
class CSV {
    static Scanner input = new Scanner(System.in);
    
    /**
     * Reads customer data from CSV file.
     * @param path File path to read from
     * @return List of parsed Customer objects
     */
    public static List<Customer> read(String path) {
        List<Customer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine(); // Skip header
            String line;
            while((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                list.add(new Customer(
                    Integer.parseInt(data[0]),
                    data[1],
                    data[2],
                    Double.parseDouble(data[3])
                ));
            }
        } catch (IOException e) {
            System.err.println("CSV read error: " + e.getMessage());
        }
        return list;
    }

    /**
     * Appends new customer data to CSV file.
     * @param file CSV file path to append to
     */
    public static void append(String file) {
        try (FileWriter fw = new FileWriter(file, true)) {
            System.out.print("Enter CustomerID: ");
            fw.append(input.nextLine()).append(',');
            
            System.out.print("Enter Name: ");
            fw.append(input.nextLine()).append(',');
            
            System.out.print("Enter Address: ");
            fw.append(input.nextLine()).append(',');
            
            System.out.print("Enter Water Usage: ");
            fw.append(input.nextLine()).append("\n");
            
            System.out.println("New customer added successfully");
        } catch (Exception e) {
            System.err.println("CSV append error: " + e.getMessage());
        }
    }
}   

/**
 * Handles object serialization/deserialization operations.
 */
class serialize {
    /**
     * Serializes customer list to file.
     * @param list Customers to serialize
     * @param path Destination file path
     */
    public static void ser(List<Customer> list, String path) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(path))) {
            for (Customer customer : list) {
                oos.writeObject(customer);
            }
            System.out.println("Serialization completed");
        }
    }

    /**
     * Deserializes customer objects from file.
     * @param OriginalList Original list for size reference (caution: potential issue)
     * @param path Source file path
     * @return List of deserialized customers
     */
    public static List<Customer> deser(List<Customer> OriginalList, String path) throws Exception {
        List<Customer> list = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream(path))) {
            for (int i = 0; i < OriginalList.size(); i++) {
                list.add((Customer) ois.readObject());
            }
            System.out.println("Deserialization completed");
        }
        return list;
    }
}
