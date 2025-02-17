package nextstep.jwp.server;

import nextstep.jwp.http.request.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Stream;

public class WebServer {

    private static final Logger LOG = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    private final int port;
    private final ApplicationContext applicationContext;

    public WebServer(int port) {
        this.port = checkPort(port);
        applicationContext = new ApplicationContext();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOG.info("Web Server started {} port.", serverSocket.getLocalPort());
            handle(serverSocket);
        } catch (IOException exception) {
            LOG.error("Exception accepting connection", exception);
        } catch (RuntimeException exception) {
            LOG.error("Unexpected error", exception);
        }
    }

    private void handle(ServerSocket serverSocket) throws IOException {
        Socket connection;
        while ((connection = serverSocket.accept()) != null) {
            final RequestHandler requestHandler = applicationContext.getRequestHandler();
            final Socket finalConnection = connection;
            new Thread(() -> requestHandler.run(finalConnection)).start();
        }
    }

    public static int defaultPortIfNull(String[] args) {
        return Stream.of(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(WebServer.DEFAULT_PORT);
    }

    private int checkPort(int port) {
        if (port < 1 || 65535 < port) {
            return DEFAULT_PORT;
        }
        return port;
    }
}
