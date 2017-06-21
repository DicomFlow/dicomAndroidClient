package com.github.dicomflow.androiddicomflow.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.ServiceDetailActivity;
import com.github.dicomflow.androiddicomflow.fragments.ServicoDetailFragment;
import com.github.dicomflow.androiddicomflow.models.protocolo.services.Service;

import java.util.List;

public class ServiceRecyclerViewAdapter
        extends RecyclerView.Adapter<ServiceRecyclerViewAdapter.ServiceViewHolder> {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane = false;

    private final List<Service> serviceList;

    public ServiceRecyclerViewAdapter(boolean twoPane, List<Service> serviceList) {
        this.mTwoPane = twoPane;
        this.serviceList = serviceList;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.servico_list_content, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServiceViewHolder holder, int position) {
        holder.service = serviceList.get(position);
        holder.mIdView.setText(serviceList.get(position).getName());
        holder.mContentView.setText(serviceList.get(position).getDetails());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ServicoDetailFragment.ARG_SERVICE_ID, holder.service.getName());
                    ServicoDetailFragment fragment = new ServicoDetailFragment();
                    fragment.setArguments(arguments);
                    Context context = v.getContext();
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.serviço_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ServiceDetailActivity.class);
                    intent.putExtra(ServicoDetailFragment.ARG_SERVICE_ID, holder.service.getName());

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Service service;

        public ServiceViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}