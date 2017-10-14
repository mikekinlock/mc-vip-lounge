package com.mc.vip.lounge.server;

import com.mc.vip.lounge.model.ChatUsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonArrayBuilder;

/** A multithreaded chat room server. When a client connects the server requests a screen name by sending the client the
 * text "SUBMITNAME", and keeps requesting a name until a unique one is received. After a client submits a unique name,
 * the server acknowledges with "NAMEACCEPTED". Then all messages from that client will be broadcast to all other
 * clients that have submitted a unique screen name. The broadcast messages are prefixed with "MESSAGE ".
 *
 * Because this is just a teaching example to illustrate a simple chat server, there are a few features that have been
 * left out. Two are very useful and belong in production code:
 *
 * 1. The protocol should be enhanced so that the client can send clean disconnect messages to the server.
 *
 * 2. The server should do some logging. */
public class Server {

    /** The port that the server listens on. */
    public static final int PORT = 9001;

    /** The set of all names of clients in the chat room. Maintained so that we can check that new clients are not
     * registering name already in use. */
    private static List<ChatUsers> names = new CopyOnWriteArrayList<>();

    /** The set of all the print writers for all the clients. This set is kept so we can easily broadcast messages. */
    private static HashSet<PrintWriter> writers = new HashSet<>();

    /** A handler thread class. Handlers are spawned from the listening loop and are responsible for a dealing with a
     * single client and broadcasting its messages. */
    public static class Handler extends Thread {

        private Logger SERVER_LOG = Logger.getLogger(Server.Handler.class.getName());

        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private boolean closed;

        /** Constructs a handler thread, squirreling away the socket. All the interesting work is done in the run
         * method. */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /** Services this thread's client by repeatedly requesting a screen name until a unique one has been submitted,
         * then acknowledges the name and registers the output stream for the client in a global set, then repeatedly
         * gets inputs and broadcasts them. */
        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                boolean newUserAdded;
                JsonObjectBuilder jsonBuilder;

                // Request a name from this client. Keep requesting until
                // a name is submitted that is not already used. Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }

                    if (!names.contains(name)) {
                        names.add(new ChatUsers(name,true));
                        newUserAdded = true;
                        break;
                    }
                }

                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.println("NAMEACCEPTED");
                writers.add(out);

                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {

                    if (newUserAdded) {

                        StringBuilder strB = new StringBuilder();
                        strB.append("USERS:");
                        names.stream()
                                .map(u -> u.getUsername())
                                .forEach(name -> strB.append(name).append(","));

                        for (PrintWriter writer : writers) {
                            writer.println(strB.toString());
                        }

                        newUserAdded = false;
                    }

                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } catch (IOException e) {
                SERVER_LOG.log(Level.WARNING, "Not able to read from Reader: ", e);
            } finally {
                // This client is going down! Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);

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

        public boolean isClosed() {
            return closed;
        }
    }
}
