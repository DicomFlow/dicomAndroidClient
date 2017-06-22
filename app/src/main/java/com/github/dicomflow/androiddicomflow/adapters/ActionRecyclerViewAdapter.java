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
import com.github.dicomflow.androiddicomflow.activities.ActionDetailActivity;
import com.github.dicomflow.androiddicomflow.fragments.ActionDetailFragment;
import com.github.dicomflow.androiddicomflow.fragments.ServicoDetailFragment;
import com.github.dicomflow.androiddicomflow.models.protocolo.Action;

import java.util.List;

public class ActionRecyclerViewAdapter
        extends RecyclerView.Adapter<ActionRecyclerViewAdapter.ActionViewHolder> {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane = false;

    private final List<Action> actionList;

    public ActionRecyclerViewAdapter(boolean twoPane, List<Action> actionList) {
        this.mTwoPane = twoPane;
        this.actionList = actionList;
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.servico_list_content, parent, false);
        return new ActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ActionViewHolder holder, int position) {
        holder.action = actionList.get(position);
        holder.mIdView.setText(actionList.get(position).getName());
        holder.mContentView.setText(actionList.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ServicoDetailFragment.ARG_SERVICE_ID, holder.action.service.name);
                    arguments.putString(ActionDetailFragment.ARG_ACTION_ID, holder.action.getName());
                    ServicoDetailFragment fragment = new ServicoDetailFragment();
                    fragment.setArguments(arguments);
                    Context context = v.getContext();
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.serviço_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ActionDetailActivity.class);
                    intent.putExtra(ServicoDetailFragment.ARG_SERVICE_ID, holder.action.service.name);
                    intent.putExtra(ActionDetailFragment.ARG_ACTION_ID, holder.action.getName());

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Action action;

        public ActionViewHolder(View view) {
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