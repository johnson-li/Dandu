package com.dandu.fdureader;

public class Slider
{
	public String url;
	public Integer postid;
	public Slider( String url_, Integer postid_ )
	{
		url = url_;
		postid = postid_;
	}
	public String toString()
	{
		return url+'\n'+postid;
	}
}
