package com.mpgs.asma.basket;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.mpgs.asma.R;
import com.mpgs.asma.basket.model.BasketItemWrapper;
import com.mpgs.asma.catalogue.DetailFragment;
import com.mpgs.asma.catalogue.ItemListActivity;
import com.mpgs.asma.catalogue.StandaloneDetailActivity;
import com.mpgs.asma.images.DownloadBitmapTask;

/*
 * This activity represents the shopping cart. It holds a list of items, and supports
 * clicking through to their detail views, removing them from the basket, and checkout.
 */
public class BasketActivity extends AppCompatActivity {

    // Capture a few elements that we want available in inner classes.
    Button checkoutButton;
    TextView footerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        setupToolbar();

        // Set up the support mechanism for the list view
        RecyclerView basketItemsRecycler = (RecyclerView) findViewById(R.id.basket_list);
        basketItemsRecycler.setAdapter(new BasketRecyclerViewAdapter(ItemListActivity.BASKET.getBasketItems()));

        // Grab references to a couple of useful UI elements - needed by updateFooter()
        footerText = (TextView) findViewById(R.id.footerText);
        checkoutButton = (Button) findViewById(R.id.checkoutButton);

        // Initialise the footer bar.
        updateFooter();
    }

    // Get the toolbar to show up, and configure it.
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
     * Change the footer text, and configure the checkout button, according to basket state.
     */
    private void updateFooter() {
        // TODO: unsatisfactory that the basket is under a particular activity (should be centralized & injected)
        // If the basket is empty, disable checkout
        if (ItemListActivity.BASKET.isEmpty()) {
            footerText.setText(R.string.label_basket_empty);
            checkoutButton.setEnabled(false);
        }
        // If basket has item(s), enable checkout
        else {
            Resources res = getResources();
            footerText.setText(res.getQuantityString(R.plurals.label_basket, ItemListActivity.BASKET.getQuantityOfAllItems(), ItemListActivity.BASKET.getQuantityOfAllItems(), Double.valueOf(ItemListActivity.BASKET.getValueOfAllItems() / 100)));
            checkoutButton.setEnabled(true);
        }
    }

    /**
     * Adapter to get the Recycler list in the UI to work with our data model.
     */
    public class BasketRecyclerViewAdapter
            extends RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder> {

        // TODO: I think the class may cope with directly referencing the real basket items collection.
        private List<BasketItemWrapper> basketItemCollection;

        public BasketRecyclerViewAdapter(List<BasketItemWrapper> items) {
            assert items != null;

            basketItemCollection = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.basket_list_item, parent, false);
            return new ViewHolder(view);
        }

        /**
         * This method is called when the app wants to display something in the Recycler
         *
         * @param holder the object containing a cache of UI elements (for the row we want to use)
         * @param position the index of the item in the underlying datasource (use this to populate)
         */
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            BasketItemWrapper basketItem = basketItemCollection.get(position);
            holder.updateView(basketItem);
        }

        @Override
        public int getItemCount() {
            return basketItemCollection.size();
        }

        /**
         * The purpose of this class is to cache UI components
         * making it quick to update them as the list scrolls.
         *
         * The actual updating is done in onBindViewHolder()
         */
        public class ViewHolder extends RecyclerView.ViewHolder {

            public final ImageView thumbnail;
            public final TextView title;
            public final TextView itemCount;
            public final ImageButton removeItemButton;
            public BasketItemWrapper basketItem;

            public ViewHolder(View view) {
                super(view);

                // Capture UI elements
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                title = (TextView) view.findViewById(R.id.item_name);
                itemCount = (TextView) view.findViewById(R.id.item_count);
                removeItemButton = (ImageButton) view.findViewById(R.id.item_remove);

                // Set up the click on line -> details view behaviour
                // TODO: It seems odd to set these in the ViewHolder, but more efficient that creating one every time in the update method?
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Context context = v.getContext();
                        Intent intent = new Intent(context, StandaloneDetailActivity.class);
                        intent.putExtra(DetailFragment.ARG_ITEM_ID, basketItem.item.id);

                        context.startActivity(intent);

                    }
                });

                // Set up the per-line "remove item" button
                // TODO: a proper cart has an update quantity feature?
                removeItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Remove from the underlying dataset
                        ItemListActivity.BASKET.removeOneOfItem(basketItem.item);

                        // Notify the view depending on if we just removed the last of a type of item, or merely reduced the quantity in the cart.
                        if (ItemListActivity.BASKET.getQuantityOfItem(basketItem.item.id) == 0) {
                            notifyItemRemoved(getAdapterPosition());
                        } else {
                            notifyDataSetChanged();
                        }
                        updateFooter();

                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + title.getText() + "'";
            }

            /*
             * This method is called when we want to change the content of the view.
             */
            public void updateView(BasketItemWrapper basketItem) {
                this.basketItem = basketItem;

                title.setText(basketItem.item.content);
                itemCount.setText(String.valueOf(basketItem.quantity));

                // TODO: unsatisfactory leeching of bitmap cache from another activity... centralize
                Bitmap bm = ItemListActivity.mMemoryCache.get(basketItem.item.thumbnailSource);
                if (bm == null) {
                    DownloadBitmapTask task = new DownloadBitmapTask(thumbnail, ItemListActivity.mMemoryCache);
                    task.execute(basketItem.item.thumbnailSource);
                } else {
                    thumbnail.setImageBitmap(bm);
                }
            }
        }
    }

    // Get the back button to take us up to the catalogue list.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
