package com.mc.vip.lounge.clientchat.gui;

import javax.swing.*;

import com.mc.vip.lounge.clientchat.db.user.factory.OnlineUserListFactory;

public class ClientGraficalInterface {

    private static ClientGraficalInterface instance;

    private JFrame frame = new JFrame("Chat");
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 40);
    // List to show currently all users
    private static JList<String> list;


    private ClientGraficalInterface() {
        list = new JList<>(OnlineUserListFactory.getInstance().getUserList());
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(list,"East");
        frame.pack();
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
