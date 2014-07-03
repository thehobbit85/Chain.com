import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CheckSecurity {

	private static final String rootCert = "https://chain.com/chain.pem";

	public static String checkCertificate() {
		String answer = null;
		HttpsURLConnection connection = null;
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0],
					new TrustManager[] { new DefaultTrustManager() },
					new SecureRandom());
			SSLContext.setDefault(ctx);
			URL url = new URL("https://api.chain.com");
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});

			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;

				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line + "\n");
				}
				reader.close();
				answer = stringBuilder.toString();
			}
			answer = null;
		} catch (Exception e) {
			connection.disconnect();
			answer = null;
		}
		return answer;
	}

	private static class DefaultTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// do nothing, you're the client
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			
			for (X509Certificate cert : chain) {
				System.out.println(cert.toString());
			}
			KeyStore ks;
			try {
				ks = KeyStore.getInstance("JKS");
				FileInputStream in = new FileInputStream("chain.pem");
				ks.load(in, "changeit".toCharArray());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			 * chain[chain.length -1] is the candidate for the root certificate.
			 * Look it up to see whether it's in your list. If not, ask the user
			 * for permission to add it. If not granted, reject. Validate the
			 * chain using CertPathValidator and your list of trusted roots.
			 */
		}
	}

	public static boolean verified() {
		// TODO Auto-generated method stub
		return true;
	}
}
