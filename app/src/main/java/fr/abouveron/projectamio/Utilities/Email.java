package fr.abouveron.projectamio.Utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Email {
    private final String receiver;
    private final String subject;
    private final String text;
    private final Context context;

    public Email(String receiver, String subject, String text, Context context) {
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;
        this.context = context;
    }

    public void send() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, new String[]{receiver})
                .putExtra(Intent.EXTRA_SUBJECT, subject)
                .putExtra(Intent.EXTRA_TEXT, text)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(emailIntent);
    }
}