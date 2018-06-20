package jp.bap.traning.simplechat.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.ChatService;

/**
 * Created by dungpv on 6/13/18.
 */

public class Common {
    public static final String URL_SERVER = "http://18.216.126.225:3000";
    public static final String ACTION_SOCKET_EVENT = "action.socket.event";
    public static final int REQUEST_LOGIN = 100;
    private static final String TAG = "Common";

    public static void connectToServerSocket(Context context, String host, int token) {
        if (ChatService.getChat() == null) {
            Intent i = new Intent(context, ChatService.class);
            i.putExtra("host", host);
            i.putExtra("token", token);
            context.startService(i);
        }else{
            Log.d(TAG, "connectToServerSocket: Service started ");
        }
    }

    public static Room getRoomWithUser(int userId) {
        return new RoomDAO().getRoomWithUser(userId);
    }

    public static String getNameRoomFromRoomId(int roomId) {
        String nameRoom = "";
        Room room = new RoomDAO().getRoomFromRoomId(roomId);
        if(room!=null) {
            for (User user : room.getUsers()) {
                if (user.getId() != SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class)) {
                    nameRoom = user.getFirstName() + " " + user.getLastName() + "(" + user.getId() + ")";
                    break;
                }
            }
        }
        return nameRoom;
    }
}
