package nl.maastrichtuniversity.networklibrary.cyneo4j.internal.serviceprovider.extension;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

public class ExtensionLocationsHandler implements ResponseHandler<Set<String>>{

	@Override
	public Set<String> handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		Set<String> res = null;
		int responseCode = response.getStatusLine().getStatusCode();
		if(responseCode >= 200 && responseCode < 300){
			ObjectMapper mapper = new ObjectMapper();
			Map<String,String> instanceResp = mapper.readValue(response.getEntity().getContent(),Map.class);

			res = instanceResp.keySet();
		}

		return res;
	}

}