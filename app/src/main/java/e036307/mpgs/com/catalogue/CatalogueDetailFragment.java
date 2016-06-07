package e036307.mpgs.com.catalogue;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DecimalFormat;

import e036307.mpgs.com.catalogue.model.Basket;
import e036307.mpgs.com.catalogue.model.BasketCatalogueItem;
import e036307.mpgs.com.catalogue.model.CatalogueItem;

/**
 * A fragment representing a single Suit detail screen.
 * This fragment is either contained in a {@link CatalogueListActivity}
 * in two-pane mode (on tablets) or a {@link CatalogueStandaloneDetailActivity}
 * on handsets.
 */
public class CatalogueDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private CatalogueItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CatalogueDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = CatalogueListActivity.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CatalogueListActivity.BASKET.addItem(mItem);
                    Snackbar.make(view, "Item added to basket", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    subtextView.setText(getSubtext());

                }
            });

        }
    }
    private TextView subtextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.catalogue_detail_fragment, container, false);
        subtextView = (TextView) rootView.findViewById(R.id.detail_price);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {


            subtextView.setText(getSubtext());
            ((TextView) rootView.findViewById(R.id.detail_header_text)).setText(mItem.detailHeader);
            ((TextView) rootView.findViewById(R.id.detail_header_text)).setText(Html.fromHtml(mItem.details));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }

            ImageView img = (ImageView) activity.findViewById(R.id.header_bk);
            if (img != null) {
                img.setImageBitmap(CatalogueListActivity.mMemoryCache.get(mItem.thumbnailSource));
                img.setAlpha(0.2f);
            }
        }

        return rootView;
    }

    private String getSubtext(){

        String subtext = "£"+(new DecimalFormat("#.00").format( mItem.priceMinorUnits/100));

        BasketCatalogueItem bci = CatalogueListActivity.BASKET.basketItems.get(mItem.id);

        if (null != bci){
            if (bci.quantity > 0 ){
                subtext += " ("+bci.quantity+" in cart)";
            }
        }

        return subtext;
    }
}
