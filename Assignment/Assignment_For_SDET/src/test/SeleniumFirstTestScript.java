package test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SeleniumFirstTestScript {
	
	public static void main(String[] args) throws InterruptedException, JsonMappingException, JsonProcessingException {
		
		// Create a new instance of the Chrome driver.
		WebDriver driver = new ChromeDriver();
		
		// JSON data representing a list of persons with names, ages, and genders.
		 String json_data = "[{\"name\": \"Bob\", \"age\": 20, \"gender\": \"male\"}, " +
	                 		"{\"name\": \"George\", \"age\": 42, \"gender\": \"male\"}, " +
	                 		"{\"name\": \"Sara\", \"age\": 42, \"gender\": \"female\"}, " +
	                 		"{\"name\": \"Conor\", \"age\": 40, \"gender\": \"male\"}, " +
	                 		"{\"name\": \"Jennifer\", \"age\": 42, \"gender\": \"female\"}]";

		// Navigate to a test page containing a dynamic table.
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		
		// Find the button to display table data using XPath.
		WebElement table_data_button = driver.findElement(By.xpath("/html/body/div/div[3]/details/summary"));

		// Create a wait object to wait for the button to be displayed.
//		Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	    wait.until(d -> table_data_button.isDisplayed());
		
		Thread.sleep(2000);
	    
	 // Click the button to display the table data.
	    table_data_button.click();	
		
	 // Find the text area on the page where JSON data can be entered.
		WebElement text_area = driver.findElement(By.id("jsondata"));
		
		Thread.sleep(2000);
		
		// Clear any existing content in the text area and input the JSON data.
		text_area.clear();
		
		Thread.sleep(2000);
		
		text_area.sendKeys(json_data);
		
		Thread.sleep(2000);

		// Trigger the refresh action to update the table with the provided JSON data.
		driver.findElement(By.id("refreshtable")).click();

		Thread.sleep(2000);

		// Create an ObjectMapper instance for JSON deserialization.
        ObjectMapper objectMapper = new ObjectMapper();

     // Initialize a List<Map<String, Object>> to store the deserialized data.
        List<Map<String, Object>> dataList = null; 
	
		        // Locate the table element by its HTML attribute (you may need to inspect the page source)
		        WebElement table = driver.findElement(By.id("dynamictable"));

		        // Retrieve the data from the table
		        List<WebElement> rows = table.findElements(By.tagName("tr"));

		     // Use the ObjectMapper to deserialize the JSON data into a List<Map<String, Object>>.
		        dataList = objectMapper.readValue(json_data,
	                    new TypeReference<List<Map<String, Object>>>() {});
		        
		     // Initialize lists to store expected and actual values.
		        List<Person> expectdValues = new ArrayList<>();
		        List<Person> actualValues = new ArrayList<>();

		     // Iterate through the deserialized data (List<Map<String, Object>>).
		        for (Map<String, Object> data : dataList) {
		            // Create a new Person object using the values from the map and add it to the expectedValues list.
                    Person person = new Person(data.get("name").toString(), Integer.parseInt(data.get("age").toString()), data.get("gender").toString());
                    
                    // Add the created Person object to the expectedValues list.
                    expectdValues.add(person);
	            }
		        
		     // Iterate through the row.
		        for (int i = 1; i < rows.size(); i++) { 
		        	// Find all the <td> elements within the i-th row of the table and store them in a List<WebElement>.
		               List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
                       Person person = new Person();

                    // Iterate through the cells of a row
		                for (int j = 0; j < cells.size(); j++) {
		                	// Get the text content of the current cell.
		                    String actualValue = cells.get(j).getText();
		                    
		                    // Check if the current cell is the first cell (index 0) and the text content is not null.
		                    if(j == 0 && actualValue != null) {
		                        // Set the 'name' property of a 'person' object based on the text content of the first cell.
		                    	person.setName(actualValue);
		                    }
		                    // Check if the current cell is the second cell (index 1) and the text content is not null.
		                    else if(j == 1 && actualValue != null) {
		                        // Parse the text content as an integer and set the 'age' property of the 'person' object.
		                    	person.setAge(Integer.parseInt(actualValue));
		                    }
		                    // For any other cell or if the text content is not null.
		                    else if (actualValue != null){
		                        // Set the 'gender' property of the 'person' object based on the text content of the current cell.
		                    	person.setGender(actualValue);
		                    }
		                }
		                // Add the created Person object to the actualValues list.
		                actualValues.add(person);
		            }
		        
		     // Check if the data representation of actualValues is equal to the data representation of expectedValues.
		        if(actualValues.toString().equals(expectdValues.toString())) {
		            // Print a message indicating that the stored data and the data populated on the UI both match.
		        	System.out.println("Stored data and the data populated on UI both are Matching");
		        }
		        else {
		            // Print a message indicating that the stored data and the data populated on the UI do not match.
		        	System.out.println("Stored data and the data populated on UI both are Matching");

		        }

		        // Close the browser window
		        driver.quit();

	}
}
