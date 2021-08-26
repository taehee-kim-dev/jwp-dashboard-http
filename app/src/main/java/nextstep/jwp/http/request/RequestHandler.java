package nextstep.jwp.http.request;

import nextstep.jwp.controller.Controller;
import nextstep.jwp.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class RequestHandler implements Runnable {

    private static final HttpRequestParser HTTP_REQUEST_PARSER = new HttpRequestParser();
    private static final RequestMapping REQUEST_MAPPING = new RequestMapping();
    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    @Override
    public void run() {
        LOG.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (final InputStream inputStream = connection.getInputStream();
             final OutputStream outputStream = connection.getOutputStream()) {

            final HttpRequest request = HTTP_REQUEST_PARSER.parse(inputStream);
            final RequestLine requestLine = request.getRequestLine();
            final Controller controller = REQUEST_MAPPING.getController(requestLine.getUri());
            final HttpResponse response = getResponse(request, controller);

            doResponse(outputStream, response);
        } catch (IOException exception) {
            LOG.error("Exception stream", exception);
        } finally {
            close();
        }
    }

    private HttpResponse getResponse(HttpRequest request, Controller controller) {
        final HttpResponse response = new HttpResponse();
        try {
            controller.service(request, response);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return response;
    }

    private void doResponse(OutputStream outputStream, HttpResponse response) throws IOException {
        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(Objects.requireNonNull(response).getBytes());
        bufferedOutputStream.flush();
    }

    private void close() {
        try {
            connection.close();
        } catch (IOException exception) {
            LOG.error("Exception closing socket", exception);
        }
    }
}
