package com.github.dicomflow.androiddicomflow.activities.requests;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.mikepenz.iconics.view.IconicsImageButton;
import com.mikepenz.iconics.view.IconicsImageView;

public class RequestPutViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView dataDeEnvioView;
    public final TextView requestPutFromView;
    public final TextView messageIdView;

    public final IconicsImageView iconicsImageViewDownload;
    public final IconicsImageView iconicsImageViewForward;
    public final IconicsImageView iconicsImageViewReply;

    public RequestPutViewHolder(View view) {
        super(view);
        mView = view;
        dataDeEnvioView = (TextView) view.findViewById(R.id.data_de_envio);
        requestPutFromView = (TextView) view.findViewById(R.id.request_put_from);
        messageIdView = (TextView) view.findViewById(R.id.message_id);

        iconicsImageViewDownload = (IconicsImageView) view.findViewById(R.id.downloadButtom);
        iconicsImageViewForward = (IconicsImageView) view.findViewById(R.id.forwardButtom);
        iconicsImageViewReply = (IconicsImageView) view.findViewById(R.id.resultButtom);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + messageIdView.getText() + "'";
    }

    public void bindToPost(Request request) {
        dataDeEnvioView.setText(request.timestamp);
        //TODO substituir pelo email de quem realmente enviou a mensagem
        requestPutFromView.setText(request.from);
        messageIdView.setText(request.messageID);
    }
}