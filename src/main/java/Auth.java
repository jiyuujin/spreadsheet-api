import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;

import java.io.File;
import java.util.Arrays;
import java.util.List;

// アクセス修飾子無しはパッケージプライベート
class Auth {
    // クラス変数、定数
    private static final String APPLICATION_NAME = "SpreadsheetJava";
    private static final List<String> SCOPES = Arrays.asList(
            "https://docs.google.com/feeds",
            "https://spreadsheets.google.com/feeds"
    );
    private static final String ACCOUNT_P12_ID = "googleoauth2@spreadsheet-java-212606.iam.gserviceaccount.com";
    private static final File P12FILE = new File("src/main/resources/spreadsheet-java.p12");

    /**
     * インスタンスメソッド
     * SpreadsheetServiceオブジェクトを取得する
     * @return SpreadsheetServiceオブジェクト
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    SpreadsheetService getService() throws Exception {
        SpreadsheetService service = new SpreadsheetService(APPLICATION_NAME);
        service.setProtocolVersion(SpreadsheetService.Versions.V3);

        Credential credential = authorize();
        service.setOAuth2Credentials(credential);

        return service;
    }

    /**
     * クラスメソッド
     * Google Oauth2認証を使う (事前にCloud Consoleで設定したサービスアカウントを指定のスプレッドシートに紐付ける)
     * @return GoogleCredentialオブジェクト
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    private static Credential authorize() throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(ACCOUNT_P12_ID)
                .setServiceAccountPrivateKeyFromP12File(P12FILE)
                .setServiceAccountScopes(SCOPES)
                .build();

        boolean ret = credential.refreshToken();
        System.out.println("RefreshToken: " + ret);

        if (credential != null) {
            System.out.println("AccessToken: " + credential.getAccessToken());
        }

        return credential;
    }
}
