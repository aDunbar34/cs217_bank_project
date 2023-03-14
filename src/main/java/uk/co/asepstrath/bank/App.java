package uk.co.asepstrath.bank;

import io.jooby.ModelAndView;
import io.jooby.OpenAPIModule;
import uk.co.asepstrath.bank.example.ExampleController;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;

import java.util.ArrayList;

public class App extends Jooby {
    {
        install(new HandlebarsModule());
    }


    private static ArrayList <Account> accs  = new ArrayList<>();

    private static ArrayList<Transaction> transactions = new ArrayList<>();

    {
        /*
        This section is used for setting up the Jooby Framework modules
         */
        install(new UniRestExtension());
        install(new HandlebarsModule());
        install(new HikariModule("mem"));
        install(new OpenAPIModule());

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
    public void onStart() {
        Logger log = getLog();
        log.info("Starting Up...");

        // Fetch DB Source
        DataSource ds = require(DataSource.class);
        Controller controller = new Controller(ds,log);
        accs = controller.fetchAccountData();
        transactions = controller.fetchTransactionData();

        // Populate array with data
        /*dataset.add(new Account("1","Rachel", BigDecimal.valueOf(50.00)));
        dataset.add(new Account("Monica", BigDecimal.valueOf(100.00)));
        dataset.add(new Account("Phoebe", BigDecimal.valueOf(76.00)));
        dataset.add(new Account("Joey", BigDecimal.valueOf(23.90)));
        dataset.add(new Account("Chandler", BigDecimal.valueOf(3.00)));
        dataset.add(new Account("Ross", BigDecimal.valueOf(54.32)));
        System.out.println(dataset);*/


        // Open Connection to DB
        try (Connection connection = ds.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE `Example` (`Key` varchar(255),`Value` varchar(255))");
            stmt.executeUpdate("INSERT INTO Example " + "VALUES ('WelcomeMessage', 'Welcome to A Bank')");
        } catch (SQLException e) {
            log.error("Database Creation Error",e);
        }

        try(Connection connection = ds.getConnection()){
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS accounts (\n"
                    + " id varchar(50) PRIMARY KEY,\n"
                    + " name text NOT NULL,\n"
                    + " balance decimal NOT NULL,\n"
                    + " accountType text NOT NULL,\n"
                    + " currency text NOT NULL);";
            statement.execute(sql);
            sql = "INSERT INTO accounts (id, name, balance, accountType, currency)" +
                    "VALUES (?,?,?,?,?)";
            PreparedStatement prepared = connection.prepareStatement(sql);
            for(int i = 0; i < accs.size(); i++){
                prepared.setString(1, accs.get(i).getId());
                prepared.setString(2, accs.get(i).getName());
                prepared.setBigDecimal(3, accs.get(i).getBalance());
                prepared.setString(4, accs.get(i).getCurrency());
                prepared.setString(5, accs.get(i).getAccountType());
                prepared.executeUpdate();
            }
            prepared.close();
            statement.close();
        } catch(SQLException e) {
            log.error("Cannot Create Database", e );
        }
        try(Connection connection = ds.getConnection()){
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS transactions (\n"
                    + " withdrawAccount varchar(50),\n"
                    + " depositAccount varchar(50),\n"
                    + " timestamp text,\n"
                    + " id varchar(50) PRIMARY KEY,\n"
                    + " amount double ,\n"
                    + " currency text, \n"
                    + " status integer NOT NULL);";
            statement.execute(sql);
            sql = "INSERT INTO transactions (withdrawAccount, depositAccount, id, timestamp, amount, currency, status)" +
                    "VALUES (?,?,?,?,?,?,?)";
            PreparedStatement prepared = connection.prepareStatement(sql);
            for(int i = 0; i < transactions.size(); i++){
                prepared.setString(1, transactions.get(i).getWithdrawAcc());
                prepared.setString(2, transactions.get(i).getDepositAcc());
                prepared.setString(3, transactions.get(i).getTransactionID());
                prepared.setString(4, transactions.get(i).getTimestamp());
                prepared.setDouble(5, transactions.get(i).getAmount());
                prepared.setString(6, transactions.get(i).getCurrency());
                prepared.setInt(7, transactions.get(i).getStatus());
                prepared.executeUpdate();
            }
        } catch(SQLException e) {
            log.error("Cannot Create Database", e );
        }
        controller.removeFraudulentTransactions();
    }
    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

    public static ArrayList<Account> getAccounts() {
        return accs;
    }

    public static ArrayList<Transaction> displayTransactions() {
        return transactions;
    }
}
