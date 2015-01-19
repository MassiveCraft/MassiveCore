package com.massivecraft.massivecore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebUtil
{
	// -------------------------------------------- //
	// ASYNC GET TOUCH
	// -------------------------------------------- //
	
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public static void asyncGetTouch(String url)
	{
		try
		{
			asyncGetTouch(new URL(url));
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void asyncGetTouch(final URL url)
	{
		executor.execute(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					InputStream inputStream = null;
					
					inputStream = url.openStream();
					inputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	// -------------------------------------------- //
	// GET URL LINES
	// -------------------------------------------- //
	
	// This one should be run async since it's very slow.
	public static List<String> getUrlLines(URL url, int timeoutMillis) throws IOException
	{
		HttpURLConnection uc = null;
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		
		try
		{
			uc = (HttpURLConnection) url.openConnection();
			uc.setConnectTimeout(timeoutMillis);
			uc.setRequestMethod("GET");
			uc.addRequestProperty("User-Agent", "Mozilla/4.76");
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
	
	
}
