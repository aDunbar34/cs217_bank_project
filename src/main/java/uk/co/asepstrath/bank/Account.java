package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Account{

    /*
            VARIABLES
     */
    String id;
    String name;
    BigDecimal balance;
    String currency;
    String accountType;

    /*
            CONSTRUCTORS
     */
    public Account(String id, String name, double balance, String accountType, String currency){
        this.id = id;
        this.name = name;
        this.balance = new BigDecimal(balance);
        this.accountType = accountType;
        this.currency = currency;
    }
    public Account(String id, String name, String accountType, String currency) {
        this.id = id;
        this.name = name;
        this.balance = new BigDecimal(0);
        this.accountType = accountType;
        this.currency = currency;
    }

    /*
            GETTERS
     */
    public String getId() {
        return this.id;
    }
    public String getCurrency() { return this.currency; }
    public String getAccountType() {
        return this.accountType;
    }
    public double getBalance() {
        if ( balance == null ) {
            return 0.0;
        }
        return balance.doubleValue();
    }
    public String getName() { return name; }


    /*
            SETTERS
     */
    public void setId(String id) {
        this.id = id;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public void setName(String name) {this.name = name; }
    public void setBalance(double bal) { this.balance = new BigDecimal(bal);}


    /*
            MODIFIERS
     */
    public void deposit(double amount) {
        this.balance = this.balance.add(BigDecimal.valueOf(amount));
    }
    public void withdraw(double amount) {
        if(balance.compareTo(BigDecimal.valueOf(amount))<0){
            throw new ArithmeticException("Account balance can't be Negative");
        }
        else{
            balance = balance.subtract(BigDecimal.valueOf(amount));
        }
    }


    /*
            ADDITIONAL FUNCTIONS
     */
    @Override
    public String toString(){
        if (name == null || balance == null) {
            return "Account is missing name or balance";
        }
        return "Account{" +
                "id='" + id + '\'' +
                ", accountName='" + name + '\'' +
                ", accountBalance=" + balance +
                ", currency='" + currency + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}