import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CheckSecurity {

	public static boolean checkCertificate() {

		HttpsURLConnection connection = null;
		try {
			DefaultTrustManager trustManager = new DefaultTrustManager();
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0],
					new TrustManager[] { trustManager },
					new SecureRandom());
			SSLContext.setDefault(ctx);
			URL url = new URL("https://api.chain.com");
			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
	
			connection.connect();
			if (trustManager.isServerTrusted()) 
			{
				connection.disconnect();
				
				return true;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			connection.disconnect();
			return false;
		}
		connection.disconnect();
		return false;
	}
	
	private static class DefaultTrustManager implements X509TrustManager {

		private boolean trusted = false;
		
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// do nothing, you're the client
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {

			InputStream inStream = null;
			try {
				// Loading the CA cert
				URL u = getClass().getResource("chain.pem");
				inStream = new FileInputStream(u.getFile());
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				X509Certificate ca = (X509Certificate) cf
						.generateCertificate(inStream);

				//chain[1].verify(ca.getPublicKey());
				// Verifing by public key
				chain[2].verify(ca.getPublicKey());
				this.trusted = true;
			} catch (Exception e) {
				System.out.println(e.toString());
			} finally {
				try {
					inStream.close();
				} catch (Exception e) {
					System.out.println(e.toString());		
				}
			}
		}
		
		public boolean isServerTrusted() {
			return this.trusted;
		}
	}
}
