package jp.bap.traning.simplechat.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.User;

/**
 * Created by Admin on 6/20/2018.
 */

public class FriendExpandLvAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<String> mHeaderGroup;
    private HashMap<String, ArrayList<User>> mDataChild;
    private Listener mListener;

    interface Listener {
        void onChat(User user);

        void onCallVideo(int userId);

        void onCallAudio(int userId);
    }


    public FriendExpandLvAdapter(Context mContext, ArrayList<String> mHeader, HashMap<String, ArrayList<User>> mDataChild, Listener listener) {
        this.mContext = mContext;
        this.mHeaderGroup = mHeader;
        this.mDataChild = mDataChild;
        mListener = listener;

    }

    @Override
    public int getGroupCount() {
        return mHeaderGroup.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mDataChild.get(mHeaderGroup.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return mHeaderGroup.get(i);
    }

    @Override
    public User getChild(int i, int i1) {
        return mDataChild.get(mHeaderGroup.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater li = LayoutInflater.from(mContext);
            view = li.inflate(R.layout.friend_group_item_layout, viewGroup, false);
        }
        AppCompatTextView tvGroupName = view.findViewById(R.id.mTvGroupName);
        AppCompatImageView btnUpDown = view.findViewById(R.id.btnUpDown);
        if (getChildrenCount(i) == 0 || i==0) {
            tvGroupName.setText(mHeaderGroup.get(i));
        } else {
            tvGroupName.setText(mHeaderGroup.get(i) + " (" + getChildrenCount(i) + ")");
        }
        if (b) {
            btnUpDown.setImageResource(R.drawable.ic_arrow_up);
        } else {
            btnUpDown.setImageResource(R.drawable.ic_arrow_down);
        }
        return view;

    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater li = LayoutInflater.from(mContext);
            view = li.inflate(R.layout.viewholder_friend, viewGroup, false);
        }
        CircleImageView mAvatar = view.findViewById(R.id.mImgAvatar);
        AppCompatTextView mUserName = view.findViewById(R.id.mTvUserName);
        AppCompatTextView mStatus = view.findViewById(R.id.mtvStatus);
        AppCompatImageButton mImgButtonCall = view.findViewById(R.id.mImgButtonCall);
        AppCompatImageButton mImgButtonCallVideo = view.findViewById(R.id.mImgButtonCallVideo);

        User user = mDataChild.get(mHeaderGroup.get(i)).get(i1);
        if(i == 0){
            mImgButtonCall.setVisibility(View.GONE);
            mImgButtonCallVideo.setVisibility(View.GONE);
        }

        mUserName.setText(user.getFirstName()+" "+user.getLastName());
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog mDialog = new Dialog(mContext);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setContentView(R.layout.dialog_detail_profile_layout);
                AppCompatTextView tvUsername = mDialog.findViewById(R.id.mTvUserName);
                AppCompatButton btnChat = mDialog.findViewById(R.id.mBtnChat);
                AppCompatButton btnCall = mDialog.findViewById(R.id.mBtnCall);
                AppCompatButton btnCallVideo = mDialog.findViewById(R.id.mBtnCallVideo);
                AppCompatButton mBtnEdit = mDialog.findViewById(R.id.mBtnEdit);
                LinearLayout lnContactFriend = mDialog.findViewById(R.id.lnContactFriend);

                if(i == 0){
                    mBtnEdit.setVisibility(View.VISIBLE);
                    lnContactFriend.setVisibility(View.GONE);
                } else{
                    mBtnEdit.setVisibility(View.GONE);
                    lnContactFriend.setVisibility(View.VISIBLE);
                }

                btnChat.setOnClickListener(view1 -> {
                    mListener.onChat(user);
                    mDialog.dismiss();
                });
                btnCall.setOnClickListener(view1 -> {
                    mListener.onCallAudio(user.getId());
                    mDialog.dismiss();
                });
                btnCallVideo.setOnClickListener(view1 -> {
                    mListener.onCallVideo(user.getId());
                    mDialog.dismiss();
                });
                tvUsername.setText(user.getFirstName() + " " + user.getLastName());
                mDialog.show();
            }
        });
        mImgButtonCall.setOnClickListener(view1 ->
                mListener.onCallAudio(user.getId())
            );
        mImgButtonCallVideo.setOnClickListener(view1 ->
                mListener.onCallVideo(user.getId())
        );
        return view;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}


