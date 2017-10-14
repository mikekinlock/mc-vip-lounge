package com.mc.vip.lounge.clientchat.controller;

import static javax.json.Json.createReader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.JsonObject;
import javax.json.JsonReader;

import com.mc.vip.lounge.clientchat.gui.ClientGraficalInterface;

/** A simple Swing-based client for the chat server. Graphically it is a gui with a text field for entering messages
 * and a textarea to see the whole dialog.
 *
 * The client follows the Chat Protocol which is as follows. When the server sends "SUBMITNAME" the client replies with
 * the desired screen name. The server will keep sending "SUBMITNAME" requests as long as the client submits screen
 * names that are already in use. When the server sends a line beginning with "NAMEACCEPTED" the client is now allowed
 * to start sending the server arbitrary strings to be broadcast to all chatters connected to the server. When the
 * server sends a line beginning with "MESSAGE " then all characters following this string should be displayed in its
 * message area. */
public class MessageRendering {

    private Logger CLIENT_GROUP_LOG = Logger.getLogger(MessageRendering.class.getName());

    private BufferedReader in;
    private PrintWriter out;
    private ClientGraficalInterface gui;

    /** Constructs the client by laying out the GUI and registering a listener with the textfield so that pressing
     * Return in the listener sends the textfield contents to the server. Note however that the textfield is initially
     * NOT editable, and only becomes editable AFTER the client receives the NAMEACCEPTED message from the server. */
    public MessageRendering(final BufferedReader readerIn, final PrintWriter outWriter, final ClientGraficalInterface gui) {
        this.in = readerIn;
        this.out = outWriter;
        this.gui = gui;
    }


    public void listenGroupChat(){

        try {
            addGroupChatListener(out);

            boolean runClient = true;

            // Process all messages from server, according to the protocol.
            while (runClient) {
                String line = in.readLine();
                boolean hasLine = line != null;
                if (hasLine && line.startsWith("SUBMITNAME")) {
                    out.println(gui.getUserName());
                } else if (hasLine && line.startsWith("NAMEACCEPTED")) {
                    gui.getTextField().setEditable(true);
                } else if (hasLine && line.startsWith("MESSAGE")) {
                    gui.getMessageArea().append(line.substring(8) + "\n");
                } else if (hasLine && line.startsWith("{\"users\":")){
                    JsonReader jsonReader = createReader(new StringReader(line));
                    JsonObject jsonObject = jsonReader.readObject();
                    String [] users = jsonObject.getString("users").split(",");
                    jsonReader.close();
                } else if (hasLine && line.startsWith("CLOSE")) {
                    runClient = false;
                }
            }
        } catch (IOException e) {
            CLIENT_GROUP_LOG.log(Level.WARNING,"Not able to read from input: ",e);
        }

    }


    private void addGroupChatListener(final PrintWriter out){
        // Add Listeners
        gui.getTextField().addActionListener(new ActionListener() {
            /** Responds to pressing the enter key in the textfield by sending the contents of the text field to the
             * server. Then clear the text area in preparation for the next message. */
            public void actionPerformed(ActionEvent e) {
                out.println(gui.getTextField().getText());
                gui.getTextField().setText("");
            }
        });
    }



}
