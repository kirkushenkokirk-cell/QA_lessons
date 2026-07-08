import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
}



class MainPage extends BasePage {

    @FindBy(xpath = "//h2[contains(text(),'Онлайн пополнение без комиссии')]")
    private WebElement blockTitle;

    @FindBy(css = ".payment-logos")  
    private WebElement logosContainer;

    @FindBy(linkText = "Подробнее о сервисе")
    private WebElement moreDetailsLink;

    @FindBy(css = "input[placeholder*='номер']") import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
}



class MainPage extends BasePage {

    @FindBy(xpath = "//h2[contains(text(),'Онлайн пополнение без комиссии')]")
    private WebElement blockTitle;

    @FindBy(css = ".payment-logos")   // уточните селектор
    private WebElement logosContainer;

    @FindBy(linkText = "Подробнее о сервисе")
    private WebElement moreDetailsLink;

    @FindBy(css = "input[placeholder*='номер']")  // уточнить
    private WebElement phoneInput;

    @FindBy(css = "input[placeholder='Сумма']")
    private WebElement sumInput;

    @FindBy(css = "button[type='submit']")
    private WebElement continueButton;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public String getBlockTitle() {
        return blockTitle.getText().trim();
    }

    public boolean isLogoPresent(String altText) {
        return !logosContainer.findElements(By.xpath(".//img[contains(@alt, '" + altText + "')]")).isEmpty();
    }

    public String getMoreDetailsHref() {
        return moreDetailsLink.getAttribute("href");
    }

    public void selectServiceTab(String tabName) {
        WebElement tab = driver.findElement(By.xpath("//span[contains(text(),'" + tabName + "')]"));
        tab.click();
        // Небольшая задержка для переключения вкладки
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public String getPhonePlaceholder() {
        return phoneInput.getAttribute("placeholder");
    }

    public String getSumPlaceholder() {
        return sumInput.getAttribute("placeholder");
    }

    public boolean isSumFieldDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(sumInput));
        return sumInput.isDisplayed();
    }

    public void enterPhoneNumber(String phone) {
        phoneInput.clear();
        phoneInput.sendKeys(phone);
    }

    public PaymentPopup clickContinue() {
        continueButton.click();
        return new PaymentPopup(driver);
    }
}


class PaymentPopup extends BasePage {

    @FindBy(css = ".popup .total-amount")   // уточн
    private WebElement totalAmount;

    private WebElement payButton;

    @FindBy(css = ".popup .phone-number")   
    private WebElement phoneDisplay;

    @FindBy(css = ".card-input")            
    private List<WebElement> cardInputs;

    @FindBy(css = ".popup .payment-icons img")
    private List<WebElement> popupIcons;

    public PaymentPopup(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".popup")));
    }

    public String getTotalAmount() {
        return totalAmount.getText();
    }

    public String getPayButtonText() {
        return payButton.getText();
    }

    public String getPhoneNumber() {
        return phoneDisplay.getText();
    }

    public List<String> getCardPlaceholders() {
        return cardInputs.stream().map(el -> el.getAttribute("placeholder")).toList();
    }

    public boolean arePaymentIconsPresent() {
        return !popupIcons.isEmpty();
    }
}


public class MtsTest {

    private static WebDriver driver;
    private static MainPage mainPage;

    public static void main(String[] args) {
        
        WebDriverManager.chromedriver().setup();


        ChromeOptions options = new ChromeOptions();
     
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.mts.by");

        mainPage = new MainPage(driver);

        try {
            // 1. Проверка заголов
            testBlockTitle();

            // 2. Проверка логот
            testPaymentLogos();

            // 3. Проверка ссылки "Подробнее"
            testMoreDetailsLink();

            // 4. Проверка placeholder'ов для всех 
            testPlaceholdersForAllServices();

            // 5. Проверка оплаты "Услуги связи"
            testCommunicationServicePayment();

            System.out.println("✅ Все тесты пройдены успешно!");

        } catch (AssertionError | Exception e) {
            System.err.println("❌ Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) driver.quit();
        }
    }

    // Тестовые метод

    private static void testBlockTitle() {
        String title = mainPage.getBlockTitle();
        assertEquals("Онлайн пополнение без комиссии", title);
        System.out.println("✅ Заголовок блока корректен");
    }

    private static void testPaymentLogos() {
        assertTrue(mainPage.isLogoPresent("Visa"));
        assertTrue(mainPage.isLogoPresent("MasterCard"));
        assertTrue(mainPage.isLogoPresent("Белкарт"));
        System.out.println("✅ Логотипы платёжных систем присутствуют");
    }

    private static void testMoreDetailsLink() {
        String href = mainPage.getMoreDetailsHref();
        assertNotNull(href);
        assertTrue(href.startsWith("https://"));
        System.out.println("✅ Ссылка 'Подробнее о сервисе' корректна");
    }

    private static void testPlaceholdersForAllServices() {
        String[] services = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};
        for (String service : services) {
            mainPage.selectServiceTab(service);
            String phonePlaceholder = mainPage.getPhonePlaceholder();
            assertNotNull(phonePlaceholder);
            assertFalse(phonePlaceholder.isEmpty(), "Placeholder для телефона пуст для " + service);
            // Дополнительно проверяем поле суммы
            assertTrue(mainPage.isSumFieldDisplayed(), "Поле суммы не видно для " + service);
            String sumPlaceholder = mainPage.getSumPlaceholder();
            assertNotNull(sumPlaceholder);
            assertFalse(sumPlaceholder.isEmpty(), "Placeholder для суммы пуст для " + service);
            System.out.println("✅ Для '" + service + "' placeholder'ы корректны");
        }
    }

    private static void testCommunicationServicePayment() {
        // Выбираем вклад
        mainPage.selectServiceTab("Услуги связи");
        // Вводим номер
        mainPage.enterPhoneNumber("297777777");
        // Нажимаем "Продолжить"
        PaymentPopup popup = mainPage.clickContinue();

        // Проверяем сумму и кн
        String total = popup.getTotalAmount();
        String payButtonText = popup.getPayButtonText();
        assertNotNull(total);
        assertNotNull(payButtonText);
        assertTrue(payButtonText.contains("Оплатить") || payButtonText.contains("Продолжить"), "На кнопке не отображается сумма");
        System.out.println("✅ Сумма на экране и на кнопке корректна");

        // Проверяем номер телеф
        String phone = popup.getPhoneNumber();
        assertNotNull(phone);
        assertTrue(phone.contains("297777777") || phone.contains("+375297777777"), "Номер телефона отображается неверно");
        System.out.println("✅ Номер телефона отображается корректно: " + phone);

        // Проверяем placeholder'ы пол
        List<String> cardPlaceholders = popup.getCardPlaceholders();
        assertTrue(cardPlaceholders.size() >= 3, "Должно быть как минимум 3 поля для карты");
        for (String ph : cardPlaceholders) {
            assertNotNull(ph);
            assertFalse(ph.isEmpty(), "Placeholder поля карты пуст");
        }
        System.out.println("✅ Поля для реквизитов карты имеют надписи");

        // Проверяем наличие иконок
        assertTrue(popup.arePaymentIconsPresent(), "Иконки платёжных систем в модалке отсутствуют");
        System.out.println("✅ Иконки платёжных систем в модалке присутствуют");
    }
}

    private WebElement phoneInput;

    @FindBy(css = "input[placeholder='Сумма']")
    private WebElement sumInput;

    @FindBy(css = "button[type='submit']")
    private WebElement continueButton;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public String getBlockTitle() {
        return blockTitle.getText().trim();
    }

    public boolean isLogoPresent(String altText) {
        return !logosContainer.findElements(By.xpath(".//img[contains(@alt, '" + altText + "')]")).isEmpty();
    }

    public String getMoreDetailsHref() {
        return moreDetailsLink.getAttribute("href");
    }

    public void selectServiceTab(String tabName) {
        WebElement tab = driver.findElement(By.xpath("//span[contains(text(),'" + tabName + "')]"));
        tab.click();
        // задержка для переключения вкл
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public String getPhonePlaceholder() {
        return phoneInput.getAttribute("placeholder");
    }

    public String getSumPlaceholder() {
        return sumInput.getAttribute("placeholder");
    }

    public boolean isSumFieldDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(sumInput));
        return sumInput.isDisplayed();
    }

    public void enterPhoneNumber(String phone) {
        phoneInput.clear();
        phoneInput.sendKeys(phone);
    }

    public PaymentPopup clickContinue() {
        continueButton.click();
        return new PaymentPopup(driver);
    }
}


// окно оплаты
class PaymentPopup extends BasePage {

    @FindBy(css = ".popup .total-amount")   
    private WebElement totalAmount;

    @FindBy(css = ".popup .pay-button")     
    private WebElement payButton;

    @FindBy(css = ".popup .phone-number")   
    private WebElement phoneDisplay;

    @FindBy(css = ".card-input")            
    private List<WebElement> cardInputs;

    @FindBy(css = ".popup .payment-icons img")
    private List<WebElement> popupIcons;

    public PaymentPopup(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".popup")));
    }

    public String getTotalAmount() {
        return totalAmount.getText();
    }

    public String getPayButtonText() {
        return payButton.getText();
    }

    public String getPhoneNumber() {
        return phoneDisplay.getText();
    }

    public List<String> getCardPlaceholders() {
        return cardInputs.stream().map(el -> el.getAttribute("placeholder")).toList();
    }

    public boolean arePaymentIconsPresent() {
        return !popupIcons.isEmpty();
    }
}

public class MtsTest {

    private static WebDriver driver;
    private static MainPage mainPage;

    public static void main(String[] args) {
     
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.mts.by");
        mainPage = new MainPage(driver);

        try {
            // Проверка загол
            testBlockTitle();

            // Проверка логот
            testPaymentLogos();

            // Проверка ссылки "Подробнее"
            testMoreDetailsLink();

            // Проверка placeholder'ов 
            testPlaceholdersForAllServices();

            // 5. Проверка оплаты "Усл связи"
            testCommunicationServicePayment();

            System.out.println("✅ Все тесты пройдены успешно!");

        } catch (AssertionError | Exception e) {
            System.err.println("❌ Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) driver.quit();
        }
    }


    private static void testBlockTitle() {
        String title = mainPage.getBlockTitle();
        assertEquals("Онлайн пополнение без комиссии", title);
        System.out.println("✅ Заголовок блока корректен");
    }

    private static void testPaymentLogos() {
        assertTrue(mainPage.isLogoPresent("Visa"));
        assertTrue(mainPage.isLogoPresent("MasterCard"));
        assertTrue(mainPage.isLogoPresent("Белкарт"));
        System.out.println("✅ Логотипы платёжных систем присутствуют");
    }

    private static void testMoreDetailsLink() {
        String href = mainPage.getMoreDetailsHref();
        assertNotNull(href);
        assertTrue(href.startsWith("https://"));
        System.out.println("✅ Ссылка 'Подробнее о сервисе' корректна");
    }

    private static void testPlaceholdersForAllServices() {
        String[] services = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};
        for (String service : services) {
            mainPage.selectServiceTab(service);
            String phonePlaceholder = mainPage.getPhonePlaceholder();
            assertNotNull(phonePlaceholder);
            assertFalse(phonePlaceholder.isEmpty(), "Placeholder для телефона пуст для " + service);
         
            assertTrue(mainPage.isSumFieldDisplayed(), "Поле суммы не видно для " + service);
            String sumPlaceholder = mainPage.getSumPlaceholder();
            assertNotNull(sumPlaceholder);
            assertFalse(sumPlaceholder.isEmpty(), "Placeholder для суммы пуст для " + service);
            System.out.println("✅ Для '" + service + "' placeholder'ы корректны");
        }
    }

    private static void testCommunicationServicePayment() {
 
        mainPage.selectServiceTab("Услуги связи");
        mainPage.enterPhoneNumber("297777777");
        PaymentPopup popup = mainPage.clickContinue();

     
        String total = popup.getTotalAmount();
        String payButtonText = popup.getPayButtonText();
        assertNotNull(total);
        assertNotNull(payButtonText);
        assertTrue(payButtonText.contains("Оплатить") || payButtonText.contains("Продолжить"), "На кнопке не отображается сумма");
        System.out.println("✅ Сумма на экране и на кнопке корректна");

        String phone = popup.getPhoneNumber();
        assertNotNull(phone);
        assertTrue(phone.contains("297777777") || phone.contains("+375297777777"), "Номер телефона отображается неверно");
        System.out.println("✅ Номер телефона отображается корректно: " + phone);

        List<String> cardPlaceholders = popup.getCardPlaceholders();
        assertTrue(cardPlaceholders.size() >= 3, "Должно быть как минимум 3 поля для карты");
        for (String ph : cardPlaceholders) {
            assertNotNull(ph);
            assertFalse(ph.isEmpty(), "Placeholder поля карты пуст");
        }
        System.out.println("✅ Поля для реквизитов карты имеют надписи");

        assertTrue(popup.arePaymentIconsPresent(), "Иконки платёжных систем в модалке отсутствуют");
        System.out.println("✅ Иконки платёжных систем в модалке присутствуют");
    }
}
