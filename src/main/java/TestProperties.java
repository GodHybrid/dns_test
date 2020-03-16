import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestProperties
{
    private Properties settingsProps = new Properties();
    private static TestProperties _INSTANCE = null;

    private TestProperties()
    {
        try
        {
            settingsProps.load(new FileInputStream(new File("src/main/resources/environment_" + System.getProperty("browser", "chrome") + ".properties")));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static TestProperties getInstance()
    {
        if(_INSTANCE == null)
        {
            _INSTANCE = new TestProperties();
        }
        return _INSTANCE;
    }

    public String getProperty(String key, String defaultValue)
    {
        return settingsProps.getProperty(key, defaultValue);
    }

    public String getProperty(String key)
    {
        return settingsProps.getProperty(key);
    }
}
