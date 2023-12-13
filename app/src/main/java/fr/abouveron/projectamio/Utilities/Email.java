package fr.abouveron.projectamio.Utilities;

import android.content.Intent;
import android.net.Uri;

import fr.abouveron.projectamio.AppContext;

public class Email {
    private final String receiver;
    private final String subject;
    private final String text;

    public Email(String receiver, String subject, String text) {
        this.receiver = receiver;
        this.subject = subject;
        this.text = text;
    }

    public void send() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, new String[]{receiver})
                .putExtra(Intent.EXTRA_SUBJECT, subject)
                .putExtra(Intent.EXTRA_TEXT, text);
        AppContext.getMainActivity().startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}