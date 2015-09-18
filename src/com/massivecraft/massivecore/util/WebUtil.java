package com.massivecraft.massivecore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebUtil
{
	// -------------------------------------------- //
	// CONSTANTS
	// -------------------------------------------- //
	
	public final static String USER_AGENT = "MassiveCore/1.2.3";
	public final static int CONNECT_TIMEOUT = 10000;
	
	// -------------------------------------------- //
	// TOUCH
	// -------------------------------------------- //
	
	private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
	
	public static void touch(String url, boolean synchronous)
	{
		try
		{
			touch(new URL(url), synchronous);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void touch(final URL url, boolean synchronous)
	{
		if (synchronous)
		{
			touch(url);
		}
		else
		{
			EXECUTOR.execute(new Runnable()
			{
				@Override
				public void run()
				{
					touch(url);
				}
			});
		}
	}
	
	protected static void touch(final URL url)
	{
		HttpURLConnection uc = null;
		InputStream is = null;
		try
		{
			uc = openHttpUrlConnection(url);
			uc.connect();
			is = uc.getInputStream();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try { is.close(); } catch (Exception ignored) {}
			try { uc.disconnect(); } catch (Exception ignored) {}
		}
	}
	
	// -------------------------------------------- //
	// GET URL LINES
	// -------------------------------------------- //
	
	// This one should be run async since it's very slow.
	public static List<String> getLines(URL url) throws IOException
	{
		HttpURLConnection uc = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try
		{
			uc = openHttpUrlConnection(url);
			uc.connect();
			
			is = uc.getInputStream();
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			
			List<String> lines = new ArrayList<String>();
			String line = null;
			while ( (line = br.readLine()) != null )
			{
				lines.add(line);
			}
			
			return lines;
		}
		finally
		{
			// Closing BufferedReader does close InputStreamReader does close InputStream.
			try { br.close(); } catch (Exception ignored) {}
			// However documentation suggested we must disconnect the HttpURLConnection manually.
			try { uc.disconnect(); } catch (Exception ignored) {}
		}
	}
	
	// -------------------------------------------- //
	// UTIL
	// -------------------------------------------- //
	
	public static HttpURLConnection openHttpUrlConnection(URL url) throws IOException
	{
		// Create Ret
		HttpURLConnection ret = (HttpURLConnection) url.openConnection();
		
		// Fill Ret
		trustConnection(ret);
		ret.setConnectTimeout(CONNECT_TIMEOUT);
		ret.setRequestMethod("GET");
		ret.addRequestProperty("User-Agent", USER_AGENT);
		
		// Return Ret
		return ret;
	}
	
	// -------------------------------------------- //
	// SSL TRUST
	// -------------------------------------------- //
	
	public static final HostnameVerifier TRUSTING_HOSTNAME_VERIFIER = new HostnameVerifier()
	{
		@Override public boolean verify(String hostname, SSLSession session) { return true; }
	};
	
	public static final X509TrustManager TRUSTING_TRUST_MANAGER = new X509TrustManager()
	{
		@Override public X509Certificate[] getAcceptedIssuers() { return null; }
		@Override public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
		@Override public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
	};
	
	public static final SSLSocketFactory TRUSTING_SOCKET_FACTORY;
	
	static
	{
		SSLSocketFactory socketFactory = null;
		try
		{
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{ TRUSTING_TRUST_MANAGER }, null);
			socketFactory = ctx.getSocketFactory();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		TRUSTING_SOCKET_FACTORY = socketFactory;
	}
	
	public static void trustConnection(URLConnection connection)
	{
		if ( ! (connection instanceof HttpsURLConnection)) return;
		HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
        httpsConnection.setSSLSocketFactory(TRUSTING_SOCKET_FACTORY);
        httpsConnection.setHostnameVerifier(TRUSTING_HOSTNAME_VERIFIER);
	}
	
}
