import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class MtsOnlinePaymentTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void init() {
       
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Users\\User\\Desktop\\Новая папка\\chrome-win64\\chromedriver.exe");


        WebDriverManager.chromedriver().setup();

        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.mts.by");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    void testBlockTitle() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Онлайн пополнение без комиссии')]")
        ));
        assertNotNull(title);
        assertEquals("Онлайн пополнение без комиссии", title.getText().trim());
    }

    @Test
    void testPaymentLogosPresence() {
        String[] expectedLogos = {"Visa", "MasterCard", "Белкарт"};
        WebElement logosContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'payment-logos')]")
        ));
        for (String logoName : expectedLogos) {
            boolean found = logosContainer.findElements(
                    By.xpath(".//img[contains(@alt, '" + logoName + "')]")
            ).size() > 0;
            assertTrue(found, "Логотип " + logoName + " не найден");
        }
    }

    @Test
    void testMoreDetailsLink() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(text(),'Подробнее о сервисе')]")
        ));
        assertNotNull(link);
        String href = link.getAttribute("href");
        assertNotNull(href);
        assertTrue(href.startsWith("https://"));
    }

    @Test
    void testContinueButtonForCommunicationService() {
        WebElement serviceTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(text(),'Услуги связи')]")
        ));
        serviceTab.click();

        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Номер телефона']")
        ));
        phoneInput.clear();
        phoneInput.sendKeys("297777777");

        WebElement continueBtn = driver.findElement(
                By.xpath("//button[contains(text(),'Продолжить')]")
        );
        continueBtn.click();

        WebElement sumInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Сумма']")
        ));
        assertNotNull(sumInput);
    }
}
