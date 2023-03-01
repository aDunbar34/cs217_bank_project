package uk.co.asepstrath.bank;

import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import uk.co.asepstrath.bank.example.ExampleController;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class App<accList> extends Jooby {

    {
        /*
        This section is used for setting up the Jooby Framework modules
         */
        install(new UniRestExtension());
        install(new HandlebarsModule());
        install(new HikariModule("mem"));

        /*
        This will host any files in src/main/resources/assets on <host>/assets
        For example in the dice template (dice.hbs) it references "assets/dice.png" which is in resources/assets folder
         */
        assets("/assets/*", "/assets");
        assets("/service_worker.js","/service_worker.js");

        /*
        Now we set up our controllers and their dependencies
         */
        DataSource ds = require(DataSource.class);
        Logger log = getLog();

        mvc(new ExampleController(ds,log));
        mvc(new Controller());

        /*
        Finally we register our application lifecycle methods
         */
        onStarted(() -> onStart());
        onStop(() -> onStop());
    }



    public static void main(final String[] args) {
        runApp(args, App::new);
    }

    static ArrayList<Account> accList = new ArrayList<Account>();
    /*
    This function will be called when the application starts up,
    it should be used to ensure that the DB is properly setup
     */
    public void onStart() {
        Logger log = getLog();
        log.info("Starting Up...");

        // Fetch DB Source
        DataSource ds = require(DataSource.class);
        // Open Connection to DB
        try (Connection connection = ds.getConnection()) {
            Statement stmt = connection.createStatement();

            List list = Unirest.get("https://api.asep-strath.co.uk/api/team6/accounts").asJson().getBody().getArray().toList();
//            list.stream().map(acc -> {return new Account()}).collect(Collectors.toList())();
//            stmt.executeUpdate("CREATE TABLE `accountInfo` (id String PRIMARY KEY, accountName String, accountBalance BigDecimal, currency String, accountType String)");
//            stmt.executeUpdate("INSERT INTO accountInfo " + "VALUES ('WelcomeMessage', 'Welcome to A Bank')");
            Statement stmnt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS accounts (\n"
                    + " id text PRIMARY KEY,\n"
                    + " accountName text NOT NULL,\n"
                    + " accountBalance decimal NOT NULL,\n"
                    + " currency text NOT NULL,\n"
                    + " accountType text NOT NULL);";

            stmt.execute(sql);

//            for (Object i: arrayObject) {
//                String sql2 = "INSERT INTO employees (id, accountName, accountBalance, currency, accountType) "
//                        + "VALUES (?,?,?,?,?)"; // Note: the ?s are important
//                PreparedStatement prep = connection.prepareStatement(sql);
//                prep.setString(1, "gfjfdkgfdk");
//                prep.setString(2, "Bob");
//                prep.setDouble(3, 35000.00);
//                prep.setString(4, "Pounds");
//                prep.setString(5, "Savings");
//                prep.executeUpdate();
//            }
        } catch (SQLException e) {
            log.error("Database Creation Error",e);
        }

        accList.add(new Account("Rachel", 50.00));
        accList.add(new Account("Monica", 1000.00));
        accList.add(new Account("Phoebe", 76.00));
        accList.add(new Account("Joey", 23.90));
        accList.add(new Account("Chandler", 3.00));
        accList.add(new Account("Ross", 54.32));
    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

    public static ArrayList<Account> displayAccounts(){
        return accList;
    }
}
