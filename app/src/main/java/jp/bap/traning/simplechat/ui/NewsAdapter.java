package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.utils.Common;

public class NewsAdapter extends RecyclerView.Adapter {
    private ArrayList<News> newsArrayList;
    private Context mContext;

    public NewsAdapter(Context mContext, ArrayList<News> newsArrayList) {
        this.newsArrayList = newsArrayList;
        this.mContext = mContext;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.view_holder_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News mNews = newsArrayList.get(position);
        NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
        newsViewHolder.txtName.setText(mNews.getUser().getFirstName() + " " + mNews.getUser().getLastName());
        newsViewHolder.txtName2.setText(mNews.getUser().getFirstName() + " " + mNews.getUser().getLastName());
        newsViewHolder.txtDescription.setText(mNews.getDescription());
        Common.setImage(mContext, mNews.getImageView(), newsViewHolder.imageView);
        setAvatar(mNews.getUser().getId(), newsViewHolder.avatar);
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        AppCompatTextView txtName, txtName2;
        AppCompatImageView imageView;
        AppCompatTextView txtDescription;
        AppCompatImageButton imageButtonLike;
        AppCompatImageButton imageButtonComment;
        AppCompatImageButton imageButtonShare;

        public NewsViewHolder(View itemView) {
            super(itemView);
            addControls();
            addEvents();
        }

        private void addEvents() {
            imageView.setOnClickListener(view -> {
                News mNews = newsArrayList.get(getAdapterPosition());
                FullScreenImageActivity_.intent(mContext).urlImage(mNews.getImageView()).start();
            });

            imageButtonShare.setOnClickListener(view -> {
                News mNews = newsArrayList.get(getAdapterPosition());
                Message mMessage = new Message(mNews.getImageView(), Common.getUserLogin().getId(), -1, Common.typeImage);
                SharingMessageActivity_.intent(imageView.getContext()).message(mMessage).start();
            });
        }

        private void addControls() {
            avatar = itemView.findViewById(R.id.mAvatarNews);
            txtName = itemView.findViewById(R.id.txtName);
            txtName2 = itemView.findViewById(R.id.txtName2);
            imageView = itemView.findViewById(R.id.imgNews);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            imageButtonLike = itemView.findViewById(R.id.btnLike);
            imageButtonShare = itemView.findViewById(R.id.btnShare);
            imageButtonComment = itemView.findViewById(R.id.btnComment);
        }
    }

    private void setAvatar(int id, CircleImageView mAvatar) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(mContext).load(new UserDAO().getUser(id).getAvatar()).apply(options).into(mAvatar);
    }
}
