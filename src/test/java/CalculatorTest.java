import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.junit.After;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.ITestResult.SUCCESS;

public class CalculatorTest
{
	public String testobject_datacenter_us = "https://us1.appium.testobject.com/wd/hub";
	public String testobject_username = System.getenv("TESTOBJECT_USERNAME");
	public String testobject_api_key = System.getenv("TESTOBJECT_API_KEY");

	IOSDriver<MobileElement> driver;
	@BeforeMethod
	public void setup(Method method) throws MalformedURLException
	{
		URL url = new URL(testobject_datacenter_us);

		String testName =  method.getName();
		String suiteName = this.getClass().getSimpleName();

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("testobject_api_key", testobject_api_key);
		capabilities.setCapability("testobject_test_name", testName);
		capabilities.setCapability("testobject_suite_name", suiteName);
		capabilities.setCapability("platformName", "iOS");
//		capabilities.setCapability("platformVersion", "10.2");
//		capabilities.setCapability("deviceName", "iPhone SE");

		System.out.println(url);
		System.out.println(capabilities);

		driver = new IOSDriver<MobileElement>(url, capabilities);

		driver.getCapabilities().asMap().forEach((key, value) -> {
			System.out.println(key + ":" + value);
		});
	}

	@Test
	public void passingTest()
	{
		assertThat(true).isTrue();
	}

	@Test
	public void failingTest()
	{
		assertThat(true).isFalse();
	}

	@Test
	public void add_two_numbers()
	{
		IOSCalculatorDriver calculator = new IOSCalculatorDriver(driver);

		calculator.pressKey("1");
		calculator.pressKey("+");
		calculator.pressKey("2");
		calculator.pressKey("=");

		String output = calculator.readScreen();
		System.out.println("CALCULATOR GOT VALUE: " + output);
		assertThat(output).isEqualTo("3.0");
	}

	@AfterMethod
	public void cleanup(ITestResult result) throws InterruptedException
	{
		String sessionId = driver.getSessionId().toString();
		driver.quit();

		Thread.sleep(15000);

		boolean status = result.getStatus() == SUCCESS;
		TestObjectAPI api = new TestObjectAPI(testobject_username, testobject_api_key);
		api.updateTestStatus(sessionId, status);
	}
}
