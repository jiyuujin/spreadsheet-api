import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.spreadsheet.WorksheetQuery;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

import java.net.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import slack.Message;

public class Connect {
    // クラス変数、定数
    private static final String SPREADSHEET_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
    private static URL SPREADSHEET_FEED_URL;

    public static void main(String[] args) {
        try {
            // URLを決定する
            SPREADSHEET_FEED_URL = new URL(SPREADSHEET_URL);
        } catch (MalformedURLException e) {
            // スーパークラスに IOException さらに Exception が存在 Checked Exception 必ず実装する必要がある
            e.printStackTrace();
        }

        try {
            System.out.println("処理開始します...");

            // データを読み込む
//            loadData();

            // データを書き込む
//            writeData();
        } catch (Exception e) {
            // Exception は Checked Exception 必ず実装する必要がある
            e.printStackTrace();
        } finally {
            // 必ず通したい処理があれば finally ブロックで
            System.out.println("処理完了しました");
        }
    }

    /**
     * クラスメソッド
     * スプレッドシート一覧を読み込む
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    private static void loadData() throws Exception {
        // Authのインスタンスを作成する
        Auth auth = new Auth();

        // SpreadsheetServiceオブジェクトを取得する
        SpreadsheetService service = auth.getService();

        List<SpreadsheetEntry> list = findAllSpreadsheets(service);
        // インスタンスオブジェクトは、クラス名@ハッシュコードを出力します
        System.out.println(list);
    }

    /**
     * クラスメソッド
     * スプレッドシートのデータに書き込む
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    private static void writeData() throws Exception {
        // Authのインスタンスを作成する
        Auth auth = new Auth();

        // SpreadsheetServiceオブジェクトを取得する
        SpreadsheetService service = auth.getService();

        // スプレッドシートを指定する
        String spreadsheetName = "テストスプレッドシート";
        SpreadsheetEntry spreadsheetEntry = findSpreadsheetByName(service, spreadsheetName);

        // ワークシートを指定する
        String worksheetName = "テストシート";
        WorksheetEntry worksheetEntry = findWorksheetByName(service, spreadsheetEntry, worksheetName);

        // 追加するデータを Map で宣言 Hashmap でインスタンスを作成する
        Map<String, String> insertValues = new HashMap<>();
        insertValues.put("Name", "Aki");
        insertValues.put("Age", "33");
        insertValues.put("Skill", "Traveling");

        insertDataRow(service, worksheetEntry, insertValues);

        // 更新するデータを Map で宣言 Hashmap でインスタンスを作成する
        Map<String, String> updateValues = new HashMap<>();
        updateValues.put("Name", "Aki");
        updateValues.put("Age", "36");
        updateValues.put("Skill", "Traveling around the world");

        updateDataRow(service, worksheetEntry, 0, updateValues);
    }

    /**
     * クラスメソッド
     * サービスアカウントと紐付いているスプレッドシート一覧を取得する
     * @param service SpreadsheetServiceオブジェクト
     * @return サービスアカウントと紐付いているスプレッドシート一覧
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    private static List<SpreadsheetEntry> findAllSpreadsheets(SpreadsheetService service) throws Exception {
        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);

        List<SpreadsheetEntry> list = feed.getEntries();

        for (SpreadsheetEntry spreadsheet : list) {
            System.out.println("title: " + spreadsheet.getTitle().getPlainText());
        }

        return list;
    }

    /**
     * クラスメソッド
     * 指定のスプレッドシートのデータを取得する
     * @param service SpreadsheetServiceオブジェクト
     * @param spreadsheetName 指定のスプレッドシート名
     * @return 指定のスプレッドシートのデータ
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    private static SpreadsheetEntry findSpreadsheetByName(SpreadsheetService service, String spreadsheetName) throws Exception {
        SpreadsheetQuery query = new SpreadsheetQuery(SPREADSHEET_FEED_URL);
        query.setTitleQuery(spreadsheetName);
        SpreadsheetFeed feed = service.query(query, SpreadsheetFeed.class);
        SpreadsheetEntry spreadsheetEntry = null;
        if (feed.getEntries().size() > 0) {
            spreadsheetEntry = feed.getEntries().get(0);
        }
        return spreadsheetEntry;
    }

    /**
     * クラスメソッド
     * 指定のワークシートのデータを取得する
     * @param service SpreadsheetServiceオブジェクト
     * @param spreadsheetEntry 指定のスプレッドシートのデータ
     * @param worksheetName 指定のワークシート名
     * @return 指定のワークシートのデータ
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    private static WorksheetEntry findWorksheetByName(SpreadsheetService service, SpreadsheetEntry spreadsheetEntry, String worksheetName) throws Exception {
        WorksheetQuery query = new WorksheetQuery(spreadsheetEntry.getWorksheetFeedUrl());
        query.setTitleQuery(worksheetName);
        WorksheetFeed feed = service.query(query, WorksheetFeed.class);
        WorksheetEntry worksheetEntry = null;
        if (feed.getEntries().size() > 0){
            worksheetEntry = feed.getEntries().get(0);
        }
        return worksheetEntry;
    }

    /**
     * クラスメソッド
     * データを追加する
     * @param service SpreadsheetServiceオブジェクト
     * @param worksheetEntry 指定のワークシートのデータ
     * @param values 追加するデータ
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    private static void insertDataRow(SpreadsheetService service, WorksheetEntry worksheetEntry, Map<String, String> values) throws Exception {
        ListEntry dataRow = new ListEntry();

        values.forEach((title,value)->{
            dataRow.getCustomElements().setValueLocal(title, value);
        });

        System.out.println(worksheetEntry);
        URL listFeedUrl = worksheetEntry.getListFeedUrl();
        service.insert(listFeedUrl, dataRow);

        Message message = new Message();
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        message.send("データを追加しました " + dateTime.format(formatter));
    }

    /**
     * クラスメソッド
     * データを更新する
     * @param service SpreadsheetServiceオブジェクト
     * @param worksheetEntry 指定のワークシートのデータ
     * @param rowNum 行数
     * @param values 更新するデータ
     * @throws Exception Checked Exception は必ず実装する必要がある
     */
    private static void updateDataRow(SpreadsheetService service, WorksheetEntry worksheetEntry, int rowNum, Map<String, String> values) throws Exception {
        URL listFeedUrl = worksheetEntry.getListFeedUrl();
        ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

        ListEntry row = listFeed.getEntries().get(rowNum);

        values.forEach((title,value) -> {
            row.getCustomElements().setValueLocal(title, value);
        });

        row.update();

        Message message = new Message();
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        message.send("データを更新しました " + dateTime.format(formatter));
    }
}
