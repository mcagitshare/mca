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
import com.mca.Activity.ContactListActivity;
import com.mca.Fragment.GroupsFragment;
import com.mca.Model.Group;
import com.mca.R;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class GroupRecyclerAdapter extends RealmRecyclerViewAdapter<Group, GroupRecyclerAdapter.MyViewHolder> {

    GroupsFragment mActivity;

    public GroupRecyclerAdapter(GroupsFragment activity,
                                OrderedRealmCollection<Group> data) {
        super(data, true);
        this.mActivity = activity;
    }

    public void refreshData() {
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Group obj = getData().get(position);

        holder.item_name.setText(obj.getName());

        Glide.with(mActivity.getActivity())
                .load(obj.getImage())
                .apply(new RequestOptions()
                        .placeholder(null)
                        .error(null))
                .into(holder.image);

        if (obj.getId() != null) {
            holder.item_number.setText(obj.getPhone() + "");
        }

        holder.item_unread_count.setVisibility(View.GONE);

        Boolean read = obj.getReadStatus();
        if (!read) {
            holder.edit.setImageResource(R.mipmap.new_message);
        } else
            holder.edit.setVisibility(View.GONE);


        holder.ll_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivity.editGroup(obj.getId(), true, obj.getAccRej());

                Intent intent = new Intent(mActivity.getActivity(), ContactListActivity.class);
                intent.putExtra("Name", "Group Name");
                intent.putExtra("groupId", obj.getGroupId());
                intent.putExtra("name", obj.getName());
                intent.putExtra("phone", obj.getPhone());
                intent.putExtra("image", obj.getImage());
                intent.putExtra("id", obj.getId());
                intent.putExtra("type", 103);
                mActivity.startActivity(intent);
            }
        });

        holder.ll_acc_rej.setVisibility(View.GONE);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView item_name, item_number, item_unread_count;
        private ImageView edit, accept, reject;
        private CircleImageView image;
        private LinearLayout ll_header, ll_acc_rej;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.iv_item);
            item_name = view.findViewById(R.id.item_name);
            item_number = view.findViewById(R.id.item_number);
            edit = view.findViewById(R.id.iv_edit);

            ll_acc_rej = view.findViewById(R.id.ll_acc_rej);
            ll_header = view.findViewById(R.id.ll_header);

            accept = view.findViewById(R.id.iv_accept);
            reject = view.findViewById(R.id.iv_reject);
            item_unread_count = view.findViewById(R.id.item_unread_count);
        }
    }
}