//package uk.co.asepstrath.bank;
//
//import io.jooby.ModelAndView;
//import org.junit.jupiter.api.Test;
//
//import javax.sql.DataSource;
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//public class UnitTest {

//
//        @Test
//        public void testGetHome() {
//            Controller controller = new Controller(null, null);
//            ModelAndView modelAndView = controller.getHome();
//            assertEquals("home.hbs", modelAndView.getViewName());
//        }
//
//        @Test
//        public void testGetAbout() {
//            Controller controller = new Controller(null, null);
//            ModelAndView modelAndView = controller.getAbout();
//            assertEquals("about.hbs", modelAndView.getViewName());
//        }
//
//    public void testParseJSON() {
//        Controller controller = new Controller(mock(DataSource.class), mock(Logger.class));
//        String json = "[{\"id\": \"1\",\"name\": \"John\",\"balance\": 1000.0,\"currency\": \"USD\",\"accountType\": \"Checking\"}]";
//        ArrayList<Account> accounts = controller.parseJSON(json);
//        assertEquals(1, accounts.size());
//        assertEquals("1", accounts.get(0).getId());
//        assertEquals("John", accounts.get(0).getName());
//        assertEquals(new BigDecimal("1000.0"), accounts.get(0).getBalance());
//        assertEquals("USD", accounts.get(0).getCurrency());
//        assertEquals("Checking", accounts.get(0).getAccountType());
//    }
//
//    @Test
//    public void testRetrieveData() throws SQLException {
//        DataSource dataSource = mock(DataSource.class);
//        Connection connection = mock(Connection.class);
//        Statement statement = mock(Statement.class);
//        ResultSet resultSet = mock(ResultSet.class);
//        when(dataSource.getConnection()).thenReturn(connection);
//        when(connection.createStatement()).thenReturn(statement);
//        when(statement.executeQuery(anyString())).thenReturn(resultSet);
//        when(resultSet.next()).thenReturn(true, false);
//        when(resultSet.getString("id")).thenReturn("1");
//        when(resultSet.getString("name")).thenReturn("John");
//        when(resultSet.getBigDecimal("balance")).thenReturn(new BigDecimal("1000.0"));
//        when(resultSet.getString("currency")).thenReturn("USD");
//        when(resultSet.getString("accountType")).thenReturn("Checking");
//        Controller controller = new Controller(dataSource, mock(Logger.class));
//        ArrayList<Account> accounts = controller.retreiveData();
//        assertEquals(1, accounts.size());
//        assertEquals("1", accounts.get(0).getId());
//        assertEquals("John", accounts.get(0).getName());
//        assertEquals(new BigDecimal("1000.0"), accounts.get(0).getBalance());
//        assertEquals("USD", accounts.get(0).getCurrency());
//        assertEquals("Checking", accounts.get(0).getAccountType());
//    }
//
//    @Test
//    public void testParseJSONTransaction() {
//        Controller controller = new Controller(mock(DataSource.class), mock(Logger.class));
//        String json = "[{\"withdrawAccount\":\"1\",\"depositAccount\":\"2\",\"timestamp\":\"2022-02-22T22:22:22.000Z\",\"id\":\"1\",\"amount\":1000.0,\"currency\":\"USD\"}]";
//        ArrayList<Transaction> transactions = controller.parseJSONTransaction(json);
//        assertEquals(1, transactions.size());
//        assertEquals("1", transactions.get(0).getTransactionID());
//        assertEquals("1", transactions.get(0).getWithdrawAcc());
//        assertEquals("2", transactions.get(0).getDepositAcc());
//        assertEquals(new BigDecimal("1000.0"), transactions.get(0).getAmount());
//        assertEquals("USD", transactions.get(0).getCurrency());
//        assertEquals("2022-02-22T22:22:22.000Z", transactions.get(0).getTimestamp());
//    }
//}


package uk.co.asepstrath.bank;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class UnitTest {

    @Test
    public void testGetWithdrawAcc() {
        Transaction t = new Transaction("A", "B", "2022-01-01T00:00:00.000Z", "12345", BigDecimal.valueOf(100.00), "USD");
        assertEquals("A", t.getWithdrawAcc());
    }

    @Test
    public void testGetDepositAcc() {
        Transaction t = new Transaction("A", "B", "2022-01-01T00:00:00.000Z", "12345", BigDecimal.valueOf(100.00), "USD");
        assertEquals("B", t.getDepositAcc());
    }

    @Test
    public void testGetTimestamp() {
        Transaction t = new Transaction("A", "B", "2022-01-01T00:00:00.000Z", "12345", BigDecimal.valueOf(100.00), "USD");
        assertEquals("2022-01-01T00:00:00.000Z", t.getTimestamp());
    }

    @Test
    public void testGetTransactionID() {
        Transaction t = new Transaction("A", "B", "2022-01-01T00:00:00.000Z", "12345", BigDecimal.valueOf(100.00), "USD");
        assertEquals("12345", t.getTransactionID());
    }

    @Test
    public void testGetAmount() {
        Transaction t = new Transaction("A", "B", "2022-01-01T00:00:00.000Z", "12345", BigDecimal.valueOf(100.00), "USD");
        assertEquals(100.00, t.getAmount());
    }

    @Test
    public void testGetCurrency() {
        Transaction t = new Transaction("A", "B", "2022-01-01T00:00:00.000Z", "12345", BigDecimal.valueOf(100.00), "USD");
        assertEquals("USD", t.getCurrency());
    }

    @Test
    public void testSetStatus() {
        Transaction t = new Transaction("A", "B", "2022-01-01T00:00:00.000Z", "12345", BigDecimal.valueOf(100.00), "USD");
        t.setStatus(1);
        assertEquals(1, t.getStatus());
        t.setStatus(-1);
        assertEquals(-1, t.getStatus());
        t.setStatus(2);
        assertEquals(-1, t.getStatus()); // status should not change when n is outside range
        t.setStatus(-2);
        assertEquals(-1, t.getStatus()); // status should not change when n is outside range
    }
}











