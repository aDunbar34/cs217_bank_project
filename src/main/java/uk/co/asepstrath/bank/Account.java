package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Account {

    BigDecimal balance = BigDecimal.valueOf(0);

    String name;
    String id;
    String accountType;
    String currency;


    public Account(String Name, BigDecimal Balance){
        name = Name;
        balance = Balance;
    }
    public Account(String id, String name, BigDecimal balance, String currency, String accountType){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
        this.accountType = accountType;
    }

    public Account(String id) {
    }


    public String getName() {
        return name;
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
    public void createName(String Name) {
        name = Name;
    }

    public String getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAccountType() {
        return accountType;
    }


    @Override
    public String toString(){
        return id + name + balance + currency + accountType;
    }



    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0)  {
            throw new ArithmeticException("You do not have an overdraft!");
        }
        balance = balance.subtract(amount);
    }
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

