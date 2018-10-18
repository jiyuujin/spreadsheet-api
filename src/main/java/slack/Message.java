package slack;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import util.Log;

import java.io.IOException;

public class Message {
    // クラス変数、定数
    private static final String botToken = "xoxb-294501866708-398667584307-LyQ7KyVOQ6PiOeqTY4Pi7TYS";

    /**
     * インスタンスメソッド
     * Slackセッションを利用して、メッセージを送信する
     * @param description 送信内容
     * @throws IOException Checked Exception は必ず実装する必要がある
     */
    public void send(String description) throws IOException {
        SlackSession session = SlackSessionFactory.createWebSocketSlackSession(botToken);

        session.connect();

        // Messageのインスタンスを作成する
        Message message = new Message();
        // メッセージを送信する
        message.sendMessage(session, description);

        session.disconnect();
    }

    /**
     * インスタンスメソッド
     * メッセージを送信する
     * @param session SlackSessionオブジェクト
     * @param description 送信内容
     */
    private void sendMessage(SlackSession session, String description) {
        // Messageのインスタンスを作成する
        Message message = new Message();
        // generalチャンネルにメッセージを送信する
        message.sendToGeneralChannel(session, description);

        Log.printLog(true);
    }

    /**
     * インスタンスメソッド
     * generalチャンネルにメッセージを送信する
     * @param session SlackSessionオブジェクト
     * @param description 送信内容
     */
    private void sendToGeneralChannel(SlackSession session, String description) {
        final String channelName = "general";

        SlackChannel channel = session.findChannelByName(channelName);

        // generalチャンネルに送信する
        session.sendMessage(channel, description);
    }
}
