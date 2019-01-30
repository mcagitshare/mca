package com.mca.xmpp;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

public class MessageListner implements StanzaListener, StanzaFilter {

	@Override
	public boolean accept(final Stanza stanza) {
		if (stanza instanceof Message) {
			return true;
		}
		return false;
	}

	@Override
	public void processStanza(final Stanza stazna)
			throws NotConnectedException, InterruptedException, NotLoggedInException {
		System.out.println("stanza received " + stazna.toXML(null).toString());

		Message message = (Message) stazna;

		if(message.getType().equals("normal/event/c")){
			// add job to add in event list
		}else if(message.getType().equals("normal/event/u")){
			// add upate event job.
		}
	}
}