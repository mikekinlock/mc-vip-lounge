package com.mc.vip.lounge.clientchat.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

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

    private static final String ALL_USERS_CHAT = "all_users";
    private static final String CHAT_IDENTIFICATION = "chat_id";
    private static final String CLIENT_MESSAGE_IDENTIFICATION = "client_message";
    private static final String SERVER_MESSAGE_IDENTIFICATION = "server_message_json";
    private static final String SENDER_IDENTIFICATION = "sender_name";
    private static final String JSON_BEGIN_IDENTIFICATION = "{\"";

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
                } else if (hasLine && line.startsWith(JSON_BEGIN_IDENTIFICATION + SENDER_IDENTIFICATION)) {
                    // Create logic to check if room already exist or if room has to be created and add message to textarea
                    try (JsonReader serverJsonReader = Json.createReader(new StringReader(line))) {
                        JsonObject serverJson = serverJsonReader.readObject();

                        String senderName = serverJson.getString(SENDER_IDENTIFICATION);
                        JsonObject jsonObject = (JsonObject) serverJson.get(SERVER_MESSAGE_IDENTIFICATION);
                        String message = jsonObject.getString(CLIENT_MESSAGE_IDENTIFICATION);
                        String chatId = "";

                        if (jsonObject.containsKey(CHAT_IDENTIFICATION)) {
                            chatId = jsonObject.getString(CHAT_IDENTIFICATION);
                        }

                        Optional<ClientChatRoom> room = ClientChatRoomsListFactory.getInstance().getRoomById(chatId);
                        if (room.isPresent()) {
                            room.get().addMessage(senderName + ": " + message + "\n");
                            gui.setTextAreaText(room.get().getMessages());
                        } else {
                            room = Optional.of(new ClientChatRoom(chatId.split(",")));
                            room.get().addMessage(senderName + ": " + message + "\n");
                            ClientChatRoomsListFactory.getInstance().getAllClientChatRooms().add(room.get());
                            gui.setTextAreaText(room.get().getMessages());
                        }
                    }
                } else if (hasLine && line.startsWith("USERS:")) {
                    String usersString = line.substring(6);
                    String[] userList = usersString.split(",");
                    OnlineUserListFactory.getInstance().updateUserList(userList);

                    ClientChatRoomsList chatRoomsList = ClientChatRoomsListFactory.getInstance();

                    if (chatRoomsList.getAllClientChatRooms().size() == 0) {
                        ClientChatRoom room = new ClientChatRoom(CurrentClient.getName(), usersString);
                        room.setName(ALL_USERS_CHAT);
                        chatRoomsList.getAllClientChatRooms().add(room);
                    } else {
                        Optional<ClientChatRoom> room = chatRoomsList.getRoomByName(ALL_USERS_CHAT);
                        if (room.isPresent()) {
                            room.get().addUsers(CurrentClient.getName(), usersString);
                        }
                    }

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
        gui.getTextField().addActionListener(e -> {
            ClientChatRoomsList roomsList = ClientChatRoomsListFactory.getInstance();
            Optional<ClientChatRoom> optionalChatRoom = roomsList.getSelectedChatRoom();
            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

            jsonBuilder.add(CLIENT_MESSAGE_IDENTIFICATION, gui.getTextField().getText());
            if (optionalChatRoom.isPresent()) {
                ClientChatRoom chatRoom = optionalChatRoom.get();
                jsonBuilder.add(CHAT_IDENTIFICATION, chatRoom.getId());
            }

            out.println(jsonBuilder.build().toString());
            gui.getTextField().setText("");
        });
    }

}
