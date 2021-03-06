package com.mc.vip.lounge.clientchat.gui.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.*;

import com.mc.vip.lounge.clientchat.db.user.ClientChatRoomsList;
import com.mc.vip.lounge.clientchat.db.user.factory.ClientChatRoomsListFactory;
import com.mc.vip.lounge.clientchat.db.user.factory.OnlineUserListFactory;
import com.mc.vip.lounge.clientchat.gui.ClientGUI;
import com.mc.vip.lounge.clientchat.model.ClientChatRoom;
import com.mc.vip.lounge.clientchat.model.CurrentClient;

public class ClientGUIImpl implements ClientGUI {

    private JFrame frame = new JFrame("Chat");
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 40);
    private static DefaultListModel<String> chatRoomNames = new DefaultListModel<>();
    private static JList<String> onlineUsers = new JList<>(OnlineUserListFactory.getInstance().getUserList());
    private static JList<String> guiChatRooms = new JList<>(chatRoomNames);

    public ClientGUIImpl() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(onlineUsers, "East");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(guiChatRooms, "West");
        frame.pack();

        addUserListListener();
        addChatListListener();
    }

    public String getUserName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    public void setTextAreaText(final String textArea) {
        messageArea.setText(textArea);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void addChatRoomToChatList(final String identifier, final ClientChatRoom newChatRoom) {
        if (!chatRoomNames.contains(identifier)) {
            ClientChatRoomsListFactory.getInstance().getAllClientChatRooms()
                    .add(newChatRoom);
            chatRoomNames.addElement(identifier);
            setTextAreaText(newChatRoom.getMessages());
        }
    }

    private ClientChatRoom addChatRoom(final List<String> selected) {
        ClientChatRoomsListFactory.getInstance().nonSelected();
        ClientChatRoom newChatRoom = new ClientChatRoom(selected.stream().toArray(String[]::new));
        addChatRoomToChatList(newChatRoom.getId(), newChatRoom);
        return newChatRoom;
    }

    private void addChatListListener() {
        guiChatRooms.addListSelectionListener(e -> {
            String roomId = guiChatRooms.getSelectedValue();
            ClientChatRoomsList roomsList = ClientChatRoomsListFactory.getInstance();
            Optional<ClientChatRoom> room = roomsList.getRoomById(roomId);
            if (room.isPresent()) {
                roomsList.nonSelected();
                room.get().setSelected(true);
                setTextAreaText(room.get().getMessages());
            }
        });
    }

    private void addUserListListener() {
        onlineUsers.addListSelectionListener(e -> {

            List<String> selected = onlineUsers.getSelectedValuesList();
            if (selected.size() > 0) {
                selected.add(CurrentClient.getName());

                String createdId = selected.stream()
                        .sorted()
                        .collect(Collectors.joining(","));

                ClientChatRoomsList chatRooms = ClientChatRoomsListFactory.getInstance();

                Optional<ClientChatRoom> room = chatRooms.getAllClientChatRooms()
                        .stream()
                        .filter(currentRoom -> currentRoom.getId().equals(createdId))
                        .findFirst();

                if (!room.isPresent()) {
                   addChatRoom(selected);
                } else {
                    room.get().setSelected(true);
                }
            }
        });
    }

}
