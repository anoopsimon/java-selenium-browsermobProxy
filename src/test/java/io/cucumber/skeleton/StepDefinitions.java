package io.cucumber.skeleton;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.NoSuchContextException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import io.cucumber.java.en.Given;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class StepDefinitions {
    @Given("I Capture Network Calls")
    public void I_have_cukes_in_my_belly() throws IOException, InterruptedException 
    {
        System.out.println("Capturing Network Calls");
        	// start the proxy
	    BrowserMobProxy proxy = new BrowserMobProxyServer();
	    proxy.setTrustAllServers(true);
	    proxy.start(0);
	    
	    // get the Selenium proxy object
	    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        System.out.println("proxy: " + seleniumProxy.toJson());
        String exe = System.getProperty("user.dir")+"/exe/chromedriver.exe";
	    
		System.setProperty("webdriver.chrome.driver", "C:/selenoid/chromedriver.exe");
		
		ChromeOptions  options = new ChromeOptions();
	    options.setCapability(CapabilityType.PROXY, seleniumProxy);
        //options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
	    options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
	    options.addArguments("--ignore-certificate-errors");
	    //options.addArguments("--user-data-dir=C:\\Users\\s451421\\Downloads\\chrome");

	    WebDriver driver = new ChromeDriver(options);
	    
	    // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
	    proxy.enableHarCaptureTypes(
	    		CaptureType.REQUEST_CONTENT, CaptureType.REQUEST_HEADERS, CaptureType.REQUEST_BINARY_CONTENT,
	    		CaptureType.RESPONSE_CONTENT, CaptureType.RESPONSE_HEADERS, CaptureType.RESPONSE_CONTENT);

        // create a new HAR 
        proxy.newHar("proxy.com");

	    // open yahoo.com
        driver.get("https://google.com");

        Thread.sleep(60000);
        
        Har har = proxy.getHar();
	    	har.writeTo(new File("C:/Temp/eclipse.har"));
	    	proxy.stop();
            driver.quit();           
      
    }
}

