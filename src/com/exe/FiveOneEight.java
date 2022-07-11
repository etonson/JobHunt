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

import to.excel.JsonArrayToExcel;

public class FiveOneEight {
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
		int pageTotal = getPageTotalLabel(driver, wait);
		JSONArray requestOrderArray = new JSONArray();
		System.out.println("共計" + pageTotal + "頁");
		for (int j = 1; j <= pageTotal; j++) {
			System.out.println("第" + j + "頁");
			Thread.sleep(3000);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			try {
				agent = driver.findElement(By.name("showSelectForm"));
				List<WebElement> webList = agent.findElements(By.tagName("ul"));
				System.out.println("共計:" + (webList.size() - 1) + "列資料");
				for (int i = 0; i < webList.size(); i++) {
					obj = new JSONObject();
					agent = webList.get(i).findElement(By.className("title"));
					obj.put("職缺", agent.getText());
					agent = webList.get(i).findElement(By.className("company"));
					obj.put("公司名稱", agent.getText());
					agent = webList.get(i).findElement(By.className("sumbox"));
					obj.put("薪水", agent.getText());
					System.out.println("第"+i+"列");
					System.out.println(obj.toString());
					requestOrderArray.put(obj);
				}
				WebElement nextPage = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/div[2]/div/a[5]"));
				new Actions(driver).click(nextPage).perform();

			} catch (NoSuchElementException e) {
				System.out.println(e.getRawMessage());
			}
		}
		driver.close();
		JsonArrayToExcel exethis = new JsonArrayToExcel("518", "518");
		exethis.doExcelFrom518(requestOrderArray);
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


}
