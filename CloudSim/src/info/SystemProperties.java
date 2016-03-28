package info;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemProperties {
	
	private Properties properties;
	private static SystemProperties instance;
	
	private SystemProperties() {
		properties = new Properties(); 
	}
	
	public static SystemProperties getInstance() {
		if (instance == null) {
			instance = new SystemProperties();
		}
		return instance;
	}
	
	public String getPropertyValue(String property) {
		return properties.getProperty(property);
	}
	
	public void load(InputStream stream) throws IOException {
		properties.load(stream);
	}

}
