package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.neo4j.rest.handler;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;

public abstract class HttpResponseHandler<T> implements ResponseHandler<T> {
    abstract public T handle(int responseCode, InputStream content) throws IOException;

    @Override
    public T handleResponse(HttpResponse httpResponse) throws IOException {
        return handle(httpResponse.getStatusLine().getStatusCode(), httpResponse.getEntity().getContent());
    }
}
