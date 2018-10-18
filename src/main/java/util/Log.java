package util;

public class Log {
    /**
     * 結果を表示する
     * @param resultFlag 成功したか、失敗したか (成功=true、失敗=false)
     */
    public static void printLog(Boolean resultFlag) {
        String text = "";

        if (resultFlag) {
            text = "成功しました";
        } else {
            text = "失敗しました";
        }

        System.out.println(text);
    }
}
