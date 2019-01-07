package com.example.lenovo.myapplicationdemo.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.birbit.android.jobqueue.JobManager;
import com.example.lenovo.myapplicationdemo.Adapter.RecyclerAdapter;
import com.example.lenovo.myapplicationdemo.Class.Utils;
import com.example.lenovo.myapplicationdemo.R;
import com.example.lenovo.myapplicationdemo.Realm.DemoApplication;
import com.example.lenovo.myapplicationdemo.Realm.RealmController;
import com.example.lenovo.myapplicationdemo.RealmClass;

public class ContactListActivity extends AppCompatActivity {
    RealmController realmController;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    JobManager jobManager;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initView();
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

        realmController = RealmController.with(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(ContactListActivity.this));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(ContactListActivity.this, realmController.getItems());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transaction_search, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
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
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}