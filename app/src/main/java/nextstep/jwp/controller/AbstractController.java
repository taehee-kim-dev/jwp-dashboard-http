package nextstep.jwp.controller;

import nextstep.jwp.http.request.HttpRequest;
import nextstep.jwp.http.response.HttpResponse;
import nextstep.jwp.staticresource.StaticResource;
import nextstep.jwp.staticresource.StaticResourceFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractController implements Controller {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractController.class);
    protected static final StaticResourceFinder STATIC_RESOURCE_FINDER = new StaticResourceFinder();

    protected void doPost(HttpRequest request, HttpResponse response) {
    }

    protected void doGet(HttpRequest request, HttpResponse response) {
    }

    protected StaticResource getStaticResource(String filePath) {
        return STATIC_RESOURCE_FINDER.findStaticResource(filePath);
    }
}

