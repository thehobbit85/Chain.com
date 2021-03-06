import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONArray;
import org.json.JSONObject;

public class Chain {

	private String apiKey;
	private String chainUri = "https://api.chain.com/v1/";
	private String network = "bitcoin/";
	private String baseUri = this.chainUri + this.network;
	private HttpsURLConnection connection = null;

	// Initiators
	public static Chain getChain()  {
		if (CheckSecurity.checkCertificate()) 
			return new Chain();
		else 
			return null;
	}
	
	public static Chain getChain(String apiKey)  {
		if (CheckSecurity.checkCertificate()) 
			return new Chain(apiKey);
		else 
			return null;
	}
	
	// Addresses
	public JSONObject getAddress(String address) {

		return getJSONObject(generateRequestGET("addresses/" + address));

	}

	public JSONArray getAddressTransactions(String address) {
		return getJSONArray(generateRequestGET("addresses/" + address
				+ "/transactions"));
	}

	public JSONArray getAddressTransactions(String address, int limit) {
		return getJSONArray(generateRequestGET("addresses/" + address
				+ "/transactions?limit=" + Integer.toString(limit)));
	}

	public JSONArray getAddressUnspents(String address) {
		return getJSONArray(generateRequestGET("addresses/" + address
				+ "/unspents"));
	}

	// Transactions
	public JSONObject getTransaction(String hash) {
		return getJSONObject(generateRequestGET("transactions/" + hash));
	}

	public JSONObject getTransactionOpRerturn(String hash) {
		return getJSONObject(generateRequestGET("transactions/" + hash + "/op-return"));
	}

	public String sendTransaction(String rawTransactionHash) {
		String json = "{\"hex\":\"" + rawTransactionHash + "\"}";
		String answer = generateRequestPUT("transactions", json);
		return answer;
	}

	// Blocks
	public JSONObject getBlockByHash(String hash) {
	
			return getJSONObject(generateRequestGET("blocks/" + hash));
		
	}

	public JSONObject getBlockByHeight(int height) {
		
			return getJSONObject(generateRequestGET(
					"blocks/" + Integer.toString(height)));
	}

	public JSONObject getLatestBlock() {
		return getJSONObject(generateRequestGET("blocks/latest"));
	}

	// Others
	public void setApiKey(String apiKey) {
		this.apiKey = "?key=" + apiKey;
	}
	
	public void useMainNet() {
		this.network = "bitcoin/";
	}
	
	public void useTestNet3() {
		this.network = "testnet3/";
	}
	
	// Privates
	private static final Charset QUERY_CHARSET = Charset.forName("ISO8859-1");

	private Chain() {
		super();
		this.apiKey = "Basic " + DatatypeConverter.printBase64Binary("DEMO-4a5e1e4".getBytes());
	}

	private Chain(String apiKey)  {
		super();
		this.apiKey = "Basic " + DatatypeConverter.printBase64Binary(apiKey.getBytes());
			
	}

	private String generateRequestGET(String api) {
		try {
			URL url = new URL(this.baseUri + api);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", this.apiKey);
		
			return getResopnse();
		} catch (Exception e) {
			return null;
		}
	}

	private String generateRequestPUT(String api,
			String requestBody) {
		try {
			URL url = new URL(this.baseUri + api);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", this.apiKey);

			OutputStream out = connection.getOutputStream();
			out.write(requestBody.getBytes(QUERY_CHARSET));
			out.flush();
			out.close();
			return getResopnse();
		} catch (Exception ioE) {
			ioE.printStackTrace();
			return null;
		}
	}

	private String getResopnse() {
		try {
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line + "\n");
				}
				reader.close();
				connection.disconnect();
				return stringBuilder.toString();
			}
			return null;
		} catch (Exception e) {
			connection.disconnect();
			return null;
		}
	}

	private JSONObject getJSONObject(String input) {

		try {
			JSONObject answer = new JSONObject(input);
			return answer;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}

	private JSONArray getJSONArray(String input) {

		try {
			JSONArray answer = new JSONArray(input);
			return answer;
		} catch (Exception e1) {
			System.out.println(e1.toString());
			return null;
		}
	}

	
}
