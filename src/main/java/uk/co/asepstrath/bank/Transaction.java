package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Transaction implements Comparable<Transaction> {
    private final String withdrawAccount; //stores account to take money from
    private final String depositAccount;  //stores account that receives money
    private final String id; //ID for transaction
    private final String timestamp; //timestamp of transaction
    private final BigDecimal amount; //amount of money in transaction
    private final String currency; //currency of transaction
    private int status = 0; //initially 0, 1 if transaction is successful, -1 if failed

    public Transaction(String withdrawAccount, String depositAccount, String timestamp, String transactionID, BigDecimal amount, String currency){
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.timestamp = timestamp;
        this.id = transactionID;
        this.amount = amount;
        this.currency = currency;
    }


    public String getWithdrawAcc(){
        return withdrawAccount; //gets account that money is being withdrawn from
    }

    public String getDepositAcc(){
        return depositAccount; //gets account that money is going into
    }
    public String getTimestamp(){
        return timestamp;
    }
    public String getTransactionID(){
        return id; //gets unique transaction ID
    }
    public double getAmount(){
        return amount.doubleValue(); //returns amount involved in transaction
    }
    public String getCurrency(){
        return currency; //gets currency of money involved in transaction
    }

    @Override
    public String toString(){
        if(withdrawAccount == null || depositAccount == null){
            return "some account id missing";
        }
        return "Transaction{" + "withdrawAcc =" + withdrawAccount + '\'' +
                ", depositAcc =" + depositAccount + '\'' +
                ", depositAcc =" + depositAccount + '\'' +
                ", timestamp =" + timestamp + '\'' +
                ", transactionID =" + id + '\'' +
                ", amount =" + amount + '\'' +
                ", currency =" + currency + '\'' +
                '}';
    }

//    public String getStatusString(){
//        if(status == 1){
//            return "Transaction Complete"; //returns Transaction Complete if status = 1
//        }
//        else if (status == -1){
//            return "Transaction Failed"; //returns Transaction Failed if status = -1
//        }
//        else{
//            return "Transaction Incomplete"; //else return Transaction Incomplete
//        }
//    }

    public void setStatus(int n){
        if(n <= 1 && n >= -1){  //executes if n is in range of 1 to -1
            status = n;
        }
    }
    public void transaction(){
        Account withdrawAcc = Controller.getAccountById(withdrawAccount);
        Account depositAcc = Controller.getAccountById(depositAccount);
        double withdrawAccBalance = withdrawAcc.getBalance().doubleValue(); //converts the balance of the account being withdrawn from to double
        double amountDouble = amount.doubleValue(); //converts the amount being withdrawn to double so the remaining balance can be compared to 0
        if(this.status == 0) {  //if transaction is incomplete, execute
            if((withdrawAccBalance - amountDouble) >= 0){ //only carries out the transaction if there is sufficient funds
                withdrawAcc.withdraw(amount);
                depositAcc.deposit(amount);
                status = 1;
            }
            else {
                status = -1; //if there is insufficient funds, the transaction will fail and status = -1
            }
        }
    }

//    public String generateID(){
//        //need to discuss how we are going to do this
//        return null;
//    }

    public int getStatus() {
        return status;
    }
    @Override
    public int compareTo(Transaction o) {
        return this.timestamp.compareTo(o.timestamp);
    }
}
