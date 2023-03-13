package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class AccountTests {


    @Test
    public void createAccount(){
        Account a = new Account("testid", "namey mcnameface", 0.00,
                "Auto Loan Account", "GDP");
        Assertions.assertNotNull(a);
    }

    @Test
    public void Test1(){
        Account a = new Account("testid", "namey mcnameface", 0.00,
                "Auto Loan Account", "GDP");
        Assertions.assertEquals(0.0, a.getBalance());
    }

    @Test
    public void Test2(){
        Account a = new Account("testid", "namey mcnameface", 20.00,
                "Auto Loan Account", "GDP");
        a.deposit(50);
        Assertions.assertEquals(70.0, a.getBalance());
    }

    @Test
    public void Test3(){
        Account a = new Account("testid", "namey mcnameface", 40.00,
                "Auto Loan Account", "GDP");
        a.withdraw(20);
        Assertions.assertEquals(20.0, a.getBalance());
    }

    @Test
    public void Test4(){
        Account a = new Account("testid", "Namey McNameFace", 30.00,
                "Auto Loan Account", "GDP");
        Assertions.assertThrows(ArithmeticException.class, () -> a.withdraw(100.00));
    }

    @Test
    public void Test5(){
        Account a = new Account("testid", "namey mcnameface", 20.00,
                "Auto Loan Account", "GDP");
        for(int x=0; x<5; x++)
            a.deposit(10);
        for(int x=0; x<3; x++)
            a.withdraw(20);
        Assertions.assertEquals(10.0, a.getBalance());
    }

    @Test
    public void Test6(){
        Account a = new Account("testid", "namey mcnameface", 5.45,
                "Auto Loan Account", "GDP");
        a.deposit(17.56);
        Assertions.assertEquals(a.getBalance(), 23.01);
    }

    @Test
    public void Test7(){
        Account a = new Account("testid", "namey mcnameface", 500.00,
                "Auto Loan Account", "GDP");
        Assertions.assertEquals("Account{id='testid', accountName='namey mcnameface', accountBalance=500, currency='GDP', accountType='Auto Loan Account'}", a.toString());
    }
}

