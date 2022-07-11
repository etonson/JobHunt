package com.exe;

import java.time.Duration;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OneZeroFour {
	private static String geckoLocal = "D:\\javaLib\\selenium-java-3.10.0\\Firefox\\geckodriver.exe";
	private static String fileLocal = "D:\\javaLib\\selenium-java-3.10.0\\Firefox";
	private static String baseURL = "https://www.518.com.tw/";
	private static String searchNmae = "室內設計";
	private static JSONObject obj = null;

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Start");
		System.setProperty("webdriver.gecko.driver", geckoLocal);
		WebDriver driver = new FirefoxDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		driver.manage().window().maximize();

		driver.get(baseURL);
		Thread.sleep(5000);
		WebElement agent = driver.findElement(By.name("ad"));
		JavascriptExecutor javascriptExecutor01 = (JavascriptExecutor) driver;
		javascriptExecutor01.executeScript("arguments[0].value='" + searchNmae + "';", agent);

		agent = driver.findElement(By.id("searchBtn"));
		new Actions(driver).click(agent).perform();
//===========second page---------
		Thread.sleep(3000);
		// 滑至最下面
		int pageTotal = getPageTotalLabel(driver, wait);
		JSONArray requestOrderArray = new JSONArray();
		System.out.println("共計" + pageTotal + "頁");
		int tableTotal;

		for (int j = 1; j <= pageTotal; j++) {
			System.out.println("第" + j + "頁");
			try {
				tableTotal = getRowsTotalLabel(driver, wait);
				System.out.println("共計:" + (tableTotal - 1) + "列資料");
				for (int i = 2; i <= tableTotal; i++) {
					try {
						obj = new JSONObject();
						String rowI = String.valueOf(i);
						System.out.println("第:" + i + "列資料");
						WebElement getTable = driver.findElement(By
								.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/form/div/ul[" + rowI + "]/li[1]/a/h2"));
						obj.put("職位", getTable.getText().toString());

						getTable = driver.findElement(
								By.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/form/div/ul[" + rowI + "]/li[6]/a"));
						obj.put("公司", getTable.getText().toString());

						getTable = driver.findElement(By
								.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/form/div/ul[" + rowI + "]/li[8]/p[1]"));
						obj.put("薪水", getTable.getText().toString());

						getTable = driver.findElement(By
								.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/form/div/ul[" + rowI + "]/li[8]/p[2]"));
						obj.put("工作內容", getTable.getText().toString());

						getTable = driver.findElement(
								By.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/form/div/ul[" + rowI + "]/li[2]"));
						obj.put("地點", getTable.getText().toString());
						getSecondPageData(driver, wait, rowI);
						requestOrderArray.put(obj);
						System.out.println(obj.toString());
					} catch (NoSuchElementException e) {
						System.out.println(e.getRawMessage());
						WebElement nextPage = driver.findElement(By.linkText("下一頁"));
						new Actions(driver).click(nextPage).perform();
						Thread.sleep(3000);
					}
				}
			} catch (NoSuchElementException e) {
				System.out.println(e.getRawMessage());
			}
		}
		Thread.sleep(1000);
	}

	private static int getPageTotalLabel(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		WebElement agent = driver.findElement(By.name("ad"));
		Thread.sleep(3000);
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
		agent = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/div[2]/div/span/span"));
		String dataPage = agent.getText().split("/")[1].replace(" ", "");
		int dataPageI = Integer.parseInt(dataPage);
		return dataPageI;
	}

	private static int getRowsTotalLabel(WebDriver driver, WebDriverWait wait) throws InterruptedException {
		WebElement agent = driver.findElement(By.name("showSelectForm"));
		List<WebElement> webList = agent.findElements(By.tagName("ul"));
		return webList.size();
	}

	private static void getSecondPageData(WebDriver driver, WebDriverWait wait, String hrefStr)
			throws InterruptedException {
		String winHandleBefore = driver.getWindowHandle();
		WebElement agent = driver
				.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/form/div/ul[" + hrefStr + "]/li[1]/a"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click()", agent);
		Thread.sleep(2000);
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
		try {
			agent = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[2]/div[1]/div[2]/div/ul"));
			List<WebElement> webList = driver.findElements(By.tagName("li"));
			System.out.println(webList.get(0).getText());
		} catch (NoSuchElementException e) {
			agent = driver.findElement(
					By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[2]/div[1]/div[2]/div/ul/li[11]/span"));
		}
		obj.put("需求人數", agent.getText().toString());
		driver.close();
		driver.switchTo().window(winHandleBefore);
		Thread.sleep(1000);
		driver.switchTo().defaultContent();

	}

}
