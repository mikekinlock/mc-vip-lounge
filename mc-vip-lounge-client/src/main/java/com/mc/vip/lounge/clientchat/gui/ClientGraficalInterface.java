package com.mc.vip.lounge.clientchat.gui;

import com.mc.vip.lounge.clientchat.model.ChatUsers;

import javax.swing.*;

public class ClientGraficalInterface {

    private static ClientGraficalInterface instance;

    private JFrame frame = new JFrame("Chat");
    private JTextField textField = new JTextField(40);
    private JTextArea messageArea = new JTextArea(8, 40);
    //List to show currently all users
    private JList<ChatUsers> list = new JList();

    private ClientGraficalInterface(){
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.getContentPane().add(list,"");
        frame.pack();
    }

    public String getUserName() {
        return JOptionPane.showInputDialog(
                frame,
                "Choose a screen name:",
                "Screen name selection",
                JOptionPane.PLAIN_MESSAGE);
    }

    public static ClientGraficalInterface getInstance(){
        if (instance == null){
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
