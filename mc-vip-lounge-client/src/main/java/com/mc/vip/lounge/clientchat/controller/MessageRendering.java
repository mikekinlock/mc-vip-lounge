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
import com.mc.vip.lounge.clientchat.gui.ClientGUI;
import com.mc.vip.lounge.clientchat.model.ClientChatRoom;
import com.mc.vip.lounge.clientchat.model.CurrentClient;


public class MessageRendering {

    private Logger CLIENT_GROUP_LOG = Logger.getLogger(MessageRendering.class.getName());

    private static final String CHAT_IDENTIFICATION = "chat_id";
    private static final String CLIENT_MESSAGE_IDENTIFICATION = "client_message";
    private static final String SERVER_MESSAGE_IDENTIFICATION = "server_message_json";
    private static final String SENDER_IDENTIFICATION = "sender_name";
    private static final String JSON_BEGIN_IDENTIFICATION = "{\"";

    private BufferedReader in;
    private PrintWriter out;
    private ClientGUI gui;

    public MessageRendering(final BufferedReader readerIn, final PrintWriter outWriter, final ClientGUI gui) {
        this.in = readerIn;
        this.out = outWriter;
        this.gui = gui;
    }

    public void listenGroupChat() {

        try {
            addGroupChatListener(out);

            boolean runClient = true;

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

                    try (JsonReader serverJsonReader = Json.createReader(new StringReader(line))) {
                        JsonObject serverJson = serverJsonReader.readObject();

                        String senderName = serverJson.getString(SENDER_IDENTIFICATION);
                        JsonObject jsonObject = (JsonObject) serverJson.get(SERVER_MESSAGE_IDENTIFICATION);
                        String message = jsonObject.getString(CLIENT_MESSAGE_IDENTIFICATION);
                        String chatId = "";

                        if (jsonObject.containsKey(CHAT_IDENTIFICATION)) {
                            chatId = jsonObject.getString(CHAT_IDENTIFICATION);
                        }

                        ClientChatRoomsList roomsList = ClientChatRoomsListFactory.getInstance();

                        Optional<ClientChatRoom> room = roomsList.getRoomById(chatId);

                        if (room.isPresent()) {
                            room.get().addMessage(senderName + ": " + message + "\n");
                            gui.setTextAreaText(room.get().getMessages());
                        } else {
                            room = Optional.of(new ClientChatRoom(chatId.split(",")));
                            room.get().addMessage(senderName + ": " + message + "\n");
                            roomsList.getAllClientChatRooms().add(room.get());
                            gui.setTextAreaText(room.get().getMessages());
                            gui.addChatRoomToChatList(room.get().getId(), room.get());
                        }
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
