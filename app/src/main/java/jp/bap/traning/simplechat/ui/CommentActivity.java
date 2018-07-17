package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Comment;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EActivity(R.layout.activity_comment)
public class CommentActivity extends BaseActivity {

    private ArrayList<Comment> listComment;
    private CommentAdapter commentAdapter;
    @ViewById
    RecyclerView recyclerViewComment;
    @ViewById
    CircleImageView mAvatarComment;
    @ViewById
    AppCompatEditText edtComment;
    @ViewById
    AppCompatImageButton imgButtonSendComment;
    @ViewById
    ProgressBar mProgressBar;
    @ViewById
    CustomToolbar_ mToolbar;

    @Extra
    News mNews;

    @Override
    public void afterView() {
        setupToolbar();
        init();
        addEvents();
    }

    private void setupToolbar() {
        mToolbar.getCallButton().setVisibility(View.GONE);
        mToolbar.getCallVideoButton().setVisibility(View.GONE);
        mToolbar.setTitle("Comment");
        mToolbar.getBackButton().setOnClickListener(view -> finish());
        mToolbar.getSettingButton().setVisibility(View.GONE);
        mToolbar.setBackgroundColor(Color.WHITE);
    }

    private void init() {
        Common.setAvatar(this, Common.getUserLogin().getId(), mAvatarComment);
        mProgressBar.setVisibility(View.GONE);
        listComment = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, listComment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewComment.setLayoutManager(mLayoutManager);
        recyclerViewComment.setItemAnimator(new DefaultItemAnimator());
        recyclerViewComment.setAdapter(commentAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this , 1);
        recyclerViewComment.addItemDecoration(mDividerItemDecoration);
        commentAdapter.notifyDataSetChanged();
    }

    private void addEvents() {
        recyclerViewComment.setOnTouchListener((view, motionEvent) -> {
            Common.hideKeyboard((Activity) view.getContext());
            return false;
        });
    }

    @Click(R.id.imgButtonSendComment)
    public void sendComment() {
        if (edtComment.getText().toString().isEmpty()) {
            Toast.makeText(this, "Say your feeling...", Toast.LENGTH_SHORT).show();
        } else {
            listComment.add(0, new Comment(Common.getUserLogin(), edtComment.getText().toString()));
            commentAdapter.notifyDataSetChanged();
            recyclerViewComment.smoothScrollToPosition(0);
            edtComment.setText("");
        }
    }

}