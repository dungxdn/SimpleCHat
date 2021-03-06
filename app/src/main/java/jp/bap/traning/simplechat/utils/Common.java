package jp.bap.traning.simplechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

import java.util.ArrayList;
import java.util.List;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.RoomData;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.response.AddRoomResponse;
import jp.bap.traning.simplechat.service.ChatService;

import static jp.bap.traning.simplechat.utils.SharedPrefs.KEY_SAVE_ID;


public class Common {
    public static final String URL_SERVER = "http://18.216.126.225:3000";
    public static final String ACTION_SOCKET_EVENT = "action.socket.event";
    public static final int REQUEST_LOGIN = 100;
    public static final int REQUEST_LOGOUT = 101;
    public static final String REQUEST_LOGOUT_KEY = "KEY_LOGOUT";
    public static final int STATUS_SUCCESS = 200;
    public static final int TYPE_GROUP_TWO_PEOPLE = 0;
    public static final int TYPE_GROUP_MORE_PEOPLE = 1;
    private static final String TAG = "Common";
    public static final String typeText = "text";
    public static final String typeImage = "image";
    public static final String typeLink = "link";
    public static final String STUN_URL = "stun:stunserver.org:3478";
    public static final int CALL_BUSY = 1;
    public static final int CALL_NO_ONE = 2;
    public static final String KEY_CHOOSE_LANGUAGE = "CHOOSE_LANGUAGE";
    public static final int REQUEST_CHOOSE_LANGUAGE_ACTIVITY = 102;
    public static ArrayList<User> usersOnline = new ArrayList<>();
    //    public static final int mMineId =
//            SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
    public static final int DEFAULT_VALUE_IF_NOT_EXITS_GROUP = 0;

    public static Room getRoomWithUser(int userId) {
        return new RoomDAO().getRoomWithUser(userId);
    }

    public static Room getFullRoomFromRoomId(int roomId) {
        Room room = new RoomDAO().getRoomFromRoomId(roomId);
        if (room != null) {
            if (room.getType() == 0) {
                for (User user : room.getUsers()) {
                    if (user.getId() != SharedPrefs.getInstance()
                            .getData(SharedPrefs.KEY_SAVE_ID, Integer.class)) {
                        room.setRoomName(user.getFirstName()
                                + " "
                                + user.getLastName());
                        room.setAvatar(user.getAvatar());
                        break;
                    }
                }
            }
        }
        return room;
    }

    public static User getFriendFromRoom(Room mRoom) {
        if (mRoom != null) {
            for (User user : mRoom.getUsers()) {
                if (user.getId() != Common.getUserLogin().getId()) {
                    return user;
                }
            }
        }
        return null;
    }

    public static User getUserLogin() {
        int id = SharedPrefs.getInstance().getData(KEY_SAVE_ID, Integer.class);
        //get user from Realm
        return new UserDAO().getUser(id);
    }

    public static void selectImage(Context context) {
        ImagePicker.create((Activity) context).returnMode(ReturnMode.GALLERY_ONLY).single().start();
    }

    public static boolean checkValidUser(ArrayList<User> users) {
        int i = 0;
        while (i < users.size()) {
            if (users.get(i).getId() == Common.getUserLogin().getId()) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token
        // from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setImage(Context mContext, String linkImage, ImageView circleImageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.default_image_news);
        options.error(R.drawable.default_image_news);
        Glide.with(mContext).load(linkImage).apply(options).into(circleImageView);
    }

    public static void setAvatar(Context mContext, int id, CircleImageView mAvatar) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(mContext).load(new UserDAO().getUser(id).getAvatar()).apply(options).into(mAvatar);
    }

    public static int isRoomExits(List<Room> roomList, List<Integer> idList) {
        for (Room r : roomList) {
            if (r.getUsers().size() == idList.size()) {
                if (isListIdExitInRoom(r, idList)) return r.getRoomId();
            }
        }
        return DEFAULT_VALUE_IF_NOT_EXITS_GROUP;
    }

    public static boolean isListIdExitInRoom(Room room, List<Integer> listId) {
        for (Integer i : listId) {
            if (!isIdExitsInListUser(room.getUsers(), i)) return false;
        }
        return true;
    }

    public static boolean isIdExitsInListUser(List<User> userList, int id) {
        for (User u : userList) {
            if (id == u.getId()) return true;
        }
        return false;
    }


    public static void insertOrUpdateRoomToRealm(AddRoomResponse result, RealmList<User> realmList) {
        Room mRoom = new Room();
        RoomData mRoomData = result.getData();
        mRoom.setRoomId(mRoomData.getRoomId());
        mRoom.setType(mRoomData.getType());
        mRoom.setUsers(realmList);
        mRoom.setRoomName(mRoomData.getRoomName());
        new RoomDAO().insertOrUpdate(mRoom);
        realmList.clear();
    }

    public static <T> ArrayList<T> covertFromRealmListToArrayList(RealmList<T> mRealmList) {
        ArrayList<T> mArrayList = new ArrayList<>();
        for (int i = 0; i < mRealmList.size(); i++) {
            mArrayList.add(mRealmList.get(i));
        }
        return mArrayList;
    }

    public static <T> RealmList<T> covertFromArrayListToRealmList(ArrayList<T> mArrayList) {
        RealmList<T> mRealmList = new RealmList<>();
        for (int i = 0; i < mArrayList.size(); i++) {
            mRealmList.add(mArrayList.get(i));
        }
        return mRealmList;
    }

    public static boolean checkUserOnline(int userID) {
        for (int i = 0; i < usersOnline.size(); i++) {
            if (userID == usersOnline.get(i).getId()) {
                return true;
            }
        }
        return false;
    }
}
