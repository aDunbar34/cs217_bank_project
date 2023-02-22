package uk.co.asepstrath.bank;

import com.google.gson.Gson;
import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotations.*;
import io.jooby.exception.StatusCodeException;
import kong.unirest.Unirest;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Path("/accounts")
public class Controller {

    @GET
    public ArrayList<Account> accounts() {

        return App.displayAccounts();
    }

    @GET("/JSON")
    public String displayJSON(){
        App app = new App();
        app.onStart();
        String json = new Gson().toJson(App.displayAccounts());
        return json;
    }

    @GET("/table")
    public ModelAndView displaytable(){
     Map<String, Object> model = new HashMap<>();
     ArrayList <Account> data = App.displayAccounts();
     model.put("users", data);
     return new ModelAndView("AccountTable.html", model);
    }




}




