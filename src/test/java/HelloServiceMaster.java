import com.saucelabs.saucerest.SauceREST;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloServiceMaster
{
	String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
	String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

	RemoteWebDriver driver;
	String sessionId;
	WebDriverWait wait;

	@BeforeMethod
	public void setup(Method method) throws MalformedURLException
	{
		URL url = new URL("https://" + SAUCE_USERNAME + ":" + SAUCE_ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub");

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platform", "macOS 10.14");
		capabilities.setCapability("browserName", "Chrome");
		capabilities.setCapability("version", "latest");
		capabilities.setCapability("screenResolution", "1152x864");
		capabilities.setCapability("name", method.getName());
		capabilities.setCapability("build", "ServiceMaster Demo");

		driver = new RemoteWebDriver(url, capabilities);
		sessionId = driver.getSessionId().toString();
		wait = new WebDriverWait(driver, 10);
	}

	@Test
	public void titleTest()
	{
		driver.get("http://servicemaster.com/");
		String title = driver.getTitle();

		assertThat(title).contains("ServiceMaster");
	}

	@Test
	public void headingTest()
	{
		driver.get("http://servicemaster.com/");
		String heading = driver.findElementByTagName("h1").getText();

		assertThat(heading).contains("Delivering ");
	}

	@Test
	public void companyLinkTest()
	{
		// open home page
		driver.get("http://servicemaster.com/");

		// get heading
		String heading = driver.findElementByTagName("h1").getText();

		// check that heading matches home page
		assertThat(heading).contains("Delivering world-class");

		// click on Company link
		WebElement element = driver.findElement(By.linkText("Company"));
		element.click();

		// wait for new page to load
		wait.until(ExpectedConditions.titleContains("Company"));

		// get heading again
		heading = driver.findElementByTagName("h1").getText();

		// check that heading matches company page
		assertThat(heading).contains("Service with a Purpose");
	}

	@Test
	public void newsroomTest() throws InterruptedException
	{
		// open home page
		driver.get("http://servicemaster.com/");

		// click on newsroom link
		WebElement element = driver.findElement(By.linkText("News Room"));
		element.click();

		// switch to new tab
		String currentWindow = driver.getWindowHandle();
		for (String windowHandle : driver.getWindowHandles())
		{
			System.out.println(windowHandle);
			System.out.println(driver.getTitle());

			if (windowHandle != currentWindow)
			{
				System.out.println("switching to window: " + windowHandle);
				driver.switchTo().window(windowHandle);
				System.out.println(driver.getTitle());
			}
		}

		// wait for page to load
		Boolean newsroom = wait.until(ExpectedConditions.titleContains("Newsroom"));

		// check that we're on the newsroom page
		if (newsroom)
		{
			String content = driver.findElementByTagName("body").getText();
			assertThat(content).contains("Newsroom Home");
		}
	}

	@AfterMethod
	public void cleanup(ITestResult result)
	{
		driver.quit();

		SauceREST api = new SauceREST(SAUCE_USERNAME, SAUCE_ACCESS_KEY);

		if (result.getStatus() == ITestResult.SUCCESS)
		{
			api.jobPassed(sessionId);
		}
		else
		{
			api.jobFailed(sessionId);
		}
	}
}
