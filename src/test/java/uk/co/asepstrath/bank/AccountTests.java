package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class AccountTests {


    @Test
    public void createAccount(){
        Account a = new Account("testid", "namey mcnameface", new BigDecimal(0),
                "Auto Loan Account", "GDP");
        Assertions.assertNotNull(a);
    }

    @Test
    public void Test1(){
        Account a = new Account("testid", "namey mcnameface", new BigDecimal(0.0),
                "Auto Loan Account", "GDP");
        Assertions.assertEquals(new BigDecimal(0), a.getBalance());
    }

    @Test
    public void Test2(){
        Account a = new Account("testid", "namey mcnameface", new BigDecimal(20),
                "Auto Loan Account", "GDP");
        a.deposit(BigDecimal.valueOf(50));
        Assertions.assertEquals(new BigDecimal(70), a.getBalance());
    }

    @Test
    public void Test3(){
        Account a = new Account("testid", "namey mcnameface", new BigDecimal(40),
                "Auto Loan Account", "GDP");
        a.withdraw(BigDecimal.valueOf(20));
        Assertions.assertEquals(new BigDecimal(20), a.getBalance());
    }

    @Test
    public void Test4(){
        Account a = new Account("testid", "Namey McNameFace", new BigDecimal(30),
                "Auto Loan Account", "GDP");
        Assertions.assertThrows(ArithmeticException.class, () -> a.withdraw(BigDecimal.valueOf(100.00)));
    }

    @Test
    public void Test5(){
        Account a = new Account("testid", "namey mcnameface", new BigDecimal(20),
                "Auto Loan Account", "GDP");
        for(int x=0; x<5; x++)
            a.deposit(BigDecimal.valueOf(10));
        for(int x=0; x<3; x++)
            a.withdraw(BigDecimal.valueOf(20));
        Assertions.assertEquals(new BigDecimal(10), a.getBalance());
    }

    @Test
    public void Test7(){
        Account a = new Account("testid", "namey mcnameface", new BigDecimal(500),
                "Auto Loan Account", "GDP");
        Assertions.assertEquals("Account{id='testid', accountName='namey mcnameface', accountBalance=500, accountType='Auto Loan Account', currency='GDP'}", a.toString());
    }
}

