package com.jsoup.zakaria;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	public static void main(String[] args) throws Exception {
		
		List<String> companies = new ArrayList<String>();
		
		Document doc = Jsoup.connect("http://www.biospace.com/news_subject_all_results.aspx?CatagoryId=40103").get();
		Elements bTags = doc.getElementsByTag("b");
		int i=0;
		for (Element bTag : bTags) {
		  String name = bTag.text();
		  if(!companies.contains(name)){
			  System.out.println(name);
			  companies.add(name);
			  i++;
		  }
		}
		System.out.println(i);
	}

}
