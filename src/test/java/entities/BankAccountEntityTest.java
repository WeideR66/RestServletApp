package entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class BankAccountEntityTest {


    @Test
    public void whenCreateAddressWithFullConstructorThenAllFieldsAreNotNull() {
        BankAccount account = new BankAccount(
                123L,
                "Счет№1",
                12345
        );

        assertNotNull(account.getId());
        assertNotNull(account.getAccountName());
        assertNotNull(account.getCash());

        assertEquals(Long.valueOf(123L), account.getId());
        assertEquals("Счет№1", account.getAccountName());
        assertEquals(Integer.valueOf(12345), account.getCash());
    }

    @Test(expected = NullPointerException.class)
    public void whenNonNullFieldIsNullThenNullPointerExceptionThrow() {
        new BankAccount(null, null, 1452141);
    }
}