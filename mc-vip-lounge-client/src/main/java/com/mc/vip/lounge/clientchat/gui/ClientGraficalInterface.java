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
    private static DefaultListModel<String> chatNames = new DefaultListModel<>();
    private static JList<String> onlineUsers = new JList<>(OnlineUserListFactory.getInstance().getUserList());
    private static JList<String> chatRooms = new JList<>(chatNames);

    private ClientGraficalInterface() {
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(onlineUsers, "East");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(chatRooms, "West");
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
                addChatRoom(chatRooms,selected,true);

            } else {
                room.get().setSelected(true);
            }
        });
    }

    private void addChatListListener() {
        chatRooms.addListSelectionListener(e -> {
            String roomId = chatRooms.getSelectedValue();
            Optional<ClientChatRoom> room = ClientChatRoomsListFactory.getInstance().getRoomById(roomId);
            if(room.isPresent()){
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

    public void addChatRoom(ClientChatRoomsList chatRooms, List<String> selected, boolean isSelected){
        chatRooms.nonSelected();
        ClientChatRoom newChatRoom = new ClientChatRoom(selected.stream().toArray(String[]::new));
        chatRooms.getAllClientChatRooms()
                .add(newChatRoom);
        newChatRoom.setSelected(isSelected);
        chatNames.addElement(newChatRoom.getId());
    }

    public void setTextAreaText(String textArea){
        messageArea.setText(textArea);
    }

}
