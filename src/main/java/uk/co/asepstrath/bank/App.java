package uk.co.asepstrath.bank;

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
        /*
        Now we set up our controllers and their dependencies
         */
        DataSource ds = require(DataSource.class);
        Logger log = getLog();
        mvc(new Controller(ds, log));
        /*
        Finally we register our application lifecycle methods
         */
        onStarted(() -> onStart());
        onStop(() -> onStop());
    }

    public static void main(final String[] args) {
        runApp(args, App::new);
    }
    /*
    This function will be called when the application starts up,
    it should be used to ensure that the DB is properly setup
     */
    static ArrayList<Account> acc = new ArrayList<Account>();
    public void onStart() {
        Logger log = getLog();
        log.info("Starting Up...");
        DataSource ds = require(DataSource.class);
        Controller con = new Controller(ds,log);
        ArrayList<Account> acc = con.fetchData();
        // Open Connection to DB
        try (Connection connection = ds.getConnection()) {
            //Populate The Database
            String sql = "CREATE TABLE IF NOT EXISTS accounts (\n"
                    + " id varchar(50) PRIMARY KEY,\n"
                    + " name text,\n"
                    + " balance decimal,\n"
                    + " accountType text,\n"
                    + " currency text);";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
            }
            sql = "INSERT INTO accounts (id, name, balance, accountType, currency) "
                    + "VALUES (?,?,?,?,?)";
            PreparedStatement prep = connection.prepareStatement(sql);
            for(Account account : acc) {
                prep.setString(1, account.getId());
                prep.setString(2, account.getName());
                prep.setDouble(3, account.getBalance());
                prep.setString(4, account.getAccountType());
                prep.setString(5, account.getCurrency());
                prep.executeUpdate();
            }
        } catch (SQLException e) {
            log.error("Database Creation Error", e);
        }
    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

    public static ArrayList<Account> displayAccounts(){
        return acc;
    }
}
