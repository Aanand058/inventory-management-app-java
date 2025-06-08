
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.scene.control.TableView;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;

public class DatabaseAccess {

	private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/apd";

	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "root";

	public void saveInventoryToDatabase() {
		try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {

			PreparedStatement ct = conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS Part (id INT PRIMARY KEY, name VARCHAR(255), price DECIMAL(10, 2), stock INT, min INT, max INT, machineOrCompanyName VARCHAR(255), UNIQUE(name))");
			ct.executeUpdate();

			PreparedStatement ct1 = conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS Product (id INT PRIMARY KEY, name VARCHAR(255), price DECIMAL(10, 2), stock INT, min INT, max INT,UNIQUE(name))");
			ct1.executeUpdate();

			PreparedStatement ct2 = conn.prepareStatement(
					"CREATE TABLE IF NOT EXISTS ProductPart (productId INT, partId INT, FOREIGN KEY (productId) REFERENCES Product(id), FOREIGN KEY (partId) REFERENCES Part(id))");
			ct2.executeUpdate();

			// Save parts
			for (Part part : Inventory.getAllParts()) {
				savePartToDatabase(part);
			}

			// Save products
			for (Product product : Inventory.getAllProducts()) {
				saveProductToDatabase(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean savePartToDatabase(Part part) throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

			PreparedStatement statement = conn
					.prepareStatement("INSERT INTO Part (id, name, price, stock, min, max,machineOrCompanyName ) VALUES (?, ?, ?, ?, ?, ?,?)");
			statement.setInt(1, part.getId());
			statement.setString(2, part.getName());
			statement.setDouble(3, part.getPrice());
			statement.setInt(4, part.getStock());
			statement.setInt(5, part.getMin());
			statement.setInt(6, part.getMax());
			statement.setString(7, (part instanceof InHouse) ? String.valueOf(((InHouse) part).getMachine()) : ((Outsourced) part).getCompanyName());
			
			statement.executeUpdate();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return false;
	}

	public boolean saveProductToDatabase(Product product) throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

			PreparedStatement statement = conn.prepareStatement(
					"INSERT INTO Product (id, name, price, stock, min, max) VALUES (?, ?, ?, ?, ?, ?)");
			statement.setInt(1, product.getId());
			statement.setString(2, product.getName());
			statement.setDouble(3, product.getPrice());
			statement.setInt(4, product.getStock());
			statement.setInt(5, product.getMin());
			statement.setInt(6, product.getMax());
			statement.executeUpdate();

			// Save associated parts
			for (Part associatedPart : product.getAllAssociatedParts()) {
				saveProductPartToDatabase(product.getId(), associatedPart.getId());
			}
			return true;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return false;
	}

	public boolean saveProductPartToDatabase(int productId, int partId) throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

			PreparedStatement statement = conn
					.prepareStatement("INSERT INTO ProductPart (productId, partId) VALUES (?, ?)");
			statement.setInt(1, productId);
			statement.setInt(2, partId);
			statement.executeUpdate();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return false;
	}
	
	
	 public void loadInventoryFromDatabase() {
	        try {
	        	Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD); 
	        
	        	loadParts();
	            loadProducts();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	 
	 public boolean loadParts() throws SQLException {
		    try {
		        Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

		        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Part");
		        ResultSet resultSet = statement.executeQuery();
		        while (resultSet.next()) {
		            int id = resultSet.getInt("id");
		            String name = resultSet.getString("name");
		            double price = resultSet.getDouble("price");
		            int stock = resultSet.getInt("stock");
		            int min = resultSet.getInt("min");
		            int max = resultSet.getInt("max");

		            // Check if the part is InHouse or Outsourced
		            String machineOrCompanyName = resultSet.getString("machineOrCompanyName");
		            //System.out.println("machineOrCompanyName: " + machineOrCompanyName);
		            int machineId;
		            String companyName;
		            Part part;
		            
		            
		            //If part contains numeric, else part contains comapany name "String"
		            if (machineOrCompanyName.matches("[0-9]+")) {
			            System.out.println("machineID: " + machineOrCompanyName);

		   
		                machineId = Integer.parseInt(machineOrCompanyName);
		                companyName = null; 
		                
		                part = new InHouse(id, name, price, stock, min, max, machineId);
		                
		            } else {
			            System.out.println("CompanyName: " + machineOrCompanyName);

		               
		                companyName = machineOrCompanyName;
		                machineId = 0; 
		 
		                part = new Outsourced(id, name, price, stock, min, max, companyName);
		            }  
		          

		            // Add the part to the inventory
		            Inventory.addPart(part);
		        }
		        return true;
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return false;
		}

	
	public boolean loadProducts() throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

			
			 PreparedStatement statement = conn
					 .prepareStatement("SELECT * FROM Product");
			 ResultSet resultSet = statement.executeQuery();
			 while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String name = resultSet.getString("name");
	                double price = resultSet.getDouble("price");
	                int stock = resultSet.getInt("stock");
	                int min = resultSet.getInt("min");
	                int max = resultSet.getInt("max");

	              
	                Product product = new Product(id, name, price, stock, min, max);

	                //Load associated parts 
	                loadAsoParts(product);

	                // Add product to inventory
	                Inventory.addProduct(product);
	            }
			
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();

		}
		return false;
	}
	
	public boolean loadAsoParts(Product product) throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

		
		 PreparedStatement statement = conn
				 .prepareStatement("SELECT Part.* FROM Part INNER JOIN ProductPart ON Part.id = ProductPart.partId WHERE ProductPart.productId = ?");
		 statement.setInt(1, product.getId());
		 
		 
		 ResultSet resultSet = statement.executeQuery();
		
		 while (resultSet.next()) {
             int id = resultSet.getInt("id");
             String name = resultSet.getString("name");
             double price = resultSet.getDouble("price");
             int stock = resultSet.getInt("stock");
             int min = resultSet.getInt("min");
             int max = resultSet.getInt("max");

             // Check if the part is InHouse or Outsourced
	            String machineOrCompanyName = resultSet.getString("machineOrCompanyName");
	            //System.out.println("machineOrCompanyName: " + machineOrCompanyName);
	            int machineId;
	            String companyName;
	            Part part;
	            
	            
	            
	            if (machineOrCompanyName.matches("[0-9]+")) {
		            System.out.println("machineID: " + machineOrCompanyName);

	               
	                machineId = Integer.parseInt(machineOrCompanyName);
	                companyName = null; 
	                
	                part = new InHouse(id, name, price, stock, min, max, machineId);
	                
	            } else {
		            System.out.println("CompanyName: " + machineOrCompanyName);

	              
	                companyName = machineOrCompanyName;
	                machineId = 0; 
	                
	                part = new Outsourced(id, name, price, stock, min, max, companyName);
	            }


             // Add associated part to product
             product.addAssociatedPart(part);
         }
		
		
		return false;
	
	
	}
	
	
	
	
	
	
	
	
	

}
