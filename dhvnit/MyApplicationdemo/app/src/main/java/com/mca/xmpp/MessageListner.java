package com.mca.xmpp;

import com.birbit.android.jobqueue.JobManager;
import com.mca.Application.DemoApplication;
import com.mca.Job.GroupJob;
import com.mca.Job.EventsJob;
import com.mca.Job.MessagesJob;
import com.mca.Utils.Utils;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageListner implements StanzaListener, StanzaFilter {

    JobManager jobManager;

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

        jobManager = DemoApplication.getInstance().getJobManager();
        if (stazna instanceof Message) {

            Message message = (Message) stazna;
            String messageBody = message.getBody();

            if (messageBody != null && !messageBody.isEmpty()) {

                try {
                    JSONObject messageObj = new JSONObject(messageBody);

                    Utils.printLog("message stanza", message.toXML(null).toString());

                    //Event
                    if (messageObj.getInt("type") == 101) {

                        //  add the event to Event List
                        Boolean readStatus = false;
                        Boolean accRej = false;
                        String eventName = messageObj.getString("eventname");
                        String icon = messageObj.getString("icon");
                        String dateFrom = messageObj.getString("datefrom");
                        String dateTo = messageObj.getString("dateto");

                        String option = null;
                        JSONArray optionsArray = messageObj.getJSONArray("options");
                        for (int i = 0; i < optionsArray.length(); i++) {
                            JSONObject jobOptions = optionsArray.getJSONObject(i);
                            if (jobOptions.getInt("responecode") == 200) {
                                option = jobOptions.getString("option");
                            }
                        }

                        jobManager.addJobInBackground(new EventsJob(eventName, icon, dateFrom, dateTo, option, readStatus, accRej));
                    }
/*                    if (message.getType().equals("normal/event/u")) {
                        //  Update the event to Event List
                    }
                    if (message.getType().equals("normal/event/d")) {
                        //  Delete the event to Event List
                    }*/

                    //Message
                    if (messageObj.getInt("type") == 100) {

                        // add the message to Message List
                        Boolean readStatus = false;
                        Boolean accRej = false;
                        String displayMessage = messageObj.getString("displaymessage");
                        String icon = messageObj.getString("icon");

                        String option = null;
                        JSONArray optionsArray = messageObj.getJSONArray("options");
                        for (int i = 0; i < optionsArray.length(); i++) {
                            JSONObject jobOptions = optionsArray.getJSONObject(i);
                            if (jobOptions.getInt("responecode") == 200) {
                                option = jobOptions.getString("option");
                            }
                        }
                        jobManager.addJobInBackground(new MessagesJob(displayMessage, icon, option, readStatus, accRej));

                    }
/*                    if (message.getType().equals("normal/message/d")) {
                        // delete the message to Message List
                    }*/
                    if (messageObj.getInt("type") == 103) {
                        // add the contact to Group List
                        Boolean readStatus = false;
                        Boolean accRej = false;
                        String groupId = messageObj.getString("groupid");
                        String id = messageObj.getString("id");
                        String name = messageObj.getString("name");
                        String image = messageObj.getString("image");
                        String phone = messageObj.getString("phone");

                        jobManager.addJobInBackground(new GroupJob(groupId, id, name, image, phone, readStatus, accRej));
                    }
/*                    if (message.getType().equals("normal/vcard/u")) {
                        // update the contact to Group List
                    }
                    if (message.getType().equals("normal/vcard/d")) {
                        // delete the contact to Group List
                    }*/
                } catch (JSONException e) {

                    jobManager.addJobInBackground(new MessagesJob(messageBody, null, null, false, true));

                    e.printStackTrace();
                }
            } else {
                Utils.printLog("Message : ", "didn't get");
            }
        }
    }
}