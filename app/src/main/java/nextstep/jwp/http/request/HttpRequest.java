package nextstep.jwp.http.request;


public class HttpRequest {

    private final RequestLine requestLine;
    private final RequestHeaders headers;
    private final RequestBody body;

    public HttpRequest(RequestLine requestLine, RequestHeaders headers, RequestBody body) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public boolean hasMethod(HttpMethod method) {
        return requestLine.hasMethod(method);
    }

    public boolean hasUri(String uri) {
        return requestLine.hasUri(uri);
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestHeaders getHeaders() {
        return headers;
    }

    public RequestBody getBody() {
        return body;
    }

    public boolean isUriPatternMatchedTo(String pattern) {
        return requestLine.isUriPatternMatchedTo(pattern);
    }
}
