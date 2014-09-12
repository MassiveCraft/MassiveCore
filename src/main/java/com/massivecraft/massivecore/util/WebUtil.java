package com.massivecraft.massivecore.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebUtil
{
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
}
