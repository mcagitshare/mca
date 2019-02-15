package com.mca.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.mca.Adapter.GroupRecyclerAdapter;
import com.mca.Adapter.SimpleDividerItemDecoration;
import com.mca.Application.DemoApplication;
import com.mca.R;
import com.mca.Realm.RealmController;
import com.mca.Utils.Utils;

import io.realm.Realm;
import io.realm.RealmChangeListener;

@SuppressLint("ValidFragment")
public class GroupsFragment extends Fragment {

    RealmController realmController;
    RecyclerView recyclerView;
    GroupRecyclerAdapter adapter;
    JobManager jobManager;

    TextView noContacts;
    FragmentActivity activity;

    public GroupsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contacts_fragment, container, false);

        activity = getActivity();

        recyclerView = view.findViewById(R.id.rv_contacts);
        jobManager = DemoApplication.getInstance().getJobManager();
        noContacts = view.findViewById(R.id.no_contacts);
        realmController = RealmController.with(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new GroupRecyclerAdapter(this, realmController.getContacts());
        realmController.getRealm().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                if (realmController.getContacts() != null && realmController.getContacts().size() > 0) {
                    noContacts.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    noContacts.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void deleteGroupData(String itemId) {
        realmController.deleteGroupData(itemId);
    }

    public void editGroup(String id, Boolean readStatus, boolean accRej) {
        realmController.editGroup(id, readStatus, accRej);
        Utils.hideKeyboard(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
