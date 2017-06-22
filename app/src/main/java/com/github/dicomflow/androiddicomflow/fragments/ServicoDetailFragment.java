package com.github.dicomflow.androiddicomflow.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.adapters.ActionRecyclerViewAdapter;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.DicomFlowProtocol;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.ServiceListActivity;
import com.github.dicomflow.androiddicomflow.activities.ServiceDetailActivity;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

/**
 * A fragment representing a single Serviço detail screen.
 * This fragment is either contained in a {@link ServiceListActivity}
 * in two-pane mode (on tablets) or a {@link ServiceDetailActivity}
 * on handsets.
 */
public class ServicoDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_SERVICE_ID = "service_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Service service;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServicoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_SERVICE_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            DicomFlowProtocol dicomFlowProtocol =  DicomFlowProtocol.getInstance();
            service = dicomFlowProtocol.STRING_SERVICO_HASH_MAP.get(getArguments().getString(ARG_SERVICE_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(service.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.servico_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (service != null) {
            ((TextView) rootView.findViewById(R.id.serviço_detail)).setText(service.version);

            View recyclerView = rootView.findViewById(R.id.action_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView, service);
        }

        return rootView;
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull Service service) {
        recyclerView.setAdapter(new ActionRecyclerViewAdapter(mTwoPane, DicomFlowProtocol.getInstance().STRING_SERVICE_LIST_OF_ACTION_HASH_MAP.get(service.getName())));
    }

}
