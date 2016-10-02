package com.bls.scan;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class XMLSetting 
{
	static File xml = new File("./lib/config.xml");
	
	public static Document getDoc()
	{
		Document doc=null;
		try 
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder builder = factory.newDocumentBuilder(); 
			doc = builder.parse(xml); 
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return doc;
	}
	
	public static String getIp()
	{
		String ip = getDoc().getElementsByTagName("ip").item(0).getFirstChild().getNodeValue();
		return ip;
	}
	
	public static String getPort()
	{
		String port = getDoc().getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
		return port;
	}

	public static String getDb()
	{
		String db = getDoc().getElementsByTagName("db").item(0).getFirstChild().getNodeValue();
		return db;
	}
	
	public static String getUser()
	{
		String user = getDoc().getElementsByTagName("user").item(0).getFirstChild().getNodeValue();
		return user;
	}
	
	public static String getPass()
	{
		String pass = getDoc().getElementsByTagName("pass").item(0).getFirstChild().getNodeValue();
		return pass;
	}
	
	
	public static void main(String[] args)
	{
		System.out.println(getIp());
		System.out.println(getPort());
		System.out.println(getDb());
		System.out.println(getUser());
		System.out.println(getPass());
	}
}
