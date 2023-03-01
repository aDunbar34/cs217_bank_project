package uk.co.asepstrath.bank;


import com.google.gson.Gson;
import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotations.*;
import io.jooby.exception.StatusCodeException;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kong.unirest.Unirest;
import org.graalvm.compiler.word.Word;
import org.h2.util.json.JSONObject;
import org.slf4j.Logger;
import io.swagger.*;
import java.lang.annotation.Repeatable;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.math.BigDecimal;

@Path("/accounts")
public class Controller {

    private final DataSource dataSource;
    private final Logger logger;

    public Controller(DataSource ds, Logger log) {
        dataSource = ds;
        logger = log;
    }

    @GET("/Data")
    @ApiResponses({@ApiResponse(message = "Success", code = 200)
    })
    @ApiOperation(value = "Display all accounts", notes = "Display all accounts in bank")


    public ArrayList<Account> accounts() {
        return App.displayAccounts();
    }

    @GET("/JSON")
    public String displayJSON(){
        String json = new Gson().toJson(App.displayAccounts());
        return json;
    }

    public ArrayList<Account> fetchData() {
        String jsonResult = String.valueOf(Unirest.get("https://api.asep-strath.co.uk/api/Team1/accounts") //get request for all accounts data
                .asJson()
                .getBody());
        return parseJson(jsonResult);
    }

    public ArrayList<Account> parseJson(String responseBody) {
        ArrayList<Account> accounts = new ArrayList<>();    //array list to store accounts
        JSONArray accountsData = new JSONArray(responseBody);   //adds account data from parameter to a JSON array
        for (int i = 0; i < accountsData.length(); i++) {       //adds account data from JSON array to an Array of objects (Accounts)
            JSONObject accountData = accountData.getJSONObject(i);
            accounts.add(new Account(accountData.toString("id"),
                    accountData.toString("name"),
                    accountData.getBigDecimal("balance"),
                    accountData.toString("accountType"),
                    accountData.toString("currency")));
        }
        return accounts;
    }




    @GET("/table")
    public ModelAndView displaytable(){

        Map<String, Object> model = new HashMap<>();
        ArrayList <Account> data = App.displayAccounts();
        model.put("users", data);
        return new ModelAndView("AccountTable.hbs", model);

    }
}
