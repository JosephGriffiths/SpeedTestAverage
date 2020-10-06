package net.speedtest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class SpeedTest {

	@Test
	public void speedTest() throws IOException {
		// I/O Exception to throw if something wrong with URL to stop it from falling
		// Print Starting Test
		System.out.println("Starting speed test");
		//Create list to take web scrape values
		List<Integer> pingValues = new ArrayList<Integer>();
		List<Double> downloadValues = new ArrayList<Double>();
		List<Double> uploadValues = new ArrayList<Double>();
		//Loop for number of times speed test runs
		for (int i = 0; i < 2; i++) {
			// Create driver
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
			WebDriver driver = new ChromeDriver();
			// Open speed test page
			String url = "https://www.broadbandspeedchecker.co.uk/";
			driver.get(url);
			System.out.println("Page is open");
			// Make window full screen
			driver.manage().window().maximize();
			// Clear cookie acceptance
			WebDriverWait waitForCookie = new WebDriverWait(driver, 120);
			// Calls wait for cookie until element is visible
			waitForCookie.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("/html//div[@id='qc-cmp2-ui']//div[@class='qc-cmp2-summary-buttons']/button[2]")));
			WebElement cookieConsent = driver.findElement(
					By.xpath("/html//div[@id='qc-cmp2-ui']//div[@class='qc-cmp2-summary-buttons']/button[2]"));
			cookieConsent.click();
			// Start speed test
			WebElement startSpeedTest = driver.findElement(By.cssSelector("figure#control  text"));
			startSpeedTest.click();
			WebDriverWait wait = new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@id='sc-results-panel']//button[@name='maximumSpeed']")));
			// Get URL 
			String speedTestUrl = driver.getCurrentUrl();
			Document doc = Jsoup.connect(speedTestUrl).get();
			// Find below elements in HTML and extract them.
			for (Element result : doc.select("td[name='ping']")) {
				// Converts ping into a string
				String ping = result.text();
				// Splits sting into array when there is a space
				String[] data = ping.split(" ");
				// Adds item to the array
				pingValues.add(Integer.parseInt(data[0]));
			}
			for (Element result : doc.select("td[name='download']")) {
				String download = result.text();
				String[] data = download.split(" ");
				downloadValues.add(Double.parseDouble(data[0]));
			}
			for (Element result : doc.select("td[name='upload']")) {
				String upload = result.text();
				String[] data = upload.split(" ");
				uploadValues.add(Double.parseDouble(data[0]));
			}
			driver.close();
		}
		// Prints the array values and called calcualte average functions to get average of results.
		System.out.println(calculateAveragePing(pingValues));
		System.out.println(calculateAverage(downloadValues));
		System.out.println(calculateAverage(uploadValues));
		pingValues.size();
	}

	// Define new function for ping average calculation
	private double calculateAveragePing(List<Integer> pingValues) {
		// Defines Starting point of the array
		Integer sum = 0;
		// If ping values array is not empty continue
		if (!pingValues.isEmpty()) {
			// For each integer in the ping values list increment the sum values by the next value on the list.
			for (Integer mark : pingValues) {
				sum += mark;
			}
			// Divides the sum value of the the array by the size
			return sum.doubleValue() / pingValues.size();
		}
		// Returns the sum for the print statement in the public void.
		return sum;
	}

	private double calculateAverage(List<Double> Values) {
		Double sum = 0.0;
		if (!Values.isEmpty()) {
			for (Double mark : Values) {
				sum += mark;
			}
			return sum.doubleValue() / Values.size();
		}
		return sum;
	}
}
