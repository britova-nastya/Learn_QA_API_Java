import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HelloWorldTest {
    @Test
    public void testParse() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        System.out.println(response.prettyPrint());
        String secondMessage = response.getString("messages[1].message");

        System.out.println("The second message is: " + secondMessage);

    }

    private static JsonPath getResponse(JsonPath response) {
        return response;
    }

    @Test
    public void testLocationRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        System.out.println(response.getHeader("Location"));
    }


    @Test
    public void testLocationLongRedirect() {
        String url = "https://playground.learnqa.ru/api/long_redirect";
        int statusCode = 0;
        int redirectCount = 0;

        while (statusCode != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .get(url)
                    .andReturn();

            statusCode = response.statusCode();
            System.out.println(statusCode);
            if (statusCode == 301) {
                url = response.getHeader("Location");
                redirectCount++;
           }
        }
        System.out.println("Final URL: " + url);
        System.out.println("Total redirects: " + redirectCount);
    }

    @Test
    public void testJob() throws InterruptedException {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String token = response.getString("token");
        int seconds = response.getInt("seconds");
        JsonPath earlyResponse  = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String earlyStatus = earlyResponse.getString("status");

        assertEquals("Job is NOT ready", earlyStatus, "Статус должен быть 'Job is NOT ready'");

        Thread.sleep((seconds + 1) * 1000);

        JsonPath finalResponse = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String finalStatus = finalResponse.getString("status");
        String result = finalResponse.getString("result");

        assertEquals("Job is ready", finalStatus, "Status should be 'Job is ready' after the job is completed");
        assertNotNull(result, "Результат должен быть не пустым");

    }

    @Test
    public void testPassword(){
        List<String> passwords = Arrays.asList(
                "password", "123456", "12345678", "qwerty", "abc123", "monkey", "letmein", "dragon",
                "111111", "baseball", "iloveyou", "trustno1", "1234567", "sunshine", "master", "123123",
                "welcome", "shadow", "ashley", "football", "jesus", "ninja", "mustang", "password1",
                "123456789", "12345", "1234567890", "admin", "princess", "solo", "1qaz2wsx", "abc123",
                "login", "admin", "starwars", "123123", "1234567890", "letmein", "monkey", "welcome",
                "login", "admin", "sunshine", "master", "hottie", "loveme", "zaq1zaq1", "password1",
                "123qwe", "qwerty123", "1q2w3e4r", "admin", "qazwsx", "1234", "photoshop[a]", "111111",
                "1qaz2wsx", "admin", "abc123", "1234", "123456", "passw0rd", "passw0rd", "654321",
                "print", "123456789", "12345678", "12345", "iloveyou", "111111", "123123", "abc123",
                "qwerty123", "1q2w3e4r", "admin", "654321", "555555", "lovely", "7777777", "welcome",
                "888888", "princess", "dragon", "password1", "123qwe");

        System.out.println(password(passwords));
    }
    public static String password( List<String> passwords) {
        // Первоначальный список паролей из таблицы, объединенный из всех лет без учета повторений

        // Удаление дубликатов
        Set<String> uniquePasswordsSet = new HashSet<>(passwords);
        String login = "super_admin";
        for (String password : uniquePasswordsSet) {
            // Формирование JSON строки
            String jsonBody = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", login, password);

            Response response = RestAssured
                    .given()
                    .contentType("application/json") // Установка заголовка Content-Type в application/json
                    .body(jsonBody) // Передача JSON строки в тело запроса
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework");

            Cookies cookies = response.detailedCookies();
            if (cookies.hasCookieWithName("auth_cookie")) {
                String authCookieValue = cookies.getValue("auth_cookie");
                Response checkResponse = RestAssured
                        .given()
                        .cookie("auth_cookie", authCookieValue)
                        .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie");

                if (checkResponse.asString().contains("You are authorized")) {
                    return password;
                }
            }
        }
        return "Password not found.";
        }
    }


