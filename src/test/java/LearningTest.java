import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LearningTest {
    @Test
    public void testCookieValue() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Integer statusCode = response.statusCode();
        Map<String, String> cookies = response.getCookies();
        System.out.println(cookies);
        assertEquals(200, statusCode, "Статус код не равен 200");

        // Ассерт для значения куки с именем "HomeWork"
        String expectedCookieValue = "hw_value";
        String actualCookieValue = cookies.get("HomeWork");
        assertEquals(expectedCookieValue, actualCookieValue, "Значение куки не соответствует ожидаемому");
    }

    @Test
    public void testHeaderValue() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        Integer statusCode = response.statusCode();

        assertEquals(200, statusCode, "Статус код не равен 200");

        Headers headers = response.headers();
        for (Header header : headers) {
            System.out.println(header.getName() + ": " + header.getValue());

        }

        String expectedHeaderValue = "Some secret value";
        String actualHeaderValue = response.getHeader("x-secret-homework-header");
        assertEquals(expectedHeaderValue, actualHeaderValue, "Значение заголовка не соответствует ожидаемому");

    }
}
