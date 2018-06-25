package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.presenter.message.MessagePresenter;
import jp.bap.traning.simplechat.presenter.message.MessageView;
import jp.bap.traning.simplechat.ui.BaseFragment;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_chat)
public class ChatFragment extends BaseFragment {
    private static final String TAG = "ChatFragment";
    @ViewById
    RecyclerView mRecyclerRoom;
    private ArrayList<Room> mListRoom;
    private ArrayList<Message> mListMessage;
    private ChatAdapter mChatAdapter;
    private MessagePresenter messagePresenter;


    @Override
    public void afterView() {
        init();

    }

    private void init() {
        mListRoom = new ArrayList<>();
        mListMessage = new ArrayList<>();
        mChatAdapter = new ChatAdapter(getContext(), mListRoom, mListMessage);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerRoom.setLayoutManager(mLayoutManager);
        mRecyclerRoom.setItemAnimator(new DefaultItemAnimator());
        mRecyclerRoom.setAdapter(mChatAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), 1);
        mRecyclerRoom.addItemDecoration(mDividerItemDecoration);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListRoom.clear();
        mListMessage.clear();

        for (Room room : new RoomDAO().getAllRoom()) {
            mListRoom.add(room);
            //Create MessagePresenter
            this.messagePresenter = new MessagePresenter(new MessageView() {
                @Override
                public void getAllMessage(ArrayList<Message> messagesList) {
                    Message newMessage = messagesList.get(0);
                    for (int i = 0; i < messagesList.size(); i++) {
                        if (messagesList.get(i).getId() > newMessage.getId()) {
                            newMessage = messagesList.get(i);
                        }
                    }
                    mListMessage.add(newMessage);

                }

                @Override
                public void errorGetAllMessage(int roomID) {
                    mListMessage.add(null);
                }
            }) ;
            //GetConverstation
            messagePresenter.getAllMessage(room.getRoomId());
        }

        Log.d(TAG, "onResume: mListMessage "+mListMessage.size());
        Log.d(TAG, "onResume: mListRoom "+mListRoom.size());
        mChatAdapter.notifyDataSetChanged();

    }


}
