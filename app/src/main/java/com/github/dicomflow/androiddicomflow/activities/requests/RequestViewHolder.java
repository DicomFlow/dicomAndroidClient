package com.github.dicomflow.androiddicomflow.activities.requests;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;

    public RequestViewHolder(View view) {
        super(view);
        mView = view;
        mIdView = (TextView) view.findViewById(R.id.id);
        mContentView = (TextView) view.findViewById(R.id.content);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }

    public void bindToPost(Request request) {
        mIdView.setText(request.id);
        mContentView.setText(request.details);
    }
}