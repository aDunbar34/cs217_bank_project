package uk.co.asepstrath.bank;
import io.jooby.ModelAndView;
import io.jooby.annotations.*;
import kong.unirest.Unirest;
import kong.unirest.json.*;
import org.slf4j.Logger;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.util.*;
@Path("/")
public class Controller {
    private static DataSource dataSource;
    private final Logger logger;

    public Controller(DataSource ds, Logger log){
        dataSource = ds;
        logger = log;
    }



    @GET("/bank")
    public ModelAndView getHome (){
        return new ModelAndView("home.hbs");
    }
    @GET("/about")
    public ModelAndView getAbout (){
        return new ModelAndView("about.hbs");
    }


    public ArrayList<Account> fetchAccountData(){
        String JSON;
        JSON = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team6/accounts").asJson().getBody());
        return parseJSON(JSON);
    }
    public ArrayList<Account> parseJSON(String response){
        ArrayList<Account> accountsArray = new ArrayList<>();
        JSONArray accountsDataJSONArray = new JSONArray(response);
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
        JSON = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team6/transactions?PageSize=10000").asJson().getBody());
        return parseJSONTransaction(JSON);
    }

    public ArrayList<Transaction> parseJSONTransaction(String response) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        JSONArray transactionsData = new JSONArray(response);
        for (int i = 0; i < transactionsData.length(); i++) {
            JSONObject accountData = transactionsData.getJSONObject(i);
            transactions.add(new Transaction(accountData.getString("withdrawAccount"),
                    accountData.getString("depositAccount"),
                    accountData.getString("timestamp"),
                    accountData.getString("id"),
                    accountData.getBigDecimal("amount"),
                    accountData.getString("currency")));
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
                bankTransaction.setStatus(rs.getInt("status"));
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

    public static Account getAccountById(String id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement prep = connection.prepareStatement("SELECT * FROM accounts WHERE id = ?;");
            prep.setString(1, id);
            ResultSet rs = prep.executeQuery();
            if (!rs.next()) {
                return new Account(id);
            }
            String name = rs.getString("name");
            BigDecimal balance = rs.getBigDecimal("balance");
            String accountType = rs.getString("accountType");
            String currency = rs.getString("currency");
            Account bankUser = new Account(id, name, balance, accountType, currency);
            rs.close();
            return bankUser;
        } catch (SQLException e) {
            return null;
        }
    }

    public void applyTransactions(ArrayList<Transaction> transactions) {
        try (Connection connection = dataSource.getConnection()) {
            Collections.sort(transactions);
            for (Transaction transac : transactions) {
                if (transac.getStatus() == 0) {
                    Account deposit = getAccountById(transac.getDepositAcc());
                    Account withdraw = getAccountById(transac.getWithdrawAcc());
                    transac.transaction();
                    PreparedStatement prepared = connection.prepareStatement("UPDATE transactions SET status = ? WHERE id = ?;");
                    prepared.setInt(1, transac.getStatus());
                    prepared.setString(2, transac.getTransactionID());
                    prepared.executeUpdate();
                    prepared = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?;");
                    prepared.setBigDecimal(1, deposit.getBalance());
                    prepared.setString(2, deposit.getId());
                    prepared.executeUpdate();
                    prepared.setBigDecimal(1, withdraw.getBalance());
                    prepared.setString(2, withdraw.getId());
                    prepared.executeUpdate();
                }
            }
        } catch (SQLException e) {
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
        Map<String, Object> map = new HashMap<>();
        int totalSuccessful = 0;
        for (Transaction transac : array){
            if (transac.getStatus() == 1){
                totalSuccessful ++;
            }
        }
        map.put("transaction", "transaction");
        map.put("user", arrayListTransactionAcc);
        map.put("total", Integer.toString(totalSuccessful));
        return new ModelAndView("TransactionData.hbs", map);
    }

    public void removeFraudulentTransactions() {
        String jsonResult = String.valueOf(Unirest.get("http://api.asep-strath.co.uk/api/Team6/fraud")
                .header("accept", "application/json")
                .asJson()
                .getBody());
        JSONArray jFraudID = new JSONArray(jsonResult);
        try (Connection connection = dataSource.getConnection()){ //deletes fraudulent transactions
            for (int i = 0; i < jFraudID.length(); i++) {
                PreparedStatement prepare = connection.prepareStatement("DELETE FROM transactions WHERE id = ?;");
                prepare.setString(1, jFraudID.getString(i));
                prepare.executeUpdate();
            }
        }
        catch (SQLException e) {}
    }
    @GET("/table")
    public ModelAndView getTable () {
        Map<String, Object> model = new HashMap<>();
        ArrayList <Account> accs = App.getAccounts();
        model.put("accs", accs);
        return new ModelAndView("AccountTable.hbs", model);
    }
}




