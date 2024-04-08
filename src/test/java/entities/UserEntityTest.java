package entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserEntityTest {

    @Test
    public void whenCreateUserWithEmptyConstructorThenAllFieldsAreNull() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getSurname());
        assertNull(user.getAge());
        assertNull(user.getAddress());
        assertNull(user.getBankAccounts());
    }

    @Test
    public void whenCreateUserWithFullConstructorThenAllFieldsAreNotNull() {
        Address address = new Address(
                456L,
                "Россия",
                "Москва",
                "Пушкина",
                123
        );
        List<BankAccount> bankAccounts = Arrays.asList(
                new BankAccount(789L, "Счет1", 12345),
                new BankAccount(654L, "Счет2", 1234567)
        );
        User user = new User(
                123L,
                "Вася",
                "Пупкин",
                22,
                new Address(
                        456L,
                        "Россия",
                        "Москва",
                        "Пушкина",
                        123
                ),
                Arrays.asList(
                        new BankAccount(789L, "Счет1", 12345),
                        new BankAccount(654L, "Счет2", 1234567)
                )
        );

        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertNotNull(user.getSurname());
        assertNotNull(user.getAge());
        assertNotNull(user.getAddress());
        assertNotNull(user.getBankAccounts());

        assertEquals(Long.valueOf(123L), user.getId());
        assertEquals("Вася", user.getName());
        assertEquals("Пупкин", user.getSurname());
        assertEquals(Integer.valueOf(22), user.getAge());
        assertEquals(address, user.getAddress());
        assertEquals(bankAccounts, user.getBankAccounts());
    }

    @Test
    public void userObjectCanSerializeToJSONByJacksonObjectMapper() {
        User user = new User(
                123L,
                "Вася",
                "Пупкин",
                22,
                new Address(
                        456L,
                        "Россия",
                        "Москва",
                        "Пушкина",
                        123
                ),
                Arrays.asList(
                        new BankAccount(789L, "Счет1", 12345),
                        new BankAccount(654L, "Счет2", 1234567)
                )
        );

        ObjectMapper mapper = new ObjectMapper();
        String expectedJSON = "{" +
                "\"id\":123," +
                "\"name\":\"Вася\"," +
                "\"surname\":\"Пупкин\"," +
                "\"age\":22," +
                "\"address\":{" +
                "\"id\":456," +
                "\"country\":\"Россия\"," +
                "\"city\":\"Москва\"," +
                "\"num\":123," +
                "\"street\":\"Пушкина\"}," +
                "\"bankAccounts\":[" +
                "{\"id\":789," +
                "\"accountName\":\"Счет1\"," +
                "\"cash\":12345},{" +
                "\"id\":654," +
                "\"accountName\":\"Счет2\"," +
                "\"cash\":1234567}]}";
        try {
            assertEquals(expectedJSON, mapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void userJSONCanBeDeserializedByJacksonObjectMapperToUserObject() {
        ObjectMapper mapper = new ObjectMapper();
        Address address = new Address(
                456L,
                "Россия",
                "Москва",
                "Пушкина",
                123
        );
        List<BankAccount> bankAccounts = Arrays.asList(
                new BankAccount(789L, "Счет1", 12345),
                new BankAccount(654L, "Счет2", 1234567)
        );
        String expectedJSON = "{" +
                "\"id\":123," +
                "\"name\":\"Вася\"," +
                "\"surname\":\"Пупкин\"," +
                "\"age\":22," +
                "\"address\":{" +
                "\"id\":456," +
                "\"country\":\"Россия\"," +
                "\"city\":\"Москва\"," +
                "\"num\":123," +
                "\"street\":\"Пушкина\"}," +
                "\"bankAccounts\":[" +
                "{\"id\":789," +
                "\"accountName\":\"Счет1\"," +
                "\"cash\":12345},{" +
                "\"id\":654," +
                "\"accountName\":\"Счет2\"," +
                "\"cash\":1234567}]}";

        try {
            User user = mapper.readValue(expectedJSON, User.class);
            assertEquals(Long.valueOf(123L), user.getId());
            assertEquals("Вася", user.getName());
            assertEquals("Пупкин", user.getSurname());
            assertEquals(Integer.valueOf(22), user.getAge());
            assertEquals(address, user.getAddress());
            assertEquals(bankAccounts, user.getBankAccounts());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void notFullJSONUserCanBeDeserializedByJacksonObjectMapperToUserObject() {
        ObjectMapper mapper = new ObjectMapper();
        String expectedJSON = "{" +
                "\"id\":null," +
                "\"name\":\"Вася\"," +
                "\"surname\":\"Пупкин\"," +
                "\"age\":22," +
                "\"address\":{" +
                "\"id\":null," +
                "\"country\":\"Россия\"," +
                "\"city\":\"Москва\"," +
                "\"num\":123," +
                "\"street\":\"Пушкина\"}," +
                "\"bankAccounts\":null}";

        try {
            User user = mapper.readValue(expectedJSON, User.class);
            assertNull(user.getId());
            assertNull(user.getBankAccounts());
            assertNull(user.getAddress().getId());

            assertEquals("Вася", user.getName());
            assertEquals("Пупкин", user.getSurname());
            assertEquals(Integer.valueOf(22), user.getAge());
            assertEquals("Россия", user.getAddress().getCountry());
            assertEquals("Москва", user.getAddress().getCity());
            assertEquals("Пушкина", user.getAddress().getStreet());
            assertEquals(Integer.valueOf(123), user.getAddress().getNum());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}