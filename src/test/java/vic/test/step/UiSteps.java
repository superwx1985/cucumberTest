package vic.test.step;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class UiSteps {

    static ChromeDriver driver = null;

    @Before
    public void setUp() {
//        System.setProperty("webdriver.chrome.driver", "E:\\code\\selenium driver\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        options.setBinary("E:\\code\\selenium driver\\chrome-win64\\chrome.exe");
//        driver = new ChromeDriver(options);
    }

    @When("I open {string} page")
    public void iOpenPage(String string) throws InterruptedException {
        driver.get("https://www.baidu.com/");
        Thread.sleep(2000);
    }

    @Then("I can see the {string} in the page")
    public void iCanSeeTheInThePage(String str) {
//        String text = driver.findElement(By.xpath(
//                "//div[@id='bottom_layer']/div[@class='s-bottom-layer-content']/p[1]/a"))
//                .getText();
//        Assert.assertEquals(string, text);
//        List<WebElement> els = driver.findElements(By.xpath(String.format("/html/body//*[text()='%s']", str)));
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String xpath = String.format("/html/body//*[contains(translate(text(),'%s','%s'), translate('%s','%s','%s'))]", uppercase, lowercase, str, uppercase, lowercase);
        List<WebElement> els = driver.findElements(By.xpath(xpath));
        Assert.assertFalse(els.isEmpty());
    }

    @After("@browser")
    public void tearDown() {
        driver.quit();
    }

    @Given("I open the browser")
    public void iOpenTheBrowser() {
        System.setProperty("webdriver.chrome.driver", "E:\\code\\selenium driver\\chromedriver.exe"); //指定driver位置
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*"); //允许selenium远程控制浏览器
        options.setBinary("E:\\code\\selenium driver\\chrome-win64\\chrome.exe"); //指定chrome位置
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); //设置隐式等待时间
    }

    @Then("I close the browser")
    public void iCloseTheBrowser() {
        driver.quit();
    }

    @When("I search {string}")
    public void iSearch(String str) throws InterruptedException {
        driver.findElement(By.id("kw")).sendKeys(str);
        driver.findElement(By.id("su")).click();
//        Thread.sleep(2000);
    }

}
