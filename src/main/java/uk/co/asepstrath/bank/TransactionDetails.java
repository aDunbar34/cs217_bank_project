package uk.co.asepstrath.bank;
import java.math.BigDecimal;
import java.util.ArrayList;

public class TransactionDetails {

    private final Account account; //account for transaction information
    private ArrayList<Transaction> transactions; //array list storing all transactions associated with the account
    private int successfulTransactions; //number of successful transactions
    private int failedTransactions; //number of failed transactions
    private BigDecimal initialBalance;

    public TransactionDetails(Account account, ArrayList<Transaction> transactions){
        this.account = account;
        this.transactions = transactions;
    }

    public Account getAccount(){
        return account; //returns account details
    }

    public ArrayList<Transaction> getTransactions(){
        return transactions; //returns all transactions associated with the account
    }
    public BigDecimal getInitialBalance(){
        return account.getBalance(); //returns the account's initial balance
    }

    public int getSuccessfulTransactions(){
        successfulTransactions = 0; //initialises the successful transactions to 0
        for (Transaction transac : transactions) { //loops through all transactions for that account
            if (transac.getStatus() == 1){
                successfulTransactions++; //adds 1 to successful transactions if the status shows the transaction was successful
            }
        }
        return successfulTransactions; //returns the amount of successful transactions
    }

    public int getFailedTransactions(){
        failedTransactions = 0; //initialises the failed transaction to 0
        for(Transaction transac : transactions){ //loops through all transactions for that account
            if(transac.getStatus() == -1){
                failedTransactions++; //adds 1 to failed transactions if the status shows the transaction has failed
            }
        }
        return failedTransactions; //returns the amount of failed transactions
    }

}