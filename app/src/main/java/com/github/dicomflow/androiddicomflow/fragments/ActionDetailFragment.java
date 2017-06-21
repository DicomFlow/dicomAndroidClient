package com.github.dicomflow.androiddicomflow.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.ActionDetailActivity;
import com.github.dicomflow.androiddicomflow.models.protocolo.Action;
import com.github.dicomflow.androiddicomflow.models.protocolo.DicomFlowProtocol;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.certificate.Certificate;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.certificate.CertificateAction;
import com.github.dicomflow.androiddicomflow.models.protocolo.xml.DicomFlowXmlSerializer;

/**
 * A fragment representing a single Action detail screen.
 * This fragment is either contained in a {@link com.github.dicomflow.androiddicomflow.activities.ServiceDetailActivity}
 * in two-pane mode (on tablets) or a {@link ActionDetailActivity}
 * on handsets.
 */
public class ActionDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ACTION_ID = "action_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Action action;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ACTION_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            DicomFlowProtocol dicomFlowProtocol =  DicomFlowProtocol.getInstance();
            Service service = dicomFlowProtocol.STRING_SERVICO_HASH_MAP.get(getArguments().getString(ServicoDetailFragment.ARG_SERVICE_ID));
            action = service.STRING_ACTION_HASH_MAP.get(getArguments().getString(ARG_ACTION_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(action.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.action_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (action != null) {
            ((Certificate)action.service).theAction = (CertificateAction)action;
            String xmlString = DicomFlowXmlSerializer.serialize(action.service);

            ((TextView) rootView.findViewById(R.id.action_detail)).setText(xmlString);
        }

        return rootView;
    }
}
