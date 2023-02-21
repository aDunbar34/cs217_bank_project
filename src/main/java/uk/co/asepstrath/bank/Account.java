package uk.co.asepstrath.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account{
    BigDecimal accountBalance;

    String accountName;


    public Account(){
        accountBalance = new BigDecimal(0);
    }

    public Account(double amount, String name){

        accountBalance = BigDecimal.valueOf(amount);
        accountName = name;
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

    @Override
    public String toString(){
        return "Account Name: " + accountName + " Balance:£ " + accountBalance;
    }


}



