package com.alliedtesting.ParkingCalculator;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ParkingCalculator {

	@DataProvider(name = "Times", parallel = true)
	public Object[][] getData(){
		return new Object[][] {
			{"3:00","PM","3/18/2019", "7:00","PM","3/20/2019", "$ 72.00", "(2 Days, 4 Hours, 0 Minutes)"},
			{"3:00","AM","3/20/2019", "10:00","AM","3/25/2019", "$ 180.00", "(5 Days, 7 Hours, 0 Minutes)"},
			{"1:00","PM","5/5/2019", "15:30","PM","5/5/2019", "$ 30.00", "(0 Days, 14 Hours, 30 Minutes)"},
			{"15:00","PM","3/15/2019", "7:00","AM","3/16/2019", "$ 12.00", "(0 Days, 4 Hours, 0 Minutes)"},
			{"00:00","AM","3/1/2019", "00:00","AM","3/30/2019", "$ 882.00", "(29 Days, 0 Hours, 0 Minutes)"}
		};
	}
	
    @Test(dataProvider = "Times")
    public void test(String sTime,String sradioVal, String sDate,String eTime,String eradioVal, String eDate, String cost, String calcTime) throws InterruptedException {
    	System.setProperty("webdriver.chrome.driver","/media/sef/Archive/ADMINISTRATOR/JAVA/USM_2018/Automation 2019/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.get("http://adam.goucher.ca/parkcalc/index.php");
          
        Assert.assertEquals(driver.getTitle(), "Parking Calculator");
        
        Select chooseALotSelect = new Select(driver.findElement(By.xpath("//tr[td[contains(text(),'Choose a Lot')]]//select[@id='Lot']")));
        chooseALotSelect.selectByVisibleText("Valet Parking");

        WebElement startTime = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryTime']"));
        List<WebElement> startDateAmPm = driver.findElements(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryTimeAMPM']"));
        WebElement startDate = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Entry Date and Time')]]//input[@name='EntryDate']"));

        startTime.clear();
        startTime.sendKeys(sTime);
        selectRadioValue(startDateAmPm, sradioVal);
        startDate.clear();
        startDate.sendKeys(sDate);

        WebElement endTime = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitTime']"));
        List<WebElement> endDateAmPm = driver.findElements(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitTimeAMPM']"));
        WebElement endDate = driver.findElement(By.xpath("//tr[td[contains(text(),'Choose Leaving Date and Time')]]//input[@name='ExitDate']"));

        endTime.clear();
        endTime.sendKeys(eTime);
        selectRadioValue(endDateAmPm, eradioVal);
        endDate.clear();
        endDate.sendKeys(eDate);

        WebElement submitBtn = driver.findElement(By.xpath("//input[@name='Submit' and @value='Calculate']"));
        submitBtn.click();
        Thread.sleep(1000);

        WebElement costValue = driver.findElement(By.xpath("//tr[contains(.,'COST')]/td//b[contains(text(),'$')]"));
        WebElement calculatedTime = driver.findElement(By.xpath("//tr[contains(.,'COST')]/td//b[contains(text(),'Days')]"));
        
        Assert.assertEquals(costValue.getText(),cost);
        Assert.assertEquals(calculatedTime.getText().trim(), calcTime);
        
        driver.quit();
    }

    public void selectRadioValue(List<WebElement> list, String selectValue){
        for(WebElement elem:list){
            String paramValue = elem.getAttribute("value");
            if (StringUtils.equals(selectValue,paramValue)){
                elem.click();
                return;
            }
        }
    }
}