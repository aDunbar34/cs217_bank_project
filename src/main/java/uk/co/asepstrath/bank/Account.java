package uk.co.asepstrath.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {
    BigDecimal accountBalance;

    public Account(){
        accountBalance = new BigDecimal(0);
    }

    public Account(double amount){
        accountBalance = BigDecimal.valueOf(amount);
    }

    public void deposit(double amount) {
        accountBalance = accountBalance.add(BigDecimal.valueOf(amount));
    }

    public void withdraw(double amount) {
        if(accountBalance.compareTo(BigDecimal.valueOf(amount))<0){
            throw new ArithmeticException("Account balance can't be Negative");
        }
        else{
            accountBalance = accountBalance.subtract(BigDecimal.valueOf(amount));
        }
    }

    public BigDecimal getBalance() {
        return accountBalance;
    }

}
