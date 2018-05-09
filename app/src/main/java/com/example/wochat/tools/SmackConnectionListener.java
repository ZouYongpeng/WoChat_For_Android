package com.example.wochat.tools;

import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by 邹永鹏 on 2018/5/4.
 */

public class SmackConnectionListener implements ConnectionListener {
    @Override
    public void connected(XMPPConnection connection) {
        Log.d("SmackConnectionListener","connected");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.d("SmackConnectionListener","authenticated");
    }

    @Override
    public void connectionClosed() {
        Log.d("SmackConnectionListener","connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d("SmackConnectionListener","connectionClosedOnError");
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d("SmackConnectionListener","reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d("SmackConnectionListener","reconnectingIn");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d("SmackConnectionListener","reconnectionFailed");
    }
}
