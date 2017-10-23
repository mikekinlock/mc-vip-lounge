package com.mc.vip.lounge.clientchat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mc.vip.lounge.clientchat.db.user.ClientChatRoomsList;
import com.mc.vip.lounge.clientchat.db.user.factory.ClientChatRoomsListFactory;
import com.mc.vip.lounge.clientchat.db.user.factory.OnlineUserListFactory;
import com.mc.vip.lounge.clientchat.gui.ClientGraficalInterface;
import com.mc.vip.lounge.clientchat.model.ClientChatRoom;
import com.mc.vip.lounge.clientchat.model.CurrentClient;

/** A simple Swing-based client for the chat server. Graphically it is a gui with a text field for entering messages and a textarea to see
 * the whole dialog.
 *
 * The client follows the Chat Protocol which is as follows. When the server sends "SUBMITNAME" the client replies with the desired screen
 * name. The server will keep sending "SUBMITNAME" requests as long as the client submits screen names that are already in use. When the
 * server sends a line beginning with "NAMEACCEPTED" the client is now allowed to start sending the server arbitrary strings to be broadcast
 * to all chatters connected to the server. When the server sends a line beginning with "MESSAGE " then all characters following this string
 * should be displayed in its message area. */
public class MessageRendering {

    private Logger CLIENT_GROUP_LOG = Logger.getLogger(MessageRendering.class.getName());

    private static final String CHAT_IDENTIFICATION = "CHATID";
    private static final String MESSAGE_IDENTIFICATION = "MESSAGE";
    private static final String SENDER_IDENTIFICATION = "SENDERNAME";

    private BufferedReader in;
    private PrintWriter out;
    private ClientGraficalInterface gui;

    /** Constructs the client by laying out the GUI and registering a listener with the textfield so that pressing Return in the listener
     * sends the textfield contents to the server. Note however that the textfield is initially NOT editable, and only becomes editable
     * AFTER the client receives the NAMEACCEPTED message from the server. */
    public MessageRendering(final BufferedReader readerIn, final PrintWriter outWriter, final ClientGraficalInterface gui) {
        this.in = readerIn;
        this.out = outWriter;
        this.gui = gui;
    }

    public void listenGroupChat() {

        try {
            addGroupChatListener(out);

            boolean runClient = true;

            // Process all messages from server, according to the protocol.
            while (runClient) {
                String line = in.readLine();
                boolean hasLine = line != null;
                if (hasLine && line.startsWith("SUBMITNAME")) {
                    String name = gui.getUserName();
                    CurrentClient.setName(name);
                    out.println(name);
                } else if (hasLine && line.startsWith("NAMEACCEPTED")) {
                    gui.getTextField().setEditable(true);
                } else if (hasLine && line.startsWith(SENDER_IDENTIFICATION)) {
                    // Create logic to check if room already exist or if room has to be created and add message to textarea
                    int messageIndex = line.indexOf(MESSAGE_IDENTIFICATION)+7;
                    if (line.contains(CHAT_IDENTIFICATION)) {
                        int chatIndex = line.indexOf(CHAT_IDENTIFICATION);
                        String senderName = line.substring(SENDER_IDENTIFICATION.length(),chatIndex);
                        String chatId = line.substring(chatIndex+6, messageIndex-7) + "\n";
                        Optional<ClientChatRoom> room = ClientChatRoomsListFactory.getInstance().getRoomById(chatId);
                        if (room.isPresent()) {
                            room.get().getMessageArea().append(senderName+line.substring(messageIndex) + "\n");
                        } else {
                            room = Optional.of(new ClientChatRoom(chatId.split(",")));
                            ClientChatRoomsListFactory.getInstance().getAllClientChatRooms().add(room.get());
                            room.get().getMessageArea().append(senderName+line.substring(messageIndex) + "\n");
                        }

                    } else {
                        String senderName = line.substring(SENDER_IDENTIFICATION.length(),messageIndex-7);
                        gui.getMessageArea().append(senderName+line.substring(messageIndex) + "\n");
                    }
                } else if (hasLine && line.startsWith("USERS:")) {
                    String usersString = line.substring(6);
                    String[] userList = usersString.split(",");
                    OnlineUserListFactory.getInstance().updateUserList(userList);
                } else if (hasLine && line.startsWith("CLOSE")) {
                    runClient = false;
                }
            }
        } catch (IOException e) {
            CLIENT_GROUP_LOG.log(Level.WARNING, "Not able to read from input: ", e);
        }

    }

    private void addGroupChatListener(final PrintWriter out) {
        // Add Listeners
        gui.getTextField().addActionListener(new ActionListener() {
            /** Responds to pressing the enter key in the textfield by sending the contents of the text field to the server. Then clear the
             * text area in preparation for the next message. */
            public void actionPerformed(ActionEvent e) {
                StringBuilder stringBuilder = new StringBuilder();
                ClientChatRoomsList roomsList = ClientChatRoomsListFactory.getInstance();
                Optional<ClientChatRoom> optionalChatRoom = roomsList.getSelectedChatRoom();

                if (optionalChatRoom.isPresent()) {
                    ClientChatRoom chatRoom = optionalChatRoom.get();
                    stringBuilder.append(CHAT_IDENTIFICATION);
                    stringBuilder.append(chatRoom.getId());
                }

                stringBuilder.append(MESSAGE_IDENTIFICATION);
                stringBuilder.append(gui.getTextField().getText());

                out.println(stringBuilder.toString());
                gui.getTextField().setText("");
            }
        });
    }

}
