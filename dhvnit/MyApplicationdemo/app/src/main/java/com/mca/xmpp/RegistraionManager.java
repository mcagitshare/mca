package com.mca.xmpp;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

public class RegistraionManager {

	static RegistraionManager registraionManager;

	private static XMPPTCPConnection connection;

	public static synchronized RegistraionManager getInstance(final XMPPTCPConnection connection) {

		if (registraionManager == null) {
			registraionManager = new RegistraionManager(connection);
		}

		return registraionManager;

	}

	public RegistraionManager(final XMPPTCPConnection connection) {
		RegistraionManager.connection = connection;
	}

	public void register(final String username, final String password) throws NoResponseException, XMPPErrorException,
			NotConnectedException, InterruptedException, XmppStringprepException {

		Map<String, String> attributes = new HashMap<>();
		// attributes.put("source", "Android");
		// attributes.put("name", "Android");

		AccountManager accountManager = AccountManager.getInstance(connection);
		accountManager.sensitiveOperationOverInsecureConnection(true);
		accountManager.createAccount(Localpart.from(username), password, attributes);

	}
}
