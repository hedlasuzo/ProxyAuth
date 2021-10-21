package proxyauth;

import proxyauth.actions.Action;
import proxyauth.actions.ForwardAction;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Configuration is currently hard coded
 *
 * @author Zeckie
 * Copyright and licence details in Main.java
 */
public class Configuration {
    private Configuration() {
    }

    public static final int BUF_SIZE = 10240; // bytes
    public static final boolean DEBUG = true;
    public static final int SOCKET_TIMEOUT = 180000; // milliseconds
    public static final int LISTEN_BACKLOG = 5;
    public static final int LISTEN_PORT = 8080;
    public static Action INITIAL_ACTION;

    /**
     * Immediate stop on http error 407
     * (prevent account from being locked due to multiple attempts with wrong password)
     */
    public static final boolean STOP_ON_PROXY_AUTH_ERROR = true;

    static InetAddress listenAddress = null;
    public static final int MAX_ACTIVE_REQUESTS = 20;

    static void init() throws IOException {
        listenAddress = InetAddress.getByName("127.0.0.127");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String currentUser = System.getenv("USERNAME");
        if (currentUser == null) {
            System.out.println("Username:");
        } else {
            System.out.println("Username (press ENTER for '" + currentUser + "'):");
        }
        String username = br.readLine();
        if (username.length() == 0 && currentUser != null) {
            username = currentUser;
        }
        Console c = System.console();
        String password;
        if (c == null) {
            System.out.println("Password (not masked!)");
            password = br.readLine();
        } else {
            System.out.println("Password (masked)");
            char[] pass = c.readPassword();
            password = new String(pass);
        }

        INITIAL_ACTION = new ForwardAction(InetAddress.getByName("proxy.example.com"), 8080, username, password);
    }
}