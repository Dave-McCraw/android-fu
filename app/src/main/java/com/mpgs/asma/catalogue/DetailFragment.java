package com.mpgs.asma.catalogue;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpgs.asma.R;
import com.mpgs.asma.catalogue.model.Item;

import java.text.DecimalFormat;

/**
 * A fragment to display details of an item.
 *
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link StandaloneDetailActivity}
 * on handsets.
 *
 * TODO: in general this app doesn't particularly support tablets!
 */
public class DetailFragment extends Fragment {
    /**
     * Argument to the fragment, which allows us to retrieve the content at runtime
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The content this fragment is presenting.
     */
    private Item mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            // Get the item to be presented by this fragment
            mItem = ItemListActivity.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            if (fab != null) {
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ItemListActivity.BASKET.addOneOfItem(mItem);
                        Snackbar.make(view, "Item added to basket", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        // Update the detail line, as it includes a basket getQuantityOfAllItems
                        subtextView.setText(getSubtext());

                    }
                });
            }
        }
    }
    private TextView subtextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.catalogue_detail_fragment, container, false);
        subtextView = (TextView) rootView.findViewById(R.id.detail_price);

        if (mItem != null) {
            subtextView.setText(getSubtext());
            ((TextView) rootView.findViewById(R.id.detail_header_text)).setText(Html.fromHtml(mItem.details));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }

            ImageView img = (ImageView) activity.findViewById(R.id.header_bk);
            if (img != null) {
                img.setImageBitmap(ItemListActivity.mMemoryCache.get(mItem.thumbnailSource));
                img.setAlpha(0.2f);
            }
        }

        return rootView;
    }

    private String getSubtext(){

        String subtext = "£"+(new DecimalFormat("#.00").format( mItem.priceMinorUnits/100));

        int counter = ItemListActivity.BASKET.getQuantityOfItem(mItem.id);

            if (counter > 0 ){
                subtext += " ("+counter+" in cart)";
            }

        return subtext;
    }
}
