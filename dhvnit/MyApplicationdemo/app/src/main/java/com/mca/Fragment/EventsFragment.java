package com.mca.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.mca.Activity.ContactListActivity;
import com.mca.Adapter.EventRecyclerAdapter;
import com.mca.Adapter.RecyclerAdapter;
import com.mca.Adapter.SimpleDividerItemDecoration;
import com.mca.Application.DemoApplication;
import com.mca.R;
import com.mca.Realm.RealmController;
import com.mca.Utils.Constants;
import com.mca.Utils.Utils;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class EventsFragment extends Fragment {

    RealmController realmController;
    RecyclerView recyclerView;
    EventRecyclerAdapter adapter;
    JobManager jobManager;
    TextView noContacts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_fragment, container, false);

        jobManager = DemoApplication.getInstance().getJobManager();
        recyclerView = view.findViewById(R.id.rv_events);
        noContacts = view.findViewById(R.id.no_contacts);
        realmController = RealmController.with(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new EventRecyclerAdapter(this, realmController.getEvents());
        realmController.getRealm().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                if (realmController.getEvents() != null && realmController.getEvents().size() > 0) {
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

    public void deleteEventData(String itemId) {
        realmController.deleteEventData(itemId);
    }

    public void editEvent(String id, Boolean readStatus, Boolean accRej) {
        realmController.editEvent(id, readStatus, accRej);
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
