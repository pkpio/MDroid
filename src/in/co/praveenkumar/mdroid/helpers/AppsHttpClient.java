package in.co.praveenkumar.mdroid.helpers;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class AppsHttpClient {
	private DefaultHttpClient AppHttpClient = null;
	private SchemeRegistry schreg = new SchemeRegistry();

	public AppsHttpClient() {
		HttpParams httpParams = new BasicHttpParams();
		// set timeout in milliseconds until a connection is established
		int timeoutConnection = 30000;
		HttpConnectionParams.setSoTimeout(httpParams, timeoutConnection);

		schreg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schreg.register(new Scheme("https", PlainSocketFactory
				.getSocketFactory(), 80));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(
				httpParams, schreg);
		AppHttpClient = new DefaultHttpClient(cm, httpParams);
	}

	public DefaultHttpClient getHttpClient() {
		return AppHttpClient;
	}

}
