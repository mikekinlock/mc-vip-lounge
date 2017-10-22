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
    private static JList<String> list = new JList<>(OnlineUserListFactory.getInstance().getUserList());

    private ClientGraficalInterface() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(list, "East");
        frame.pack();

        addListListener();
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

    private void addListListener() {
        list.addListSelectionListener(e -> {

            List<String> selected = list.getSelectedValuesList();
            selected.add(CurrentClient.getName());

            ClientChatRoom chatRoom = null;

            String createdId = selected.stream()
                    .sorted()
                    .collect(Collectors.joining());

            ClientChatRoomsList chatRooms = ClientChatRoomsListFactory.getInstance();

            Optional<ClientChatRoom> room = chatRooms.getAllClientChatRooms()
                    .stream()
                    .filter(r -> r.getId().equals(createdId))
                    .findFirst();

            if (!room.isPresent()) {
                chatRooms.nonSelected();
                chatRoom = new ClientChatRoom(selected.stream().toArray(String[]::new));
                chatRooms.getAllClientChatRooms()
                         .add(chatRoom);
            } else {
                chatRoom = room.get();
                chatRoom.setSelected(true);
            }

        });
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

}
