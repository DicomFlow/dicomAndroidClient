package com.github.dicomflow.androiddicomflow.activities.requests;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView nomePacienteView;
    public final TextView requestPutFromView;
    public final TextView estudoView;
    public final TextView modalidadeView;
    public final TextView messageIdView;

    public RequestViewHolder(View view) {
        super(view);
        mView = view;
        nomePacienteView = (TextView) view.findViewById(R.id.nome_paciente);
        requestPutFromView = (TextView) view.findViewById(R.id.request_put_from);
        estudoView = (TextView) view.findViewById(R.id.estudo);
        modalidadeView = (TextView) view.findViewById(R.id.modalidade);
        messageIdView = (TextView) view.findViewById(R.id.message_id);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + messageIdView.getText() + "'";
    }

    public void bindToPost(Request request) {
        nomePacienteView.setText(request.);
        requestPutFromView.setText(request.details);
        estudoView.setText(request.id);
        modalidadeView.setText(request.details);
        messageIdView.setText(request.id);
    }
}