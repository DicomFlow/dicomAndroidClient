package com.github.dicomflow.androiddicomflow.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.androiddicomflow.activities.requests.Request;
import com.mikepenz.iconics.view.IconicsButton;

public class NovoRequestPutViewHolder extends RecyclerView.ViewHolder {
    public final View mView;

    public final TextView requestPutFromView;

    public final IconicsButton buttonEnviarLaudo;
    public final IconicsButton buttonImagens;
    public final IconicsButton buttonVerSegundasOpinioes;


    public NovoRequestPutViewHolder(View view) {
        super(view);
        mView = view;
        requestPutFromView = (TextView) view.findViewById(R.id.request_put_from);

        buttonEnviarLaudo = (IconicsButton) view.findViewById(R.id.buttonEnviarLaudo);
        buttonImagens = (IconicsButton) view.findViewById(R.id.buttonImagens);
        buttonVerSegundasOpinioes = (IconicsButton) view.findViewById(R.id.buttonVerSegundasOpinioes);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + requestPutFromView.getText() + "'";
    }

    public void bindToPost(Request request, int position) {
        //TODO [REQUEST] por o email aqui
        requestPutFromView.setText(request.messageID);

        buttonEnviarLaudo.setTag(position);
        buttonImagens.setTag(position);
        buttonVerSegundasOpinioes.setTag(position);
    }
}