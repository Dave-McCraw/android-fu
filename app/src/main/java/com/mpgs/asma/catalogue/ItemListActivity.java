package com.mpgs.asma.catalogue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpgs.asma.basket.BasketActivity;
import com.mpgs.asma.R;
import com.mpgs.asma.basket.model.Basket;
import com.mpgs.asma.catalogue.model.Item;
import com.mpgs.asma.catalogue.model.ItemDownloader;
import com.mpgs.asma.images.BitmapCache;
import com.mpgs.asma.images.DownloadBitmapTask;
import com.mpgs.asma.preferences.PreferencesActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * An activity representing a list of items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StandaloneDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * TODO: attend to this class
 */
public class ItemListActivity extends AppCompatActivity implements ItemDownloader.ItemDownloadListener<Item> {

    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final Map<String, Item> ITEM_MAP = new HashMap<String, Item>();
    public static final Basket BASKET = new Basket();

    public static BitmapCache mMemoryCache;

    public SimpleItemRecyclerViewAdapter adapter;

    @Override
    public void onTaskComplete(List<Item> s) {
        System.out.println("Got some items: "+s.size());
        // Wipe old stuff
        ITEM_MAP.clear();
        ITEMS.clear();

        for (Item item : s){


            if (item != null) {
                // Add new versions
                ITEMS.add(item);
                ITEM_MAP.put(item.id, item);
            }
        }


        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }


    }


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_catalogue_list);

        if (ITEMS.isEmpty()) {
            URL url = null;
            try {
                String locale = Locale.getDefault().getLanguage();

                if ("en".equals(locale)){
                    locale = "";
                }
                else {
                    locale = "-"+locale;
                }

                url = new URL("http://10.0.2.2:8080/examples/catalogue"+locale+".json");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ItemDownloader task = new ItemDownloader(this, this);
            task.execute(url);
        } else {
            System.out.println("Already got items, not loading again.");
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        // caching images
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        if (mMemoryCache == null) {
            mMemoryCache = new BitmapCache(cacheSize);
        }
        // end caching images

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(getTitle());



        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, PreferencesActivity.class);

                context.startActivity(intent);
            }
        });
        ImageButton cartButton = (ImageButton) findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BasketActivity.class);

                context.startActivity(intent);
            }
        });

        // Set up the recycler UI support
        adapter = new SimpleItemRecyclerViewAdapter(ITEMS);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.basket_list);
        recyclerView.setAdapter(adapter);

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Set up the counter that hovers over the cart button
        final TextView cartBadge = (TextView) findViewById(R.id.cartBadge);
        if (null != cartBadge) {
            int size = 0;

                size = BASKET.getQuantityOfAllItems();

            cartBadge.setText(String.valueOf(size));

        }
    }


    void refreshItems() {
        // Load items

        // This should be incremental really, i.e. paginating?
        URL url = null;
        try {
            //TODO: make URL configurable
            String locale = Locale.getDefault().getLanguage();

            if ("en".equals(locale)){
                locale = "";
            }
            else {
                locale = "-"+locale;
            }

            url = new URL("http://10.0.2.2:8080/examples/catalogue"+locale+".json");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ItemDownloader task = new ItemDownloader(this, this);
        task.execute(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, ItemListActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }


    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Item> mValues;

        public SimpleItemRecyclerViewAdapter(List<Item> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.catalogue_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Item item = mValues.get(position);

            holder.mContentView.setText(item.content);

            // TODO: The cache is used by more than one activity, extract it.
            Bitmap bm= mMemoryCache.get(item.thumbnailSource);
            if(  bm == null ){
                DownloadBitmapTask task = new DownloadBitmapTask(holder.mThumbnail, mMemoryCache);
                task.execute(item.thumbnailSource);
            } else {
                holder.mThumbnail.setImageBitmap(bm);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(DetailFragment.ARG_ITEM_ID, item.id);
                        DetailFragment fragment = new DetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, StandaloneDetailActivity.class);
                        intent.putExtra(DetailFragment.ARG_ITEM_ID, item.id);
                        startActivityForResult(intent, 1);
                    }
                }

            });
        }


        @Override
        public int getItemCount() {
            return mValues.size();
        }

        // TODO: here we're keeping updates in the view bind method, not as a ViewHolder method. Make consistent.
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mThumbnail;
            public final TextView mContentView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}

