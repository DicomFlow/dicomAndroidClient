package com.github.dicomflow.androiddicomflow.activities.certificate;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.dicomflow.androiddicomflow.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.view.IconicsImageView;
import com.tooltip.Tooltip;

public class CertificateViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mContentView;
    public final ImageButton imageButtonCertificateMissing;
    public final ImageButton imageButtonCertificateGreen;
    public final ImageButton imageButtonCertificateGray;


    public CertificateViewHolder(View view) {
        super(view);
        mView = view;
        mContentView = (TextView) view.findViewById(R.id.email);
        //TODO colocar o tooltip aqui
        imageButtonCertificateMissing = (ImageButton) view.findViewById(R.id.lock_missing);
        imageButtonCertificateGreen = (ImageButton) view.findViewById(R.id.lock_green);
        imageButtonCertificateGray = (ImageButton) view.findViewById(R.id.lock_gray);

        imageButtonCertificateMissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip.Builder builder = new Tooltip.Builder(v, R.style.Tooltip2)
                        .setCancelable(true)
                        .setDismissOnClick(false)
                        .setCornerRadius(20f)
                        .setGravity(Gravity.BOTTOM)
                        .setText("Certificado faltando.");
                builder.show();
            }
        });
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }

    public void bindToPost(Certificate certificate, int position) {
        mContentView.setText(certificate.email);

        imageButtonCertificateMissing.setTag(position);
        imageButtonCertificateGreen.setTag(position);
        imageButtonCertificateGray.setTag(position);

        hideButtonsWithStatus(certificate.status);
    }

    private void hideButtonsWithStatus(String status) {

        switch (status) {
            case "certificados-trocados-e-confirmados":
                imageButtonCertificateGreen.setVisibility(View.VISIBLE);
                imageButtonCertificateGray.setVisibility(View.GONE);
                imageButtonCertificateMissing.setVisibility(View.GONE);
                break;
            case "aguardando-certificado":
                imageButtonCertificateGreen.setVisibility(View.GONE);
                imageButtonCertificateGray.setVisibility(View.GONE);
                imageButtonCertificateMissing.setVisibility(View.VISIBLE);
                break;
            case "aguardando-enviar-confirmacao-minha":
                //nessa tela nao eh considerado esse sttus
                imageButtonCertificateGreen.setVisibility(View.GONE);
                imageButtonCertificateGray.setVisibility(View.GONE);
                imageButtonCertificateMissing.setVisibility(View.GONE);
                break;
            case "aguardando-receber-confirmacao-dele":
                imageButtonCertificateGreen.setVisibility(View.GONE);
                imageButtonCertificateGray.setVisibility(View.VISIBLE);
                imageButtonCertificateMissing.setVisibility(View.GONE);
                break;
            default: return;
        }

    }

}
