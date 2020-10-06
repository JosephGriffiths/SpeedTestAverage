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
		// over and not know what to do.
		// Print Starting Test
		System.out.println("Starting speed test");
		// Creates list that takes ints/doubles called pingValues, create new array for
		// collection of data type
		List<Integer> pingValues = new ArrayList<Integer>();
		List<Double> downloadValues = new ArrayList<Double>();
		List<Double> uploadValues = new ArrayList<Double>();
		// For loop i=starting value, i<2=while i is less than x, i++ add 1 to i
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
			// Web driver wait creates wait called waitForCookie that waits for 120 seconds
			// or till condition is met.
			WebDriverWait waitForCookie = new WebDriverWait(driver, 120);
			// Calls wait for cookie until element is visible
			waitForCookie.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("/html//div[@id='qc-cmp2-ui']//div[@class='qc-cmp2-summary-buttons']/button[2]")));
			// Finds allow button and clicks it
			WebElement cookieConsent = driver.findElement(
					By.xpath("/html//div[@id='qc-cmp2-ui']//div[@class='qc-cmp2-summary-buttons']/button[2]"));
			cookieConsent.click();
			// Start speed test
			// Find button to click to start speed test
			WebElement startSpeedTest = driver.findElement(By.cssSelector("figure#control  text"));
			startSpeedTest.click();
			// Wait until element is visible before continuing
			WebDriverWait wait = new WebDriverWait(driver, 120);
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[@id='sc-results-panel']//button[@name='maximumSpeed']")));
			// Get URL and store
			// Webscrape Ping, Download Speed and Upload Speed
			String speedTestUrl = driver.getCurrentUrl();
			// Tells jsoup to go to url and get the HTML
			Document doc = Jsoup.connect(speedTestUrl).get();
			// Traverse - Travel through the HTML and find and look for ID ping, download,
			// upload. When element found it will extract element
			// From the HTML.
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
		// Prints the array values
		System.out.println(calculateAveragePing(pingValues));
		System.out.println(calculateAverage(downloadValues));
		System.out.println(calculateAverage(uploadValues));
		pingValues.size();
	}

	// Define new function for ping as double because its dividing. Takes list of
	// ints called pingValues.
	private double calculateAveragePing(List<Integer> pingValues) {
		// Defines Starting point of the array
		Integer sum = 0;
		// If ping values array is not empty continue
		if (!pingValues.isEmpty()) {
			// For each integer in the ping values list increment the sum values by the next
			// value on the list.
			for (Integer mark : pingValues) {
				sum += mark;
			}
			// Divides the sum value of the the array by the size
			return sum.doubleValue() / pingValues.size();
		}
		// Returns the sum for the print statemnt in the public void.
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
