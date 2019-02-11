package com.mca.xmpp;

import java.io.IOException;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class GetXmppConnection {


    public static XMPPTCPConnection getConnection(String domain, int port) {

        XMPPTCPConnection connection = null;

        try {

            int connectTimeout = 1200000;
            XMPPTCPConnectionConfiguration.Builder connconfig = XMPPTCPConnectionConfiguration.builder();
            connconfig.setConnectTimeout(connectTimeout);
            connconfig.setXmppDomain(domain);
            connconfig.setPort(port);
            connconfig.setSecurityMode(SecurityMode.disabled);
            connconfig.setHost(domain);
            connconfig.setSendPresence(true);
            // connconfig.enableDefaultDebugger();

            connconfig.setCompressionEnabled(true);

            SASLMechanism mechanism = new SASLPlainMechanism();
            SASLAuthentication.registerSASLMechanism(mechanism);
            SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
            SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");

            connection = new XMPPTCPConnection(connconfig.build());
            connection.setReplyTimeout(connectTimeout);
            connection.addConnectionListener(new ConncetionListner());
            connection.connect();
            connection.addAsyncStanzaListener(new MessageListner(), new MessageListner());
        } catch (InterruptedException
                | XMPPException
                | IOException
                | SmackException e) {
            e.printStackTrace();
        }
        return connection;
    }
}