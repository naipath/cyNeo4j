package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.general;

import nl.maastrichtuniversity.MyHttpResponseHandler;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;

public class ReturnCodeResponseHandler extends MyHttpResponseHandler<Boolean> {

    @Override
    public Boolean handle(int responseCode, InputStream content) throws IOException {
        return (responseCode >= 200 && responseCode < 300);
    }
    
}
