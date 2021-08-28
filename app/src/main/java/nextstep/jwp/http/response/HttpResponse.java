package nextstep.jwp.http.response;


import nextstep.jwp.staticresource.StaticResource;

import java.nio.charset.StandardCharsets;

import static nextstep.jwp.common.LineSeparator.NEW_LINE;

public class HttpResponse {

    private final ResponseHeaders headers;
    private StatusLine statusLine;
    private String body;

    public HttpResponse() {
        headers = new ResponseHeaders();
    }

    public void assignStatus(ResponseStatus responseStatus) {
        statusLine = new StatusLine(responseStatus);
    }

    public void addStaticResource(StaticResource staticResource) {
        body = staticResource.getContent();
        putHeaders(staticResource.getContentType());
    }

    private void putHeaders(ContentType contentType) {
        headers.put("Content-Type", contentType.getValue() + ";charset=utf-8");
        headers.put("Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));
    }

    public void assignLocationHeader(String locationHeader) {
        headers.put("Location", locationHeader);
    }

    public byte[] getBytes() {
        if (body == null) {
            return getResponseAsBytesWithEmptyBody();
        }
        return getResponseAsBytesWithBody();
    }

    private byte[] getResponseAsBytesWithEmptyBody() {
        return String.join(NEW_LINE,
                        statusLine.toString(),
                        headers.toString())
                .getBytes(StandardCharsets.UTF_8);
    }

    private byte[] getResponseAsBytesWithBody() {
        return String.join(NEW_LINE,
                        statusLine.toString(),
                        headers.toString(),
                        "",
                        body)
                .getBytes(StandardCharsets.UTF_8);
    }
}
