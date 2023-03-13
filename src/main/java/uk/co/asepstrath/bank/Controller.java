package uk.co.asepstrath.bank;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import io.jooby.ModelAndView;
import io.jooby.annotations.*;
import io.swagger.annotations.*;
import kong.unirest.Unirest;
import kong.unirest.json.*;
import org.slf4j.Logger;
import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.util.*;
@Path("/bank")

public class Controller {

    private static DataSource dataSource;
    private final Logger logger;

    public Controller(DataSource ds, Logger log){
        dataSource = ds;
        logger = log;
    }

    @GET("/accounts")
    public ArrayList<Account> displayAccounts() { return App.getAccounts(); }
    public ArrayList<Account> fetchAccountData(){
        String JSON;
        JSON = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team6/accounts").asJson().getBody());
        return parseJSON(JSON);
    }
    public ArrayList<Account> parseJSON(String response){
        ArrayList<Account> accountsArray = new ArrayList<>(); //stores accounts
        JSONArray accountsDataJSONArray = new JSONArray(response); //adds accounts from response to json array
        for(int i = 0; i < accountsDataJSONArray.length(); i++){
            JSONObject accountsDataJSONObject = accountsDataJSONArray.getJSONObject(i);
            accountsArray.add(new Account(accountsDataJSONObject.getString("id"),
                    accountsDataJSONObject.getString("name"),
                    accountsDataJSONObject.getBigDecimal("balance").round(new MathContext(10)),
                    accountsDataJSONObject.getString("currency"),
                    accountsDataJSONObject.getString("accountType")));
        }
        return accountsArray;
    }

    @GET("/table")
    public ModelAndView getAccountData () {
        ArrayList <Account> accounts = retreiveData();
        Map<String, Object> model = new HashMap<>();
        model.put("accs", accounts);
        return new ModelAndView("AccountTable.hbs", model);
    }

    public ArrayList<Account> retreiveData(){
        ArrayList<Account> accounts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM accounts ORDER BY name ASC";
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String id = rs.getString("id");
                String name = rs.getString("name");
                BigDecimal balance = rs.getBigDecimal("balance");
                String accountType = rs.getString("accountType");
                String currency = rs.getString("currency");
                Account user = new Account(id, name, balance, accountType, currency);
                accounts.add(user);
            }
            rs.close();
        } catch (SQLException e){
            logger.error("Error getting data from database", e);
        }
        return accounts;
    }

    public ArrayList<Transaction> fetchTransactionData(){
        String JSON;
        JSON = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team6/transactions").asJson().getBody());
        System.out.println(JSON);
        return parseJSONTransaction(JSON);
    }

    public ArrayList<Transaction> parseJSONTransaction(String response) {
        ArrayList<Transaction> transactions = new ArrayList<>();    //arraylist to store each transaction
        JSONArray transactionsData = new JSONArray(response);   //storing all transaction in a JSON Array
        for (int i = 0; i < transactionsData.length(); i++) {
            JSONObject accountData = transactionsData.getJSONObject(i);
            transactions.add(new Transaction(accountData.getString("withdrawAccount"),
                    accountData.getString("depositAccount"),
                    accountData.getString("timestamp"),
                    accountData.getString("id"),
                    accountData.getBigDecimal("amount"),
                    accountData.getString("currency")));   //stores each transaction to transactions arrayList
        }
        return transactions;
    }

    @GET("/TransactionTable")
    public ModelAndView getTransactionData () {
        ArrayList <Transaction> transactions = displayTransaction();
        Map<String, Object> model = new HashMap<>();
        model.put("transactions", transactions);
        model.put("error","Error getting data from database");
        return new ModelAndView("TransactionTable.hbs", model);
    }
    public ArrayList <Transaction> displayTransaction(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM transactions";
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                String withdrawAcc = rs.getString("withdrawAccount");
                String depositAcc = rs.getString("depositAccount");
                String timestamp = rs.getString("timestamp");
                String transactionID = rs.getString("id");
                BigDecimal amount = rs.getBigDecimal("amount");
                String currency = rs.getString("currency");
                Transaction bankTransaction = new Transaction(withdrawAcc, depositAcc, timestamp, transactionID, amount, currency);
                bankTransaction.setStatus(0);
                transactions.add(bankTransaction);
            }
            rs.close();
            statement.close();
        } catch (SQLException e){
            logger.error("Error getting data from database", e);
        }
        applyTransactions(transactions);
        return  transactions;
    }

    public static Account getAccountById(String id){
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement prep = connection.prepareStatement("SELECT * FROM accounts WHERE id = ?;");
            prep.setString(0, id);
            ResultSet rs = prep.executeQuery();
            if (!rs.next()){
                return new Account(id);
            }
            id = rs.getString("id");
            String name = rs.getString("name");
            BigDecimal balance = rs.getBigDecimal("balance");
            String accountType = rs.getString("accountType");
            String currency = rs.getString("currency");
            Account bankUser = new Account(id, name, balance, accountType, currency);
            bankUser.setBalance(balance);
            rs.close();
            return bankUser;
        }
        catch (SQLException e){
            return new Account(id);
        }
    }

    public void applyTransactions(ArrayList<Transaction> transactions) {
        try {
            Connection connection = dataSource.getConnection();
            Collections.sort(transactions);
            for (Transaction transac : transactions) {
                if (transac.getStatus() == 0) {
                    Account deposit = getAccountById(transac.getDepositAcc());
                    Account withdraw = getAccountById(transac.getDepositAcc());
                    deposit.setBalance(deposit.getBalance());
                    withdraw.setBalance(withdraw.getBalance());
                    PreparedStatement prep = connection.prepareStatement("UPDATE transactions SET status = ? WHERE id = ?;");
                    transac.transaction();
                    prep.setInt(1, transac.getStatus());
                    prep.setString(2, transac.getTransactionID());
                    prep.executeUpdate();
                    prep = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?;");
                    prep.setBigDecimal(1, deposit.getBalance());
                    prep.setString(2, deposit.getId());
                    prep.executeUpdate();
                    prep.setBigDecimal(1, withdraw.getBalance());
                    prep.setString(2, withdraw.getId());
                    prep.executeUpdate();
                    prep.close();
                }
            }
        }
        catch(SQLException e){
            logger.error("Error getting data from database", e);
        }
    }

    public ArrayList<TransactionDetails> retrieveDataTransactionAccount() {
        ArrayList<Transaction> transactions = displayTransaction();
        ArrayList<Account> accounts = retreiveData();
        ArrayList<TransactionDetails> transactionInfo = new ArrayList<>();
        for (Account account : accounts){
            ArrayList<Transaction> tempArray = new ArrayList<>();
            for (Transaction transaction : transactions){
                if (transaction.getWithdrawAcc().equals(account.getId()) || transaction.getDepositAcc().equals(account.getId())){
                    tempArray.add(transaction);
                }
            }
            transactionInfo.add(new TransactionDetails(account, tempArray));
        }
        return transactionInfo;
    }

    @GET("/transactionDetails")
    public ModelAndView transactionDataAcc() {
        ArrayList<TransactionDetails> arrayListTransactionAcc = retrieveDataTransactionAccount();
        ArrayList<Transaction> array = displayTransaction();
        Map<String, Object> mapTest = new HashMap<>();
        int totalSuccessful = 0;
        for (Transaction transac : array){
            if (transac.getStatus() == 1){
                totalSuccessful ++;
            }
        }
        mapTest.put("transaction", "transaction");
        mapTest.put("user", arrayListTransactionAcc); //users show in hbs file
        mapTest.put("total", Integer.toString(totalSuccessful)); //total successful transactions is passed in
        return new ModelAndView("TransactionData.hbs", mapTest);
    }
//    @GET("/Table")
//    public ModelAndView getTable () {
//        Map<String, Object> model = new HashMap<>();
//        ArrayList <Account> accs = App.getAccounts();
//        model.put("accs", accs);
//        return new ModelAndView("AccountTable.hbs", model);
//    }

    @GET("/JSON")
    public String displayAccJSON () {
        App instance = new App();
        instance.onStart();
        String json = new Gson().toJson(App.getAccounts());
        return json;
    }
}



