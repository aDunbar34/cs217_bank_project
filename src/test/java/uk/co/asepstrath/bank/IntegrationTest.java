package uk.co.asepstrath.bank;

import io.jooby.*;
import org.junit.jupiter.api.Assertions;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest(App.class)
public class IntegrationTest {
    static OkHttpClient client = new OkHttpClient();
    /*
    Test to ensure /table displays correctly
     */
    @Test
    public void shouldDisplay() {
        MockRouter router = new MockRouter(new App());
        MockContext context = new MockContext();

        router.get("/table", context, rsp -> {
            ModelAndView modelandview = (ModelAndView) rsp.value();
            Assertions.assertNotEquals(modelandview, null);
            String view = modelandview.getView();
            Assertions.assertEquals(view, "AccountTable.hbs");
        });

        router.get("/TransactionTable", context, rsp -> {
            ModelAndView modelandview = (ModelAndView) rsp.value();
            Assertions.assertNotEquals(modelandview, null);
            String view = modelandview.getView();
            Assertions.assertEquals(view, "TransactionTable.hbs");
        });

        router.get("/about", context, rsp -> {
            ModelAndView modelandview = (ModelAndView) rsp.value();
            Assertions.assertNotEquals(modelandview, null);
            String view = modelandview.getView();
            Assertions.assertEquals(view, "about.hbs");
        });

        router.get("/transactionDetails", context, rsp -> {
            ModelAndView modelandview = (ModelAndView) rsp.value();
            Assertions.assertNotEquals(modelandview, null);
            String view = modelandview.getView();
            Assertions.assertEquals(view, "TransactionData.hbs");
        });

        router.get("/", context, rsp -> {
            ModelAndView modelandview = (ModelAndView) rsp.value();
            Assertions.assertNotEquals(modelandview, null);
            String view = modelandview.getView();
            Assertions.assertEquals(view, "home.hbs");
        });
    }
    /*
    Test to ensure pages ran return the correct status code
    To add more simply Copy+Paste and replace the "/table" with whichever new page you wish to test
     */
    @Test
    void testStatusCode(int serverPort) throws IOException {
        Request req = new Request.Builder().url("http://localhost:" + serverPort + "/table").build();
        Response rsp = client.newCall(req).execute();
        assertEquals(StatusCode.OK.value(), rsp.code());

        req = new Request.Builder().url("http://localhost:" + serverPort + "/TransactionTable").build();
        rsp = client.newCall(req).execute();
        assertEquals(StatusCode.OK.value(), rsp.code());

        req = new Request.Builder().url("http://localhost:" + serverPort + "/transactionDetails").build();
        rsp = client.newCall(req).execute();
        assertEquals(StatusCode.OK.value(), rsp.code());

        req = new Request.Builder().url("http://localhost:" + serverPort + "/about").build();
        rsp = client.newCall(req).execute();
        assertEquals(StatusCode.OK.value(), rsp.code());

        req = new Request.Builder().url("http://localhost:" + serverPort + "/").build();
        rsp = client.newCall(req).execute();
        assertEquals(StatusCode.OK.value(), rsp.code());

    }
}
