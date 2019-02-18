package com.mca.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mca.Activity.ContactDetails;
import com.mca.Activity.MessageChatActivity;
import com.mca.Fragment.MessagesFragment;
import com.mca.Model.Message;
import com.mca.R;
import com.mca.Realm.RealmController;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class MessageRecyclerAdapter extends RealmRecyclerViewAdapter<Message, MessageRecyclerAdapter.MyViewHolder> {

    MessagesFragment mActivity;
    Realm realm;

    public MessageRecyclerAdapter(MessagesFragment activity,
                                  OrderedRealmCollection<Message> data, Realm realm) {
        super(data, true);
        this.mActivity = activity;
        this.realm = realm;
    }

    public void refreshData() {
        this.notifyDataSetChanged();
    }

    @Override
    public MessageRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new MessageRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MessageRecyclerAdapter.MyViewHolder holder, int position) {
        final Message obj = getData().get(position);

        holder.item_name.setText(obj.getDisplayMessage());
        holder.item_unread_count.setText("Unread - " + obj.getUnReadCount());

        Glide.with(mActivity.getActivity())
                .load(obj.getIcon())
                .apply(new RequestOptions()
                        .placeholder(null)
                        .error(null))
                .into(holder.image);

        if (obj.getId() != null) {
            holder.item_number.setText(obj.getId() + "");
        }

        Boolean read = obj.getReadStatus();
        if (!read) {
            holder.edit.setImageResource(R.mipmap.new_message);
        } else
            holder.edit.setVisibility(View.GONE);

        holder.ll_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivity.editMessage(obj.getId(), true, obj.getAccRej());
                RealmController.clearUnreadCountMessage(realm, obj.getId());

                Intent intent;
                if (obj.getType() != null && obj.getType().equals("normal")) {
                    intent = new Intent(mActivity.getActivity(), MessageChatActivity.class);
                } else {
                    intent = new Intent(mActivity.getActivity(), ContactDetails.class);
                }

                intent.putExtra("Name", "Message");
                intent.putExtra("name", obj.getDisplayMessage());
                intent.putExtra("option", obj.getOption());
                intent.putExtra("image", obj.getIcon());
                intent.putExtra("jsonMessage", obj.getMessageBody());
                intent.putExtra("id", obj.getId());
                intent.putExtra("type", 100);
                mActivity.startActivity(intent);
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView item_name, item_number, item_unread_count;
        private ImageView edit;
        private CircleImageView image;
        private LinearLayout ll_header;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.iv_item);
            item_name = view.findViewById(R.id.item_name);
            item_unread_count = view.findViewById(R.id.item_unread_count);
            item_number = view.findViewById(R.id.item_number);
            edit = view.findViewById(R.id.iv_edit);

            ll_header = view.findViewById(R.id.ll_header);
        }
    }
}
