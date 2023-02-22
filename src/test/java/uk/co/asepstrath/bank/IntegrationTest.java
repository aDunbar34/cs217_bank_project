package uk.co.asepstrath.bank;

import uk.co.asepstrath.bank.App;
import io.jooby.JoobyTest;
import io.jooby.StatusCode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest(App.class)
public class IntegrationTest {

    static OkHttpClient client = new OkHttpClient();

    @Test
    public void shouldDisplay(int serverPort) throws IOException {
        Request req = new Request.Builder()
                .url("http://localhost:" + serverPort+"/accounts")
                .build();

        try (Response rsp = client.newCall(req).execute()) {
            assertEquals("[Account Name: Rachel Balance:Â£ 50.0, Account Name: Monica Balance:Â£ 1000.0, Account Name: Phoebe Balance:Â£ 76.0, Account Name: Joey Balance:Â£ 23.9, Account Name: Chandler Balance:Â£ 3.0, Account Name: Ross Balance:Â£ 54.32]", rsp.body().string());
            assertEquals(StatusCode.OK.value(), rsp.code());
        }
    }
}
