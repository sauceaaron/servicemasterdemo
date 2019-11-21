import io.appium.java_client.MobileElement;

public interface CalculatorDriver
{
//	public enum Key {
//		one, two, three, four, five, six, seven, eight, nine, zero,
//		add, subtract, multiply, divide, equals, clear
//	}
	
	MobileElement getKey(String key);
	void pressKey(String key);
	
	MobileElement getScreen();
	String readScreen();
	
	void clear();
}
