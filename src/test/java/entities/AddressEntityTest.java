package entities;

import org.junit.Test;


import static org.junit.Assert.*;

public class AddressEntityTest {

    @Test
    public void whenCreateAddressWithEmptyConstructorThenAllFieldsAreNull() {
        Address address = new Address();

        assertNull(address.getId());
        assertNull(address.getCountry());
        assertNull(address.getCity());
        assertNull(address.getStreet());
        assertNull(address.getNum());
    }

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
        assertNotNull(address.getNum());

        assertEquals(Long.valueOf(123L), address.getId());
        assertEquals("Россия", address.getCountry());
        assertEquals("Москва", address.getCity());
        assertEquals("Пушкина", address.getStreet());
        assertEquals(Integer.valueOf(123), address.getNum());
    }
}