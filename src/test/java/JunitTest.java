import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JunitTest {
    @ParameterizedTest
    @ValueSource(strings = { "Short", "Longer", "VeryLongString" })
    public void testStringLength(String text) {

        assertTrue(text.length() > 15, "Длина строки должна быть больше 15 символов");
    }
}
