package uk.co.asepstrath.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account{
    String id;
    String accountName;
    BigDecimal accountBalance;
    String currency;
    String accountType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Account(){
        accountBalance = new BigDecimal(0);
    }

    public Account(String name, double amount){
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

    public String getAccountName() {
        return accountName;
    }

    @Override
    public String toString(){
        return accountName + accountBalance;
    }
}