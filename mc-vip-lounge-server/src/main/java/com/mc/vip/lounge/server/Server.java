package com.mc.vip.lounge.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import com.mc.vip.lounge.db.connection.ChatRoomsConnection;
import com.mc.vip.lounge.db.connection.factory.ChatRoomConnectionFactory;
import com.mc.vip.lounge.db.connection.factory.UserConnectionFactory;
import com.mc.vip.lounge.model.ChatRoom;
import com.mc.vip.lounge.model.ChatUsers;

/** A multithreaded chat room server. When a client connects the server requests a screen name by sending the client the text "SUBMITNAME",
 * and keeps requesting a name until a unique one is received. After a client submits a unique name, the server acknowledges with
 * "NAMEACCEPTED". Then all messages from that client will be broadcast to all other clients that have submitted a unique screen name. The
 * broadcast messages are prefixed with "MESSAGE ".
 *
 * Because this is just a teaching example to illustrate a simple chat server, there are a few features that have been left out. Two are
 * very useful and belong in production code:
 *
 * 1. The protocol should be enhanced so that the client can send clean disconnect messages to the server.
 *
 * 2. The server should do some logging. */
public class Server {

    /** The port that the server listens on. */
    public static final int PORT = 9001;

    /** The set of all names of clients in the chat room. Maintained so that we can check that new clients are not registering name already
     * in use. */
    private static List<ChatUsers> names = UserConnectionFactory.getInstance().getAllUsers();

    /** The set of all the print writers for all the clients. This set is kept so we can easily broadcast messages. */
    private static Map<String,PrintWriter> writers = new HashMap<>();

    /** A handler thread class. Handlers are spawned from the listening loop and are responsible for a dealing with a single client and
     * broadcasting its messages. */
    public static class Handler extends Thread {

        private Logger SERVER_LOG = Logger.getLogger(Server.Handler.class.getName());

        private static final String USER_NAME = "new_user_name";
        private static final String CLIENT_ACCEPTED_INFO = "name_accepted";
        private static final String SERVER_MESSAGE_IDENTIFICATION = "server_message_json";
        private static final String SENDER_IDENTIFICATION = "sender_name";
        private static final String CLIENT_MESSAGE_IDENTIFICATION = "client_message";
        private static final String CHAT_IDENTIFICATION = "chat_id";

        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private boolean closed;
        private boolean newUserAdded;

        /** Constructs a handler thread, squirreling away the socket. All the interesting work is done in the run method. */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /** Services this thread's client by repeatedly requesting a screen name until a unique one has been submitted, then acknowledges
         * the name and registers the output stream for the client in a global set, then repeatedly gets inputs and broadcasts them. */
        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Request a name from this client. Keep requesting until
                // a name is submitted that is not already used. Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.println(USER_NAME);
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }

                    if (!names.contains(name)) {
                        names.add(new ChatUsers(name, true));
                        newUserAdded = true;
                        break;
                    }
                }

                // After a user has been successful created and added we'll add
                // a write to each user.
                out.println(CLIENT_ACCEPTED_INFO);
                writers.put(name,out);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {

                    if (newUserAdded) {
                        updateUserList();
                    }

                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (Map.Entry<String,PrintWriter> writerEntry : writers.entrySet()) {

                        JsonObject json;
                        JsonObjectBuilder builder;
                        try (JsonReader reader = Json.createReader(new StringReader(input))) {
                            json = reader.readObject();
                        }

                        String chatId = json.containsKey(CHAT_IDENTIFICATION)
                                ? json.getString(CHAT_IDENTIFICATION)
                                : "";

                        ChatRoomsConnection chatRoomsConnection = ChatRoomConnectionFactory.getInstance();
                        Optional<ChatRoom> room = chatRoomsConnection.getChatRoomById(chatId);


                        if(!room.isPresent()) {
                            room = Optional.of(new ChatRoom(chatId.split(",")));
                        }

                        room.get().addMessage(json.getString(CLIENT_MESSAGE_IDENTIFICATION));

                        builder = Json.createObjectBuilder();
                        builder.add(SENDER_IDENTIFICATION, name);
                        builder.add(SERVER_MESSAGE_IDENTIFICATION , json);

                        if(room.get().containsUser(writerEntry.getKey())){
                            writerEntry.getValue().println(builder.build().toString());
                        }

                    }
                }
            } catch (IOException e) {
                SERVER_LOG.log(Level.WARNING, "Not able to read from Reader: ", e);
            } finally {
                // This client is going down! Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                    updateUserList();
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                    closed = true;
                } catch (IOException e) {
                }
            }
        }

        //Todo: Change user list creation to JSON
        private void updateUserList() {
            StringBuilder strB = new StringBuilder();
            strB.append("USERS:");
            names.stream()
                    .map(u -> u.getUsername())
                    .forEach(name -> strB.append(name).append(","));

            for (Map.Entry<String,PrintWriter> writerEntry : writers.entrySet()) {
                writerEntry.getValue().println(strB.toString());
            }

            newUserAdded = false;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}
