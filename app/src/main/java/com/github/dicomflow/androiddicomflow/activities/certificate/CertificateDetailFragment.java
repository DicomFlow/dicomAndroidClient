package com.github.dicomflow.androiddicomflow.activities.certificate;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

/**
 * A fragment representing a single Certificate detail screen.
 * This fragment is either contained in a {@link CertificateListActivity}
 * in two-pane mode (on tablets) or a {@link CertificateDetailActivity}
 * on handsets.
 */
public class CertificateDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy email this fragment is presenting.
     */
    private Certificate mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CertificateDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy email specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load email from a email provider.
            //TODO remover essa classe ou ajeitar esse codigo
            mItem = null; //DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.from);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.certificate_detail, container, false);

        // Show the dummy email as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.certificate_detail)).setText(mItem.certificateFilePath);
        }

        return rootView;
    }
}
