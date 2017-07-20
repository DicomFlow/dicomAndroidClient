package com.github.dicomflow.androiddicomflow.activities.requests;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.github.dicomflow.androiddicomflow.R;
import com.github.dicomflow.dicomflowjavalib.services.Service;
import com.github.dicomflow.dicomflowjavalib.utils.DicomFlowXmlSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ricardobarbosa on 27/06/17.
 */

public abstract class MessageServiceSenderDecorator extends MessageServiceSender{
    protected final MessageServiceSender messageServiceSenderDecorado;

    public MessageServiceSenderDecorator(Context context, MessageServiceSender messageServiceSenderDecorado) {
        super(context);
        this.messageServiceSenderDecorado = messageServiceSenderDecorado;
    }

}
