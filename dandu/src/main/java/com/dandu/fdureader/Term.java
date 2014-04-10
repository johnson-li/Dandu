package com.dandu.fdureader;

import java.util.*;

public class Term
{
	public Integer term_id;//分类（刊物或媒体）ID
	public String name;//分类名称
	public String slug;//我也没搞清楚这个和name有什么大的区别。。。
	public String term_taxonomy_id;//没搞清楚这个和term_id有什么大的区别,可以完全无视它
	public String description;//描述一下这个分类
	public Integer parent;//如果parent==0，表示这是一个媒体，否则这是一个刊物，parent表示它所属的媒体
	public Integer count;//这个刊物下文章的总数
	public String coverImage;//分类（刊物或媒体）的封面图片，使用前请检查是否为null
	@Override
	public String toString()
	{
		return "term_id:"+term_id+'\n'
				+"name:"+name+'\n'
				+"slug:"+slug+'\n'
				+"term_taxonomy_id:"+term_taxonomy_id+'\n'
				+"description:"+description+'\n'
				+"parent:"+parent+'\n'
				+"image:"+coverImage+'\n'
				+"count:"+String.valueOf(count)+'\n';
	}

}

//媒体类
class Press extends Term
{
	//数组，保存这个媒体发行的所有刊物
	public ArrayList<Magazine> magazines = new ArrayList<Magazine>();
	public Press()
	{
	}
	public Press( Term t )
	{
		term_id = t.term_id;
		parent = t.parent;
		name = t.name;
		slug = t.slug;
		description = t.description;
		term_taxonomy_id = t.term_taxonomy_id;
		count = t.count;
		coverImage = t.coverImage;
	}
	public void addMagazine( Magazine m )
	{
		magazines.add( m );
	}
	public Magazine getMagazine( Integer id )
	{
		for( Magazine m : magazines )
		{
			if ( m.term_id == id )
			{
				return m;
			}
		}
		return null;
	}
	@Override
	public String toString()
	{
		return super.toString()+'\n'+
				magazines.toString()+'\n';
				
	}
}

//刊物类
class Magazine extends Term
{
	//数组，保存这个刊物下的文章
	public ArrayList<Post> posts = new ArrayList<Post>();
	public Magazine()
	{
	}
	public Magazine( Term t )
	{
		term_id = t.term_id;
		parent = t.parent;
		name = t.name;
		slug = t.slug;
		description = t.description;
		term_taxonomy_id = t.term_taxonomy_id;
		count = t.count;
		coverImage = t.coverImage;		
	}
	public void addPost( Post post )
	{
		posts.add( post );
	}
	@Override
	public String toString()
	{
		return super.toString()+'\n';
				
	}
}
