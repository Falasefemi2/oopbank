import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.unifibank.model.User;
import org.unifibank.services.BankService;

public class BankServiceTest {
    private BankService bankService;
    private User user;

    @Test
    public void testSignUp() {
        bankService = new BankService(true);
        user = bankService.signUp("david", "pass123");
        assertNotNull(user);
        assertEquals("david", user.getUsername());
        assertNotNull(user.getAccount());
        assertTrue(user.getAccount().getAccountNumber().matches("\\d{10}"));
    }

    @Test
    public void testLogin() {
        BankService bs = new BankService(true);
        bs.signUp("david", "pass123");
        User loggUser = bs.login("david", "pass123");

        assertNotNull(loggUser);
        assertEquals("david", loggUser.getUsername());
    }

    @Test
    public void testGenerateAccountNumber() {
        BankService bankService = new BankService();
        String acctNumber = bankService.generateAccountNumber();

        assertNotNull(acctNumber);
        assertEquals(10, acctNumber.length());
        assertTrue(acctNumber.matches("\\d{10}"));
    }

    @Test
    public void testDeposit() {
        BankService bankService = new BankService(true);
        user = bankService.signUp("david", "pass123");
        bankService.deposit(user, 2000);
        assertEquals(0, user.getAccount().getBalance().compareTo(BigDecimal.valueOf(2000)));
    }

    @Test
    public void testWithdraw() {
        BankService bankService = new BankService(true);
        user = bankService.signUp("david", "pass123");
        bankService.deposit(user, 2000);
        bankService.withdraw(user, 1000);
        assertEquals(0, user.getAccount().getBalance().compareTo(BigDecimal.valueOf(1000)));
    }

    @Test
    public void testTransfer() {
        BankService bankService = new BankService(true);
        user = bankService.signUp("david", "pass123");
        bankService.deposit(user, 2000);
        bankService.transfer(user, "bills", 1000);
        assertEquals(0, user.getAccount().getBalance().compareTo(BigDecimal.valueOf(1000)));
    }

    @Test
    public void testPayBill() {
        BankService bankService = new BankService(true);
        user = bankService.signUp("david", "pass123");
        bankService.deposit(user, 2000);
        bankService.payBill(user, "food", 1000);
        assertEquals(0, user.getAccount().getBalance().compareTo(BigDecimal.valueOf(1000)));
    }

    @Test
    public void testBuyAirtime() {
        BankService bankService = new BankService(true);
        user = bankService.signUp("david", "pass123");
        bankService.deposit(user, 2000);
        bankService.buyAirtime(user, "08012345678", 1000);
        assertEquals(0, user.getAccount().getBalance().compareTo(BigDecimal.valueOf(1000)));
    }
}
