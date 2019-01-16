package com.project.xr.contactsync.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.project.xr.contactsync.Adapter.RecyclerAdapter;
import com.project.xr.contactsync.Adapter.SimpleDividerItemDecoration;
import com.project.xr.contactsync.Class.Utils;
import com.project.xr.contactsync.Constants;
import com.project.xr.contactsync.DemoApplication;
import com.project.xr.contactsync.R;
import com.project.xr.contactsync.Realm.RealmClass;
import com.project.xr.contactsync.Realm.RealmController;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class ContactListActivity extends AppCompatActivity {
    RealmController realmController;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    JobManager jobManager;
    TextView contacts, noContacts;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();

        contacts.setText(realmController.getItemsCount() + " " + Constants.contacts);
    }

    public void deleteItem(final String itemId) {
        realmController.deleteItem(itemId);
    }

    public void editItem(final String itemId, final String name, String number) {
        realmController.editItem(itemId, name, number);
        Utils.hideKeyboard(ContactListActivity.this);
    }

    private void initView() {
        jobManager = DemoApplication.getInstance().getJobManager();
        recyclerView = findViewById(R.id.recyclerView);
        contacts = findViewById(R.id.tv_contacts);
        noContacts = findViewById(R.id.no_contacts);
        realmController = RealmController.with(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(ContactListActivity.this));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(ContactListActivity.this, realmController.getItems());
        realmController.getRealm().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                if (realmController.getItems() != null && realmController.getItems().size() > 0) {
                    noContacts.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    contacts.setText(realmController.getItemsCount() + " " + Constants.contacts);
                } else {
                    noContacts.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Associate searchable configuration with the SearchView
        MenuItem mSearch = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) mSearch.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (query.isEmpty()) {
                    adapter = new RecyclerAdapter(ContactListActivity.this, realmController.getItems());
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter = new RecyclerAdapter(ContactListActivity.this, RealmClass.searchData(query));
                    recyclerView.setAdapter(adapter);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //no inspection Simplifiable If Statement
        if (id == R.id.search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}