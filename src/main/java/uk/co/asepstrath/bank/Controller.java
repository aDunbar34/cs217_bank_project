package uk.co.asepstrath.bank;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotations.*;
import io.jooby.exception.StatusCodeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.math.BigDecimal;

@Path("/")
public class Controller {
    private DataSource dataSource;
    private Logger logger;

    public Controller(DataSource ds, Logger log) {
        dataSource = ds;
        logger = log;
    }

    public ArrayList<Account> fetchData() {
        String jsonResult = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team6/accounts")
                .asJson()
                .getBody());
        return parseJSON(jsonResult);
    }

    public ArrayList<Transaction> fetchTransactionData() {
        String jsonResult = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team6/Transactions")
                .asJson()
                .getBody());
        return parseTransactionJSON(jsonResult);
    }

    public ArrayList<Transaction> parseTransactionJSON(String res) {
        Gson gson = new Gson();
        Type transactionListType = new TypeToken<ArrayList<Transaction>>() {
        }.getType();
        ArrayList<Transaction> transactionList = null;
        try {
            transactionList = gson.fromJson(res, transactionListType);
        } catch (JsonParseException e) {
            // Handle the exception here
            e.printStackTrace();
        }
        return transactionList;
    }

    public ArrayList<Account> parseJSON(String res) {
        Gson gson = new Gson();
        Type accountListType = new TypeToken<ArrayList<Account>>() {
        }.getType();
        ArrayList<Account> accountList = null;
        try {
            accountList = gson.fromJson(res, accountListType);
        } catch (JsonParseException e) {
            // Handle the exception here
            e.printStackTrace();
        }
        return accountList;
    }


    @GET("/JSON")
    public String displayJSON() {
        String jsonResult = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team6/accounts")
                .asJson()
                .getBody());
        return jsonResult;
    }

    @GET("/TransactionJSON")
    public String displayTransactionJSON() {
        String jsonResult = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team6/Transactions")
                .asJson()
                .getBody());
        return jsonResult;
    }

    @GET("/table")
    public ModelAndView displaytable() {
        Map<String, Object> model = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM accounts ORDER BY name ASC");
            ArrayList<Account> data = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");
                String accountType = rs.getString("accountType");
                String currency = rs.getString("currency");
                Account account = new Account(id, name, balance, accountType, currency);
                data.add(account);
            }
            model.put("users", data);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            logger.error("Error fetching data from database", e);
            model.put("error", "Error fetching data from database");
        }
        return new ModelAndView("AccountTable.hbs", model);
    }

    @GET("/TransactionTable")
    public ModelAndView displayTransactiontable() {
        Map<String, Object> model = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transactions");
            ArrayList<Transaction> data = new ArrayList<>();
            while (rs.next()) {
                String withdrawAccount = rs.getString("withdrawAccount");
                String depositAccount = rs.getString("depositAccount");
                String timestamp = rs.getString("time_stamp");
                String id = rs.getString("transactionId");
                double amount = rs.getDouble("amount");
                String currency = rs.getString("currency");
                Transaction transaction = new Transaction(withdrawAccount, depositAccount, timestamp, id, amount, currency);
                data.add(transaction);
            }
            model.put("TransactionRequests", data);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            logger.error("Error fetching data from database", e);
            model.put("error", "Error fetching data from database");
        }
        return new ModelAndView("TransactionTable.hbs", model);
    }

}
