import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CookieTest {
    @Test
    public void testCookieValue() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Integer statusCode = response.statusCode();
        Map<String,String> cookies = response.getCookies();
        System.out.println(cookies);
        assertEquals(200, statusCode, "Статус код не равен 200");

        // Ассерт для значения куки с именем "HomeWork"
        String expectedCookieValue = "hw_value";
        String actualCookieValue = cookies.get("HomeWork");
        assertEquals(expectedCookieValue, actualCookieValue, "Значение куки не соответствует ожидаемому");

    }
}
