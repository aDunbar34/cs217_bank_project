package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Account {

    BigDecimal balance = BigDecimal.valueOf(0);

    String name;
    String id;
    String currency;
    String accType;


    public Account(String Name, BigDecimal Balance){
        name = Name;
        balance = Balance;
    }
    public Account(String id, String name, BigDecimal balance, String currency, String accountType){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
        this.accType = accountType;
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

    public String getAccType() {
        return accType;
    }

    public String getCurrency() {
        return currency;
    }


    @Override
    public String toString(){
        return "Account{id='" + id + "', accountName='" + name + "', accountBalance=" + balance + ", accountType='" + accType + "', currency='" + currency + "'}";
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

