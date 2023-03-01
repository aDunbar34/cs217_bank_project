//package uk.co.asepstrath.bank;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.math.MathContext;
//import java.math.RoundingMode;
//
//public class AccountTests {
//
//
//    @Test
//    public void createAccount(){
//        Account a = new Account();
//        Assertions.assertNotNull(a);
//    }
//
//    @Test
//    public void Test1(){
//        Account a = new Account();
//        Assertions.assertEquals(0, a.getBalance().intValue());
//    }
//
//    @Test
//    public void Test2(){
//        Account a = new Account(20, "Zoe");
//        a.deposit(50);
//        Assertions.assertEquals(70, a.getBalance().intValue());
//    }
//
//    @Test
//    public void Test3(){
//        Account a = new Account(40,"Luke");
//        a.withdraw(20);
//        Assertions.assertEquals(20, a.getBalance().intValue());
//    }
//
//    @Test
//    public void Test4(){
//        Account a = new Account(30, "George");
//        Assertions.assertThrows(ArithmeticException.class, () -> a.withdraw(100));
//    }
//
//    @Test
//    public void Test5(){
//        Account a = new Account(20, "Lucy");
//        for(int x=0; x<5; x++)
//            a.deposit(10);
//        for(int x=0; x<3; x++)
//            a.withdraw(20);
//        Assertions.assertEquals(10, a.getBalance().intValue());
//    }
//
//    @Test
//    public void Test6(){
//        Account a = new Account(5.45, "Bob");
//        a.deposit(17.56);
//        Assertions.assertEquals(a.getBalance(), BigDecimal.valueOf(23.01));
//    }
//
//    @Test
//    public void Test7(){
//        Account a = new Account(500, "Lauren");
//        Assertions.assertEquals("Account Name: Lauren Balance:£ 500.0", a.toString());
//    }
//}

