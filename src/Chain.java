import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONObject;

public class Chain {

	private String apiKey;
	private final String defualtApiKey = "DEMO-4a5e1e4";

	public Chain() {
		super();
		this.apiKey = this.defualtApiKey;
	}

	public Chain(String apiKey) {
		super();
		this.apiKey = apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	// Addresses
	public JSONObject getAddress(String address) {
		return getJSONObject(generateRequestGET("addresses/" + address, ""));
	}

	public JSONArray getAddressTransactions(String address) {
		return getJSONArray(generateRequestGET("addresses/" + address
				+ "/transactions", ""));
	}

	public JSONArray getAddressTransactions(String address, int limit) {
		return getJSONArray(generateRequestGET("addresses/" + address
				+ "/transactions", "&limit=" + Integer.toString(limit)));
	}

	public JSONArray getAddressUnspents(String address) {
		return getJSONArray(generateRequestGET("addresses/" + address
				+ "/unspents", ""));
	}

	// Transactions
	public JSONObject getTransaction(String hash) {
		return getJSONObject(generateRequestGET("transactions/" + hash, ""));
	}

	public String sendTransaction(String rawTransactionHash) {
		String json = "{\"hex\":\"" + rawTransactionHash + "\"}";
		String answer = generateRequestPUT("transactions", "" ,json);
		return answer;
	}

	// Blocks
	public JSONObject getBlockByHash(String hash) {
		return getJSONObject(generateRequestGET("blocks/" + hash, ""));
	}

	public JSONObject getBlockByHeight(int height) {
		return getJSONObject(generateRequestGET(
				"blocks/" + Integer.toString(height), ""));
	}

	public JSONObject getLatestBlock() {
		return getJSONObject(generateRequestGET("blocks/latest", ""));
	}

	// Privates
	private static final Charset QUERY_CHARSET = Charset.forName("ISO8859-1");

	private String generateRequestGET(String method, String parameters) {
		
		String uri = "https://api.chain.com/v1/bitcoin/" + method + "?key="
				+ this.apiKey + parameters;
		try {

			HttpURLConnection connection = null;
			URL url = new URL(uri);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {

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
			else {
				connection.disconnect();
			}
		} catch (Exception e) {
			// System.out.println("Coudln't connet to Chain.com");
			// System.out.println(e.toString());
		}
		return null;
	}

	private String generateRequestPUT(String method, String parameters,
			String requestBody) {

		String uri = "https://api.chain.com/v1/bitcoin/" + method + "?key="
				+ this.apiKey + parameters;

		HttpURLConnection connection = null;
		
		try {
			URL url = new URL(uri);
			String contentType = "application/json";
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("PUT");
			connection.setRequestProperty("Content-Type", contentType);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Length",
					Integer.toString(requestBody.getBytes().length));
			connection.setUseCaches(true);
			connection.setDoInput(true);
			OutputStream out = connection.getOutputStream();
			out.write(requestBody.getBytes(QUERY_CHARSET));
			out.flush();
			out.close();
		} catch (Exception ioE) {
			connection.disconnect();
			ioE.printStackTrace();
		}

		try {
			connection.connect();
			int responseCode = connection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(is));
				String line;
				StringBuffer response = new StringBuffer();
				while ((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				rd.close();

				String responseToString = response.toString();
				
				connection.disconnect();
				return responseToString;

			} else {
				//System.out.println("Coudln't connet to Chain.com");
				connection.disconnect();
			}
		} catch (Exception e) {
			//System.out.println("Coudln't connet to Chain.com");
			// System.out.println(e.toString());
		}
		return null;
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
