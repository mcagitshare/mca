package com.mca.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.mca.Adapter.MessageRecyclerAdapter;
import com.mca.Adapter.SimpleDividerItemDecoration;
import com.mca.Application.DemoApplication;
import com.mca.R;
import com.mca.Realm.RealmClass;
import com.mca.Realm.RealmController;
import com.mca.Utils.Utils;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class MessagesFragment extends Fragment {

    RealmController realmController;
    RecyclerView recyclerView;
    MessageRecyclerAdapter adapter;
    JobManager jobManager;
    TextView noContacts;

    public MessagesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messages_fragment, container, false);

        jobManager = DemoApplication.getInstance().getJobManager();
        recyclerView = view.findViewById(R.id.rv_messages);
        noContacts = view.findViewById(R.id.no_contacts);
        realmController = RealmController.with(getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        Utils.printLog("messageBody", realmController.getMessages() + "");

        adapter = new MessageRecyclerAdapter(this, realmController.getMessages());
        realmController.getRealm().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                if (realmController.getMessages() != null && realmController.getMessages().size() > 0) {
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

    public void deleteMessageData(String itemId) {
        realmController.deleteMessageData(itemId);
    }

    public void editMessage(String id, Boolean readStatus, Boolean accRej) {
        realmController.editMessages(id, readStatus, accRej);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("onQueryTextSubmit", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.i("onQueryTextChange", query);
                if (query.isEmpty()) {
                    adapter = new MessageRecyclerAdapter(MessagesFragment.this, realmController.getMessages());
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter = new MessageRecyclerAdapter(MessagesFragment.this, RealmClass.searchMessagesData(query));
                    recyclerView.setAdapter(adapter);
                }
                return true;
            }

        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                // Not implemented here
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}