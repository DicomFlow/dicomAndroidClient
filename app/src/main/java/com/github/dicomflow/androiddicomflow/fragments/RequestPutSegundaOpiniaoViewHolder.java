package com.github.dicomflow.androiddicomflow.fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.requests.Request;
import com.mikepenz.iconics.view.IconicsButton;
import com.tooltip.Tooltip;

public class RequestPutSegundaOpiniaoViewHolder extends RecyclerView.ViewHolder {
    public final View mView;

    public final TextView requestPutFromView;

    public final IconicsButton buttonVerLaudo;


    public RequestPutSegundaOpiniaoViewHolder(View view) {
        super(view);
        mView = view;
        requestPutFromView = (TextView) view.findViewById(R.id.request_put_from);
        buttonVerLaudo = (IconicsButton) view.findViewById(R.id.buttonVerLaudo);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + requestPutFromView.getText() + "'";
    }

    public void bindToPost(Request request, int position) {
        requestPutFromView.setText(request.segundaOpiniaoPara);
        buttonVerLaudo.setTag(position);
        int color = (request.segundaOpiniaoPara != null && request.segundaOpiniaoPara.equals("aguardando-segunda-opiniao")) ? R.color.white : R.color.accent;
        buttonVerLaudo.setBackgroundColor(ContextCompat.getColor(buttonVerLaudo.getContext(), color));
    }
}