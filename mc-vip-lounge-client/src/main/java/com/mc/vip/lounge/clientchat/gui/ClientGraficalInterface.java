package com.mc.vip.lounge.clientchat.gui;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.*;

import com.mc.vip.lounge.clientchat.db.user.ClientChatRoomsList;
import com.mc.vip.lounge.clientchat.db.user.factory.ClientChatRoomsListFactory;
import com.mc.vip.lounge.clientchat.db.user.factory.OnlineUserListFactory;
import com.mc.vip.lounge.clientchat.model.ClientChatRoom;
import com.mc.vip.lounge.clientchat.model.CurrentClient;

public class ClientGraficalInterface {

    private static ClientGraficalInterface instance;

    private JFrame frame = new JFrame("Chat");
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 40);
    // List to show currently all users
    private static DefaultListModel<String> chatRoomNames = new DefaultListModel<>();
    private static JList<String> onlineUsers = new JList<>(OnlineUserListFactory.getInstance().getUserList());
    private static JList<String> guiChatRooms = new JList<>(chatRoomNames);

    private ClientGraficalInterface() {
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

    public static ClientGraficalInterface getInstance() {
        if (instance == null) {
            instance = new ClientGraficalInterface();
        }
        return instance;
    }

    private void addUserListListener() {
        onlineUsers.addListSelectionListener(e -> {

            List<String> selected = onlineUsers.getSelectedValuesList();
            if (selected.size() > 0) {
                selected.add(CurrentClient.getName());

                String createdId = selected.stream()
                        .sorted()
                        .collect(Collectors.joining());

                ClientChatRoomsList chatRooms = ClientChatRoomsListFactory.getInstance();

                Optional<ClientChatRoom> room = chatRooms.getAllClientChatRooms()
                        .stream()
                        .filter(currentRoom -> currentRoom.getId().equals(createdId))
                        .findFirst();

                if (!room.isPresent()) {
                    addChatRoom(selected, true);

                } else {
                    room.get().setSelected(true);
                }
            }
        });
    }

    private void addChatListListener() {
        guiChatRooms.addListSelectionListener(e -> {
            String roomId = guiChatRooms.getSelectedValue();
            Optional<ClientChatRoom> room = ClientChatRoomsListFactory.getInstance().getRoomById(roomId);
            if (room.isPresent()) {
                ClientChatRoomsListFactory.getInstance().nonSelected();
                room.get().setSelected(true);
                setTextAreaText(room.get().getMessages());
            }
        });
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextField getTextField() {
        return textField;
    }

    private void addChatRoom(List<String> selected, boolean isSelected) {
        ClientChatRoomsListFactory.getInstance().nonSelected();
        ClientChatRoom newChatRoom = new ClientChatRoom(selected.stream().toArray(String[]::new));
        addChatRoomToChatList(newChatRoom.getId(),newChatRoom, isSelected);
    }

    public void addChatRoomToChatList(String identifier,ClientChatRoom newChatRoom, boolean isSelected) {
        if (!chatRoomNames.contains(identifier)) {
            ClientChatRoomsListFactory.getInstance().getAllClientChatRooms()
                    .add(newChatRoom);
            newChatRoom.setSelected(isSelected);
            chatRoomNames.addElement(identifier);
        }
    }

    public void setTextAreaText(String textArea) {
        messageArea.setText(textArea);
    }

}
