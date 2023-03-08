
package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Transaction {
    String withdrawAccount;
    String depositAccount;
    String timestamp;
    String id;
    BigDecimal amount;
    String currency;

    public Transaction(String withdrawAcc, String depositAcc, String time, String transactionId, double transfer, String currencyType){
        withdrawAccount = withdrawAcc;
        depositAccount = depositAcc;
        timestamp = time;
        id = transactionId;
        amount = BigDecimal.valueOf(transfer);
        currency = currencyType;
    }

    public String getWithdrawAccount(){
        return withdrawAccount;
    }

    public String getDepositAccount(){
        return depositAccount;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getId(){
        return id;
    }

    public double getAmount(){
        return amount.doubleValue();
    }

    public String getCurrency(){
        return currency;
    }

    @Override
    public String toString(){
        /*if (WithdrawAccount == null || DepositAccount == null) {
            return "withdraw or deposit acc id is missing";
        }*/

        return "Transaction{" +
                "withdrawAccount=" + withdrawAccount + '\'' +
                ", depositAccount=" + depositAccount + '\'' +
                ", timestamp=" + timestamp + '\'' +
                ", id=" + id + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +

                '}';
    }
}
