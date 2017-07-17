package com.qbasso.nfctag;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    public static final int DELAY_MILLIS = 3000;
    public static final String NFC_TAG_MIMETYPE = "application/com.qbasso.door";
    private static String CHARSET = "US-ASCII";
    private static String PAYLOAD_UNLOCK = "unlock";
    private static String PAYLOAD_EMPTY = "nothing";
    private String currentPayload = PAYLOAD_UNLOCK;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler()
        setContentView(R.layout.activity_main);
        startNfcTagBroadcast();
    }

    private void startNfcTagBroadcast() {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter != null) {
            adapter.setNdefPushMessage(createNdefMessage(currentPayload), this);
            ((TextView) findViewById(R.id.text)).setText("Pushed message " + currentPayload);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentPayload = currentPayload.equals(PAYLOAD_UNLOCK) ? PAYLOAD_EMPTY : PAYLOAD_UNLOCK;
                    startNfcTagBroadcast();
                }
            }, DELAY_MILLIS);
        }
    }

    private NdefMessage createNdefMessage(String payload) {
        NdefRecord mimeRecord = NdefRecord.createMime(NFC_TAG_MIMETYPE, payload.getBytes(Charset.forName(CHARSET)));
        return new NdefMessage(new NdefRecord[]{mimeRecord});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        return createNdefMessage(currentPayload);
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {

    }
}
