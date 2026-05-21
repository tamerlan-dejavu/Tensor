import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TensorCipherTest {

    private final TensorCipher cipher = new TensorCipher();

    @Test
    void testEncryptDecryptRoundTrip() {
        String original = "HELLO WORLD";
        String key = "SECRET";
        String encrypted = cipher.encrypt(original, key);
        String decrypted = cipher.decrypt(encrypted, key);
        assertEquals(original, decrypted);
    }

    @Test
    void testEncryptDecryptLongText() {
        String original = "THIS IS A LONG TEXT THAT EXCEEDS SIXTY FOUR CHARACTERS AND SPANS MULTIPLE TENSOR BLOCKS";
        String key = "LONGKEY";
        String decrypted = cipher.decrypt(cipher.encrypt(original, key), key);
        assertEquals(original, decrypted);
    }

    @Test
    void testCaseFolding() {
        String key = "SECRET";
        String encLower = cipher.encrypt("hello", key);
        String encUpper = cipher.encrypt("HELLO", key);
        assertEquals(encLower, encUpper);
    }

    @Test
    void testDifferentKeysDifferentOutput() {
        String plaintext = "HELLO WORLD";
        String enc1 = cipher.encrypt(plaintext, "KEY1");
        String enc2 = cipher.encrypt(plaintext, "KEY2");
        assertNotEquals(enc1, enc2);
    }

    @Test
    void testEmptyString() {
        assertDoesNotThrow(() -> {
            String encrypted = cipher.encrypt("", "SECRET");
            cipher.decrypt(encrypted, "SECRET");
        });
    }

    @Test
    void testSpecialChars() {
        String original = "HELLO 123 !@#";
        String key = "SECRET";
        String decrypted = cipher.decrypt(cipher.encrypt(original, key), key);
        assertEquals(original, decrypted);
    }
}
