package com.dandu.fdureader;

import android.annotation.SuppressLint;
import java.util.*;

@SuppressLint("UseSparseArrays")
public class Post
{
	public Integer postID;//文章ID
	public String postTitle;//文章题目
	public String postExcerpt;//文章摘要
	public String postContent;//文章正文
	public String postAuthor;//文章作者
	public Date postModifiedDate;//文章最后一次修改时间
	public String postLink;//文章所在的网页，可能用不到，保存以备不时之需
	public Map<Integer, String> medias = new HashMap<Integer,String>();//储存媒体资源序号和媒体资源名称
	@Override
	public String toString()
	{
		return "postID:"+String.valueOf(postID)+'\n'
				+"postTitle:"+postTitle+'\n'
				+"postAuthor:"+postAuthor+'\n'
				+"postExcerpt:"+postExcerpt+'\n'
				+"postModifiedTime:"+postModifiedDate+'\n'
				+"postLink:"+postLink;
	}
	
	
}
