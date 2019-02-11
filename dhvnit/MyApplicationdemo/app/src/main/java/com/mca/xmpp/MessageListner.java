package com.mca.xmpp;

import android.util.Log;

import com.mca.Utils.Utils;

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
        return true;
    }

    @Override
    public void processStanza(Stanza stazna)
            throws NotConnectedException, InterruptedException, NotLoggedInException {
        Utils.printLog("stanza received: ", "message stanza" + stazna.toXML(null));

/*        Message message = (Message) stazna;
        Utils.printLog("message stanza", message.toXML(null).toString());

        if (message.getType().equals("normal/event/c")) {
            //  add the event to Event List
        }
        if (message.getType().equals("normal/event/u")) {
            //  Update the event to Event List
        }
        if (message.getType().equals("normal/event/d")) {
            //  Delete the event to Event List
        }
        if (message.getType().equals("normal/message/c")) {
            // add the message to Message List
        }
        if (message.getType().equals("normal/message/d")) {
            // delete the message to Message List
        }
        if (message.getType().equals("normal/vcard/c")) {
            // add the contact to Contact List
        }
        if (message.getType().equals("normal/vcard/u")) {
            // update the contact to Contact List
        }
        if (message.getType().equals("normal/vcard/d")) {
            // delete the contact to Contact List
        } else {
            Utils.printLog("Message : ", "didn't get");
        }*/
    }
}