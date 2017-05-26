package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;

public abstract class MyHttpResponseHandler<T> implements ResponseHandler<T> {
    abstract public T handle(int responseCode, InputStream content) throws IOException;

    @Override
    public T handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
        return handle(httpResponse.getStatusLine().getStatusCode(), httpResponse.getEntity().getContent());
    }
}
