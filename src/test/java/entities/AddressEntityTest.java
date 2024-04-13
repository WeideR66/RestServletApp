package entities;

import org.junit.Test;


import static org.junit.Assert.*;

public class AddressEntityTest {

    @Test
    public void whenCreateAddressWithFullConstructorThenAllFieldsAreNotNull() {
        Address address = new Address(
                123L,
                "Россия",
                "Москва",
                "Пушкина",
                123
        );

        assertNotNull(address.getId());
        assertNotNull(address.getCountry());
        assertNotNull(address.getCity());
        assertNotNull(address.getStreet());
        assertNotNull(address.getNumber());

        assertEquals(Long.valueOf(123L), address.getId());
        assertEquals("Россия", address.getCountry());
        assertEquals("Москва", address.getCity());
        assertEquals("Пушкина", address.getStreet());
        assertEquals(Integer.valueOf(123), address.getNumber());
    }

    @Test(expected = NullPointerException.class)
    public void whenNonNullFieldIsNullThenNullPointerExceptionThrow() {
        new Address(null, "Russia", "Moscow", null, 4);
    }
}