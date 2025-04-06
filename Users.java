


import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Users {
    public static void main(String[] args) throws IOException{
        // Create a list to store customer objects
        List<Customer> customer = new ArrayList<>();
        customer = File.read();
        // Add customer entries to the list (hardcoded data)
        customer.add(new Customer(2023, "Henrie", "Makeni", 30.5));
        customer.add(new Customer(2073, "joe", "kafue", 0.5));
        customer.add(new Customer(2023, "lorem", "lsk", 30.8));
        customer.add(new Customer(2073, "ipsum", "kabwe", 8.5));
 
         // Sort -> Filter -> store results in customer
        customer = filter(sort(customer));

        //
        File.writeTXT(customer);
    }

	public static void writeCustomersToFile(List<Customer> customers, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) { // 'true' enables appending mode
			writer.write("===== Processed Customer Data =====\n");
            for (Customer customer : customers) {
                writer.write(customer.toString() + System.lineSeparator());
            }
            System.out.println("Customer data has been written to the file.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Insertion sort implementation (descending order by water usage)
    public static List<Customer> sort(List<Customer> unsortedList) {
        int j;
        // Start from the second element (index 1)
        for (int i = 1; i < unsortedList.size(); i++) {
            // Get current customer's water usage as key
            double key = unsortedList.get(i).getWaterUsage();
            // Store the entire customer object
            Customer key1 = unsortedList.get(i);
            j = i - 1;  // Start comparing with previous element
            
            // While previous element has LOWER usage than key
            while (j >= 0 && unsortedList.get(j).getWaterUsage() < key) {
                // Shift larger elements to the right
                unsortedList.set(i, unsortedList.get(j));
                j--;
                i--;
            }
            // Insert key in correct position
            unsortedList.set(i, key1);
        }
        return unsortedList;
    }

    // Filter customers with water usage >= 30 m³
    public static List<Customer> filter(List<Customer> unfiltered) {
        List<Customer> filtered = new ArrayList<>();
        // Iterate through all customers
        for (int i = 0; i < unfiltered.size(); i++) {
            // Check if usage meets threshold
            if (unfiltered.get(i).getWaterUsage() >= 30) {
                filtered.add(unfiltered.get(i));
            }
        }
        return filtered;
    }
}








class Customer {
    // Static counter tracks total customers created
    private static int tracker = 0;
    
    // Instance variables
    private int customerID;
    private String customerName;
    private String address;
    private double waterUsage;

    // Constructor
    public Customer(int customerID, String customerName, 
                   String address, double waterUsage) {
        tracker++;  // Increment customer counter
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.waterUsage = waterUsage;
    }

    // Returns total number of customers created
    public static int getLastId() {
        return tracker;
    }

    // Instance getters
    public int getId() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getWaterUsage() {
        return waterUsage;
    }

    // String representation for printing

    public String toString() {
        return customerID+ ", " + customerName + ", " + address + ", " + waterUsage + "\n";
    }
}


class File
{
	public static void writeTXT(List<Customer> list) throws IOException
	{
			BufferedWriter writer = new BufferedWriter(new FileWriter("customer.txt"));
			writer.write("ID,Name,Adress,usage\n");
			for(int i = 0;i<list.size();i++)
			{
				writer.write(list.get(i).toString());
			
			}
			writer.close();

	}
	public static List<Customer> read()
	{
		List<Customer> list = new ArrayList<>();
		
		
		try
		{	BufferedReader br = new BufferedReader(new FileReader("customer.csv"));
			int i= 0;
			String line = br.readLine();
			
			while(line!= null)
			{
				String[] data = line.split(",");
					if(i>0)
					{
						int id = Integer.parseInt(data[0]);
						double usage = Double.parseDouble(data[3]);
						list.add(new Customer(id,data[1],data[2],usage));
					}
				line = br.readLine();
				i++;
			}

		} catch(IOException e)
		{
			e.printStackTrace();
		}
		//System.out.println(list);
		return list;
	}
}
















