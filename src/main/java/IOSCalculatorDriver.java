import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;

import java.util.HashMap;

public class IOSCalculatorDriver implements CalculatorDriver
{
	IOSDriver<MobileElement> driver;
	
	public IOSCalculatorDriver(IOSDriver<MobileElement> driver)
	{
		this.driver = driver;
	}
	
	public static By one = By.id("1");
	public static By two = By.id("2");
	public static By three = By.id("3");
	public static By four = By.id("4");
	public static By five = By.id("5");
	public static By six = By.id("6");
	public static By seven = By.id("7");
	public static By eight = By.id("8");
	public static By nine = By.id("9");
	public static By zero = By.id("0");
	public static By add = By.id("+");
	public static By subtract = By.id("-");
	public static By multiply = By.id("×");
	public static By divide = By.id("÷");
	public static By equals = By.id("=");
	public static By screen = By.xpath("//XCUIElementTypeStaticText[1]");
	public static By clear = By.id("C");
	
	public static HashMap<String, By> keypad = new HashMap<String, By>() {{
		put("1", one);
		put("2", two);
		put("3", three);
		put("4", four);
		put("5", five);
		put("6", six);
		put("7", seven);
		put("8", eight);
		put("9", nine);
		put("0", zero);
		put("+", add);
		put("-", subtract);
		put("*", multiply);
		put("/", divide);
		put("=", equals);
		put("C", clear);
	}};
	
	public MobileElement getKey(String key)
	{
		return driver.findElement(keypad.get(key));
	}
	
	public void pressKey(String key)
	{
		getKey(key).click();
	}
	
	public MobileElement getScreen()
	{
		return driver.findElement(screen);
	}
	
	public String readScreen()
	{
		return getScreen().getText();
	}
	
	public void clear()
	{
		getKey("C").click();
	}
}
