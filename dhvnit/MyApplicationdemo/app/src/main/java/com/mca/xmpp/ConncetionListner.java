package com.mca.xmpp;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

public class ConncetionListner implements ConnectionListener {

	@Override
	public void authenticated(final XMPPConnection xmppConnection, final boolean arg1) {
		System.out.println("authenticated " + xmppConnection + " falg " + arg1);
	}

	@Override
	public void connected(final XMPPConnection xmppConnection) {
		System.out.println("connected " + xmppConnection);
	}

	@Override
	public void connectionClosed() {
		System.out.println("connection clsoed");

	}

	@Override
	public void connectionClosedOnError(final Exception error) {
		System.out.println("connection closed on Errror" + error);
	}
}
