package me.omico.currentactivity.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import me.omico.currentactivity.CurrentActivity;
import me.omico.currentactivity.R;
import me.omico.currentactivity.base.activity.AppCompatBaseActivity;
import me.omico.currentactivity.model.CurrentActivityData;
import me.omico.currentactivity.ui.adapter.CurrentActivityDataAdapter;

public class HistoryActivity extends AppCompatBaseActivity {

    private CurrentActivity currentActivity;
    private CurrentActivityDataAdapter currentActivityDataAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.colorPrimaryDark);
        setStatusBarDarkMode(true);
        currentActivity = (CurrentActivity) getApplication();
        initView();
        initAdapter();
        updateAdapter();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_history;
    }

    @Override
    protected int setSupportActionBar() {
        return R.id.toolbar;
    }

    @Override
    protected boolean setDisplayHomeAsUpEnabled() {
        return true;
    }

    @Override
    protected CharSequence setTitle() {
        return getString(R.string.history);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_clear_history:
                currentActivity.clearCurrentActivityData();
                updateAdapter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        recyclerView = findViewById(R.id.recycler_view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateAdapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initAdapter() {
        currentActivityDataAdapter = new CurrentActivityDataAdapter();
        recyclerView.setAdapter(currentActivityDataAdapter);
    }

    private void updateAdapter() {
        List<CurrentActivityData> data = currentActivity.loadCurrentActivityData();
        currentActivityDataAdapter.setData(data);
        currentActivityDataAdapter.notifyDataSetChanged();
    }
}
