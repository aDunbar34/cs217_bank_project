
package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Transaction {
    String WithdrawAccount;
    String DepositAccount;
    String timestamp;
    String id;
    BigDecimal amount;
    String currency;

    public Transaction(String withdrawAcc, String depositAcc, String time, String transactionId, double transfer, String currencyType){
        String WithdrawAccount = withdrawAcc;
        String DepositAccount = depositAcc;
        String timestamp = time;
        String id = transactionId;
        BigDecimal amount = BigDecimal.valueOf(transfer);
        String currency = currencyType;
    }

    public String getWithdrawAccount(){
        return WithdrawAccount;
    }

    public String getDepositAccount(){
        return DepositAccount;
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
        if (WithdrawAccount == null || DepositAccount == null) {
            return "withdraw or deposit acc id is missing";
        }

        return "Transaction{" +
                "withdrawAccount=" + WithdrawAccount + '\'' +
                ", depositAccount=" + DepositAccount + '\'' +
                ", timestamp=" + timestamp + '\'' +
                ", id=" + id + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +

                '}';
    }
}
