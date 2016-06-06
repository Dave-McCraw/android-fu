package e036307.mpgs.com.catalogue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import e036307.mpgs.com.catalogue.model.Basket;
import e036307.mpgs.com.catalogue.model.BasketListener;
import e036307.mpgs.com.catalogue.model.CatalogueItem;
import e036307.mpgs.com.catalogue.model.CatalogueListener;
import e036307.mpgs.com.catalogue.model.CatalogueRetriever;
import e036307.mpgs.com.catalogue.images.BitmapCache;
import e036307.mpgs.com.catalogue.images.DownloadBitmapTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An activity representing a list of Suits. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CatalogueStandaloneDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CatalogueListActivity extends AppCompatActivity implements CatalogueListener<CatalogueItem> {

    public static final List<CatalogueItem> ITEMS = new ArrayList<CatalogueItem>();
    public static final Map<String, CatalogueItem> ITEM_MAP = new HashMap<String, CatalogueItem>();
    public static final Basket BASKET = new Basket();

    @Override
    public void onTaskComplete(List<CatalogueItem> s) {
        System.out.println("Got some items: "+s.size());
        // Wipe old stuff
        ITEM_MAP.clear();
        ITEMS.clear();

        for (CatalogueItem suit : s){


            if (suit != null) {
                // Add new versions
                ITEMS.add(suit);
                ITEM_MAP.put(suit.id, suit);
            }
        }


        if (mSwipeRefreshLayout.isRefreshing()) {
            System.out.print("Told the layout we are done refreshing");
            mSwipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }


    }

    public static BitmapCache mMemoryCache;
    View recyclerView;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("================================= CREATE SUITLISTACTIVITY ========================================");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_catalogue_list);

        if (ITEMS.isEmpty()) {
            URL url = null;
            try {
                url = new URL("http://10.0.2.2:8080/examples/catalogue.json");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            CatalogueRetriever task = new CatalogueRetriever(this, this);
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
        toolbar.setTitle(getTitle());



        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SettingsActivity.class);

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

        recyclerView = findViewById(R.id.suit_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.suit_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        final TextView cartBadge = (TextView) findViewById(R.id.cartBadge);
        if (null != cartBadge) {
            int size = 0;
            if (null != BASKET.basketItems){
                size = BASKET.basketItems.size();
            }
            cartBadge.setText(String.valueOf(size));

        }
    }


    void refreshItems() {
        // Load items

        // This should be incremental really, i.e. paginating?
        URL url = null;
        try {
            url = new URL("http://10.0.2.2:8080/examples/catalogue.json");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        CatalogueRetriever task = new CatalogueRetriever(this, this);
        task.execute(url);
    }

    /*public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }*/

    SimpleItemRecyclerViewAdapter adapter;

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter =  new SimpleItemRecyclerViewAdapter(ITEMS);
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<CatalogueItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<CatalogueItem> items) {
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
            holder.mItem = mValues.get(position);

            // Set the thumbnail image using the property of the suititem
            int resID = getResources().getIdentifier(mValues.get(position).thumbnailSource, "drawable",  getPackageName());
       //     holder.mThumbnail.setImageResource(R.drawable.shirt_small);
           // holder.mThumbnail.setImageResource(resID);

            Bitmap bm= mMemoryCache.get(mValues.get(position).thumbnailSource);
            if(  bm == null ){
                System.out.println("Cache miss on "+mValues.get(position).thumbnailSource);
                DownloadBitmapTask task = new DownloadBitmapTask(holder.mThumbnail, mMemoryCache);
                task.execute(mValues.get(position).thumbnailSource);
            } else {
                holder.mThumbnail.setImageBitmap(bm);
                System.out.println("Cache hit");
            }

         //   System.out.println("Changing thumbnail to resource "+resID+" based on "+mValues.get(position).thumbnailSource);

            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CatalogueDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        CatalogueDetailFragment fragment = new CatalogueDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.suit_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CatalogueStandaloneDetailActivity.class);
                        intent.putExtra(CatalogueDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mThumbnail;
            public final TextView mContentView;
            public CatalogueItem mItem;

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

