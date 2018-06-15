package jp.bap.traning.simplechat.utils;

import android.content.Context;
import android.content.Intent;

import jp.bap.traning.simplechat.service.ChatService;

/**
 * Created by dungpv on 6/13/18.
 */

public class Common {
    public static final String URL_SERVER = "http://172.16.1.77:3000";
    public static final String ACTION_SOCKET_EVENT = "action.socket.event";
    public static final String TURN_URL = "stun:stun.l.google.com:19302";

    public static void connectToServerSocket(Context context, String host, int token) {
        if (ChatService.getChat() == null) {
            Intent i = new Intent(context, ChatService.class);
            i.putExtra("host", host);
            i.putExtra("token", token);
            context.startService(i);
        }
    }
}
