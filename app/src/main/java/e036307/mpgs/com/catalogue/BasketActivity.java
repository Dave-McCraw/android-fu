package e036307.mpgs.com.catalogue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.RSInvalidStateException;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import e036307.mpgs.com.catalogue.images.DownloadBitmapTask;
import e036307.mpgs.com.catalogue.model.BasketCatalogueItem;
import e036307.mpgs.com.catalogue.model.CatalogueItem;

public class BasketActivity extends AppCompatActivity {

    View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.suit_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

         totalLine = (TextView) findViewById(R.id.totalLine);
        assert totalLine != null;
        if (null == CatalogueListActivity.BASKET.basketItems || CatalogueListActivity.BASKET.basketItems.isEmpty()) {
            totalLine.setText("Basket empty");
            // button disable!
        } else {
            totalLine.setText(CatalogueListActivity.BASKET.count() + " items totalling " + ("£" + (new DecimalFormat("#.00").format(CatalogueListActivity.BASKET.getValue() / 100))));
        }
    }
    TextView totalLine;

    SimpleItemRecyclerViewAdapter adapter;

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter =  new SimpleItemRecyclerViewAdapter(CatalogueListActivity.BASKET.basketItems);
        recyclerView.setAdapter(adapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<BasketCatalogueItem> mValues;

        public SimpleItemRecyclerViewAdapter(Map<String, BasketCatalogueItem> items) {
            assert items != null;

            // Where there are multiple of the same item, what do we do?
            mValues = new ArrayList<BasketCatalogueItem>(items.values());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.basket_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);

            // Set the thumbnail image using the property of the suititem
            int resID = getResources().getIdentifier(mValues.get(position).item.thumbnailSource, "drawable",  getPackageName());
            //     holder.mThumbnail.setImageResource(R.drawable.shirt_small);
            // holder.mThumbnail.setImageResource(resID);

            Bitmap bm= CatalogueListActivity.mMemoryCache.get(mValues.get(position).item.thumbnailSource);
            if(  bm == null ){
                System.out.println("Cache miss on "+mValues.get(position).item.thumbnailSource);
                DownloadBitmapTask task = new DownloadBitmapTask(holder.mThumbnail, CatalogueListActivity.mMemoryCache);
                task.execute(mValues.get(position).item.thumbnailSource);
            } else {
                holder.mThumbnail.setImageBitmap(bm);
                System.out.println("Cache hit");
            }

            //   System.out.println("Changing thumbnail to resource "+resID+" based on "+mValues.get(position).thumbnailSource);

            holder.mItemCount.setText(String.valueOf(mValues.get(position).quantity));

            holder.mTitle.setText(mValues.get(position).item.content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Context context = v.getContext();
                        Intent intent = new Intent(context, CatalogueStandaloneDetailActivity.class);
                        intent.putExtra(CatalogueDetailFragment.ARG_ITEM_ID, holder.mItem.item.id);

                        context.startActivity(intent);

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
            public final TextView mTitle;
            public final TextView mItemCount;
            public BasketCatalogueItem mItem;
            public final View mRemoveItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                mTitle = (TextView) view.findViewById(R.id.item_name);
                mItemCount = (TextView) view.findViewById(R.id.item_count);
                mRemoveItem= (ImageButton) view.findViewById(R.id.item_remove);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTitle.getText() + "'";
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, CatalogueListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
