package com.dandu.fdureader;

import org.xmlrpc.android.*;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dandu.constant.Constants;
import com.dandu.contentfragment.FindMagazineContentFragment;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Backend
{
	//APP的后台，构造时需要传递一个handler。请勿调用非public的方法、成员。
	
	static public final int BACKEND_MSG_GETPOSTCONTENT_COMPLETED = 2;
	static public final int BACKEND_MSG_GETPOSTMEDIA_COMPLETED = 3;
	static public final int BACKEND_MSG_GETTERMS_COMPLETED = 4;
	static public final int BACKEND_MSG_GETPOSTSBYMAGAZINEID_COMPLETED = 5;
	static public final int BACKEND_MSG_GETMAGAZINECOVER_COMPLETED = 6;
	static public final int BACKEND_MSG_GETBOOKMARKS_COMPLETED = 7;
	static public final int BACKEND_MSG_CHANGEBOOKMARK_COMPLETED = 8;
	static public final int BACKEND_MSG_QUERYBOOKMARKSTATUS_COMPLETED = 9;
	static public final int BACKEND_MSG_RETRIVEMARKEDPOST_COMPLETED = 10;
	
	//储存媒体的数组
	public ArrayList<Press> presses = new ArrayList<Press>();
	
	//记录是否登录，在使用收藏有关的函数时，请先检查此变量值
	public Boolean isLogined = false;
	
	//所有期刊的ID，ID已经按降序排列，ID越大，表示这个期刊越新
	//此数组在调用getTerms之后才有效
	public ArrayList<Integer> magazineIDsOrderByTime = new ArrayList<Integer>();
	
	//保存书签（收藏）的数组，每个书签用一个整数（bookmarkID）来表示
	//当书签代表一篇文章时，bookmarkID = postID
	//当书签代表一个期刊时, bookmarkID = -magazineID;
	//此数组当且仅当getBookmarks调用之后才有效
	public ArrayList<Integer> bookmarks = new ArrayList<Integer>();
	
	//构造函数
	public Backend( Handler backendHandler_ )
	{
		Log.d( TAG, "Backend()" );
		uri = URI.create( "http://www.illusate.com/ddtest/xmlrpc.php" );
		backendHandler = backendHandler_;
		
	}
	
	//取得某个magazine的所有的文章，
	//完成后发送BACKEND_MSG_GETPOSTSBYMAGAZINEID_COMPLETED消息
	//这个函数取回文章是从magazine的第一篇文章开始取回numOfPosts篇文章，它只能从第一篇文章开始取回，
	//这个API是DS提供的，目前没有实现从第x篇文章开始取回
	//e.g. 如果已经取回了5篇文章，想要取回第6到12篇文章，只需要numOfPosts=12在调用一次函数即可
	public void getPostsByMagazineID( int term_id, int numOfPosts )
	{
		GetPostsByMagazineIDRunnable getPostsByMagazineID = new GetPostsByMagazineIDRunnable(term_id, numOfPosts );
		new Thread(getPostsByMagazineID).start();
		return;
	}
	
	//获取文章中的媒体资源，储存在/sdcard/FDUReader/posts/postID+postModifiedDate文件夹中
	//@curPost : 当前文章
	//@url ： 媒体资源链接
	//@indexOfMedia ：使用者自定义的媒体序号
	//获取成功后发送BACKEND_MSG_DOWNLOADMEDIA_COMPLETED消息;
	//msg.arg1 = curPost.postID;
	//msg.arg2 = indexOfMedia;
	//图片为原始大小比例!!
	public void getPostMedia( Post curPost, String url, Integer indexOfMedia )
	{
		GetPostMediaRunnable getMedia = new GetPostMediaRunnable(curPost, url, indexOfMedia);
		new Thread( getMedia ).start();
		return;
	}
	
	//获取某个magazine的封面图片，储存在/sdcard/FDUReader/magazine文件夹中
	//完成后发送BACKEND_MSG_GETMAGAZINECOVER_COMPLETED消息
	//msg.arg1 = magazine_id;
	//图片为原始大小比例!!
	public void getMagazineCover( Integer magazine_id )
	{
		GetMagazineCoverRunnable getMagazineCover = new GetMagazineCoverRunnable(magazine_id);
		new Thread(getMagazineCover).start();
		return;
	}
	
	//获取所有杂志，媒体，并自动把杂志归类到对应媒体中
	//强烈建议只在APP启动时调用一次！如果调用多次结果未知！
	//获取成功后发送BACKEND_MSG_GETTERMS_COMPLETED消息。
	public void getTerms()
	{
		GetTermsRunnable getTerm = new GetTermsRunnable();
		new Thread(getTerm).start();
		return;
	}
	

	//填充ID为post_id的文章的content。
	//完成时发送BACKEND_MSG_GETPOST_COMPLETED消息，arg1 = post_id。
	public void getPostContent( Integer post_id )
	{
		GetPostContentRunnable getPostContent = new GetPostContentRunnable( post_id );
		new Thread(getPostContent).start();
		return;
		
	}
	
	//通过媒体ID取回媒体
	public Press getPressByID( Integer id )
	{
		for ( Press p : presses )
		{
			if ( p.term_id == id )
			{
				return p;
			}
		}
		return null;
	}
	
	//通过期刊ID取回期刊
	public Magazine getMagazineByID( Integer id )
	{
		for ( Press p : presses )
		{
			for( Magazine m : p.magazines )
			{
				if ( m.term_id == id )
				{
					return m;
				}
			}
		}
		return null;
	}
	
	//通过文章ID取回文章
	public Post getPostByID( Integer id )
	{
		for( Press p : presses )
		{
			for( Magazine m : p.magazines )
			{
				for( Post curPost : m.posts )
				{
					if ( curPost.postID == id )
					{
						return curPost;
					}
				}
			}
		}
		return null;
	}
	
	//获得当前用户的所有书签
	//完成后发送BACKEND_MSG_GETBOOKMARKS_COMPLETED消息
	public void getBookmarks()
	{
		GetBookmarksRunnbale getBookmarks = new GetBookmarksRunnbale();
		new Thread(getBookmarks).start();
		return;
	}
	
	//改变一个书签的状态（从已收藏到未收藏，从未收藏到已收藏）
	//完成后发送BACKEND_MSG_CHANGEBOOKMARK_COMPLETED消息
	//msg.arg1 = bookmarkID
	public void changeBookmark( Integer bookmarkID )
	{
		ChangeBookmarkRunnable changeBookmark = new ChangeBookmarkRunnable(bookmarkID);
		new Thread(changeBookmark).start();
		return;
	}
	
	//查询一个文章（期刊）是否被收藏
	//查询文章时,设bookmarkID = postID
	//查询期刊时,设bookmarkID = -magazineID
	//完成时发送BACKEND_MSG_QUERYBOOKMARKSTATUS_COMPLETED消息
	//msg.arg1 = bookmarkID
	//msg.arg2 = 1(收藏） / 0（未收藏）
	public void queryBookmarkStatus( Integer bookmarkID )
	{
		QueryBookmarkStatusRunnable queryBookmarkStatus = new QueryBookmarkStatusRunnable(bookmarkID);
		new Thread(queryBookmarkStatus).start();
		return;
	}
	
	//取回一篇文章，通过书签号
	//如果书签号是负数，代表一个期刊，直接调用getMagazineByID即可取回该期刊
	//如果书签号是正数，代表一个文章，此时需要先调用retriveMarkedPost函数，
	//调用成功后再调用getPostByID才能取回该文章
	//完成后发送BACKEND_MSG_RETRIVEMARKEDPOST_COMPLETED消息
	//msg.arg1 = postID
	public void retriveMarkedPost( Integer bookmarkID )
	{
		RetriveMarkedPostRunnable retriveMarkedPost = new RetriveMarkedPostRunnable(bookmarkID);
		new Thread(retriveMarkedPost).start();
		return;
	}
	
	static private final String TAG = "BackendDebugging";
	static private final String BLOG_USERNAME = "dandu";
	static private final String BLOG_PASSWORD = "dandu";
	static private final String WPAPI_GETPOST = "wp.getPost";
	static private final int BLOG_ID = 1;
	static private final String WPAPI_POST_CONTENT = "post_content";
	static private final String WPAPI_GETTERMS = "wp.getTerms";
	
	private String username;
	private String password;
	

	private Handler backendHandler;
	private URI uri;

	

	
	
	private class GetPostContentRunnable implements Runnable
	{
		private Integer post_id;
		public GetPostContentRunnable( Integer post_id_ )
		{
			post_id = post_id_;
		}
		public void run()
		{
			XMLRPCClient client = new XMLRPCClient(uri);
			Log.d( TAG, "GetPostContentRunnable.run()"+String.valueOf(post_id) );
			Post curPost = getPostByID( post_id );
			if ( curPost == null  )
			{
				return;
			}
			else {
				if ( retriveLocalPost(curPost) )
				{
					Log.d( TAG, "retriveLocalPost()True" );
				}
				else {
					Log.d( TAG, "retriveLocalPost()False" );
					Object[] filter = { WPAPI_POST_CONTENT };
					Object[] params = { BLOG_ID, BLOG_USERNAME, BLOG_PASSWORD
							, post_id, filter };
					try
					{
						Object postObject = client.callEx( WPAPI_GETPOST, params );
						@SuppressWarnings("unchecked")
						Map< String, Object > postMap = (Map<String, Object>)postObject;
						curPost.postContent = (String)postMap.get( WPAPI_POST_CONTENT );
						Log.d( TAG, "getPostOnlineSUCCESS"+curPost.postTitle );

						storePost( curPost );
						
					} catch (XMLRPCException e)
					{
						e.printStackTrace();
						Log.d( TAG, "getPostOnlineFAILED"+curPost.postTitle+e.toString() );
					}
				}
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETPOSTCONTENT_COMPLETED;
				msg.arg1 = post_id;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "GetPostContentRunnable.run() return" );
			}
		}
		private Boolean retriveLocalPost( Post curPost )
		{
			Log.d( TAG, "retriveLocalPost()"+String.valueOf(curPost.postID) );
			String sDStateString = android.os.Environment.getExternalStorageState();  
			   
			if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {  
			    try {  

			    	File SDFile = android.os.Environment  
			                .getExternalStorageDirectory();  
			        File curPostDir = new File( SDFile+File.separator+"FDUReader"+File.separator+"posts"
			    			+File.separator+String.valueOf(curPost.postID)+curPost.postModifiedDate );
			    	File postFile = new File( curPostDir.getAbsolutePath() + File.separator+
			    			String.valueOf(curPost.postID)+curPost.postModifiedDate );
			    			    	
			    	if ( !postFile.exists() )
					{
						return false;
					}
			    	FileReader fr  = null; 
		    		BufferedReader buf =null;
		    		fr = new FileReader( postFile.getAbsolutePath() ); 
		    		buf = new BufferedReader(fr);
		    		String s = buf.readLine();
		    		while( s != null )
		    		{
		    			curPost.postContent += s;
		    			curPost.postContent += '\n';
		    			s = buf.readLine();
		    		}
		    		fr.close();
		    		buf.close();
		    		return true;
			   
			    } catch (Exception e) {  
			    	Log.d( TAG, "retriveLocalFailed:"+String.valueOf(curPost.postID)+e.toString() );
			    }
			   
			}
			return false;
		}
		
		private void storePost( Post curPost ) 
		{
			Log.d( TAG, "StorePosts()"+String.valueOf(curPost.postID));
			if ( curPost.postContent == null )
			{
				return;
			}
			String sDStateString = android.os.Environment.getExternalStorageState();  
			   
			if (sDStateString.equals(android.os.Environment.MEDIA_MOUNTED)) {  
				Log.d( TAG, "writable" );
			    try {  

			    	File SDFile = android.os.Environment  
			                .getExternalStorageDirectory();  
			        
			    	File FDUReaderDir = new File( SDFile+File.separator+"FDUReader" );
			    	File postsDir = new File( SDFile+File.separator+"FDUReader"+File.separator+"posts" );
			    	File curPostDir = new File( SDFile+File.separator+"FDUReader"+File.separator+"posts"
			    			+File.separator+String.valueOf(curPost.postID)+curPost.postModifiedDate );
			    	
			    	if ( !FDUReaderDir.exists() )
					{
						FDUReaderDir.mkdir();
					}
			    	
			    	if ( !postsDir.exists() )
					{
						postsDir.mkdir();
					}
			    	
			    	if ( !curPostDir.exists() )
					{
						curPostDir.mkdir();
					}
			   
			        	String fileName = String.valueOf(curPost.postID)+curPost.postModifiedDate;
				        File curFile = new File( curPostDir.getAbsolutePath()+File.separator+fileName );  
				        if (!curFile.exists()) {  
				            curFile.createNewFile();  
				        }  
				        else {
							return;
						}
			
				        String szOutText = curPost.postContent;
				        FileOutputStream outputStream = new FileOutputStream(curFile);  
				        outputStream.write(szOutText.getBytes());  
				        outputStream.close();  
			        
			   
			    } catch (Exception e) {  
			    	Log.d( TAG, "StorePostsFailed"+String.valueOf(curPost.postID)+e.toString() );
			    }
			   
			}
		}
	}
	

	private class GetTermsRunnable implements Runnable
	{

		@Override
		public void run()
		{
			Log.d( TAG, "GetTermsRunnable()" );
			magazineIDsOrderByTime.clear();
			Object[] params = { BLOG_ID, BLOG_USERNAME, BLOG_PASSWORD, "category" };
			XMLRPCClient client = new XMLRPCClient(uri);
			ArrayList<Term> ts = new ArrayList<Term>();
			try
			{
				Object termsObject = client.callEx( WPAPI_GETTERMS, params );
				Object[] termsObjects = (Object[])termsObject;
				for ( Object curTermObject : termsObjects )
				{
					@SuppressWarnings("unchecked")
					Map< String, Object > curTermMap = (Map<String, Object>)curTermObject;
					Term curTerm = new Term();
					curTerm.term_taxonomy_id = (String)curTermMap.get( "term_taxonomy_id" );
					curTerm.term_id = Integer.valueOf((String)curTermMap.get( "term_id" ));
					curTerm.count = (Integer)curTermMap.get( "count" );
					curTerm.description = (String)curTermMap.get( "description" );
					curTerm.name = (String)curTermMap.get( "name" );
					curTerm.slug = (String)curTermMap.get( "slug" );
					curTerm.parent = Integer.valueOf( (String)curTermMap.get( "parent" ) );
					curTerm.coverImage = (String)curTermMap.get( "image" );
					ts.add(curTerm);
				}
				for( Term curTerm : ts )
				{
					if ( 0 == curTerm.parent )
					{
						Press p = new Press(curTerm);
						presses.add( p );
					}
				}
				for( Term curTerm : ts )
				{
					if ( 0 != curTerm.parent )
					{
						Magazine m = new Magazine(curTerm);
						Press p = getPressByID( curTerm.parent );
						if ( p == null )
						{
							Log.d(TAG, String.valueOf(curTerm.parent) );
							continue;
						}
						p.addMagazine( m );
						magazineIDsOrderByTime.add( m.term_id );
					}
				}
				Collections.sort( magazineIDsOrderByTime );
				Collections.reverse( magazineIDsOrderByTime );
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETTERMS_COMPLETED;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "GetTermsRunnable() return" );
			} catch (XMLRPCException e)
			{
				e.printStackTrace();
				Log.d( TAG, "GetTerms()Failed"+e.toString() );
			}
			
		}
		
	}
		
	private class GetPostMediaRunnable implements Runnable
	{
		private Post curPost;
		private String url;
		private Integer indexOfMedia;
		public GetPostMediaRunnable( Post curPost_, String url_, Integer indexOfMedia_ )
		{
			curPost = curPost_;
			url = url_;
			indexOfMedia = indexOfMedia_;
			
		}
		@Override
		public void run()
		{
			Log.d(TAG, "GetPostMediaRunnable()" );
			File SDFile = android.os.Environment  
	                .getExternalStorageDirectory();  
	        
	    	File FDUReaderDir = new File( SDFile+File.separator+"FDUReader" );
	    	File postsDir = new File( SDFile+File.separator+"FDUReader"+File.separator+"posts" );
	    	File curPostDir = new File( SDFile+File.separator+"FDUReader"+File.separator+"posts"
	    			+File.separator+String.valueOf(curPost.postID)+curPost.postModifiedDate );
	    	
	    	if ( !FDUReaderDir.exists() )
			{
				FDUReaderDir.mkdir();
			}
	    	
	    	if ( !postsDir.exists() )
			{
				postsDir.mkdir();
			}
	    	
	    	if ( !curPostDir.exists() )
			{
				curPostDir.mkdir();
			}
	   		String fileName = url.substring(url.lastIndexOf("/") + 1);
	   		File mediaFile = new File( curPostDir.getAbsolutePath() + File.separator + fileName );
	   		if ( mediaFile.exists() )
			{
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETPOSTMEDIA_COMPLETED;
				msg.arg1 = curPost.postID;
				msg.arg2 = indexOfMedia;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "mediaExist" );
				return;				
			}
	   		
	   		/*if ( url.endsWith( ".jpg" ) ) 
			{
				int p = url.indexOf( ".jpg" );
				StringBuffer buf = new StringBuffer( url );
				buf.insert( p, "-150x150" );
				url = buf.toString();
			}*/
			
			try
			{
				URL Url;
				Url = new URL(url);
				URLConnection conn = Url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				int fileSize = conn.getContentLength();
				if ( fileSize <= 0 ) {
					throw new RuntimeException("无法获知文件大小 ");
				}
				if (is == null) {
					throw new RuntimeException("无法获取文件");
				}
				@SuppressWarnings("resource")
				FileOutputStream FOS = new FileOutputStream( curPostDir.getAbsolutePath()+
						File.separator+fileName); 
				byte buf[] = new byte[1024];
				@SuppressWarnings("unused")
				int downLoadFilePosition = 0;
				int numread;
				
				while ((numread = is.read(buf)) != -1) {
					FOS.write(buf, 0, numread);
					downLoadFilePosition += numread;
					}
				is.close();
				curPost.medias.put( indexOfMedia, fileName );
				
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETPOSTMEDIA_COMPLETED;
				msg.arg1 = curPost.postID;
				msg.arg2 = indexOfMedia;
				backendHandler.sendMessage(msg);
			Log.d( TAG, "GetPostMediaRunnable()return" );
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
				Log.d(TAG, "GetPostMediaRunnable()Failed "+e.toString() );
			} catch (IOException e)
			{
				e.printStackTrace();
				Log.d(TAG, "GetPostMediaRunnable()Failed "+e.toString() );
			}
			
			
		}
		
	}
	private class GetMagazineCoverRunnable implements Runnable
	{
		private Integer magazine_id;
		public GetMagazineCoverRunnable( Integer magazine_id_ )
		{
			magazine_id = magazine_id_;
		}
		@Override
		public void run()
		{
			Magazine m = getMagazineByID(magazine_id);
			String url = m.coverImage;
			Log.d( TAG, url );
			Log.d(TAG, "GetMagazineCoverRunnable.run()" );
			File SDFile = android.os.Environment  
	                .getExternalStorageDirectory();  
	        
	    	File FDUReaderDir = new File( SDFile+File.separator+"FDUReader" );
	    	File magazinesDir = new File( SDFile+File.separator+"FDUReader"+File.separator+"magazines" );
	    	
	    	if ( !FDUReaderDir.exists() )
			{
				FDUReaderDir.mkdir();
			}
	    	
	    	if ( !magazinesDir.exists() )
			{
	    		magazinesDir.mkdir();
			}
	    	
	   		String fileName = url.substring(url.lastIndexOf("/") + 1);
	   		File mediaFile = new File( magazinesDir.getAbsolutePath() + File.separator + fileName );
	   		if ( mediaFile.exists() )
			{
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETMAGAZINECOVER_COMPLETED;
				msg.arg1 = magazine_id;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "mediaExist" );
				return;				
			}
	   		
	   		/*if ( url.endsWith( ".jpg" ) ) 
			{
				int p = url.indexOf( ".jpg" );
				StringBuffer buf = new StringBuffer( url );
				buf.insert( p, "-150x150" );
				url = buf.toString();
			}*/
			
			try
			{
				URL Url;
				Url = new URL(url);
				URLConnection conn = Url.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				int fileSize = conn.getContentLength();
				if ( fileSize <= 0 ) {
					throw new RuntimeException("无法获知文件大小 ");
				}
				if (is == null) {
					throw new RuntimeException("无法获取文件");
				}
				@SuppressWarnings("resource")
				FileOutputStream FOS = new FileOutputStream( magazinesDir.getAbsolutePath()+
						File.separator+fileName); 
				byte buf[] = new byte[1024];
				@SuppressWarnings("unused")
				int downLoadFilePosition = 0;
				int numread;
				
				while ((numread = is.read(buf)) != -1) {
					FOS.write(buf, 0, numread);
					downLoadFilePosition += numread;
					}
				is.close();
				
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETMAGAZINECOVER_COMPLETED;
				msg.arg1 = magazine_id;
				backendHandler.sendMessage(msg);
			Log.d( TAG, "GetMagazineCoverRunnable() return" );
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
				Log.d(TAG, "GetMagazineCoverRunnable()Failed "+e.toString() );
			} catch (IOException e)
			{
				e.printStackTrace();
				Log.d(TAG, "GetMagazineCoverRunnable()Failed "+e.toString() );
			}
		}
		
	}
	private class GetPostsByMagazineIDRunnable implements Runnable
	{
		private int term_id;
		private int numOfPosts;
		public GetPostsByMagazineIDRunnable( int term_id_, int numOfPosts_ )
		{
			term_id = term_id_;
			numOfPosts = numOfPosts_;
		}
		@SuppressWarnings("unchecked")
		@Override
		public void run()
		{
			Log.d( TAG, "GetPostsByMagazineIDRunnable.run()" );
			XMLRPCClient client = new XMLRPCClient(uri);
			Object[] params = { BLOG_ID, BLOG_USERNAME, BLOG_PASSWORD, "post", term_id,
					numOfPosts, "false" };
			try
			{
				Object postListObject = client.callEx( "bdn.getPosts", params );
				Object[] postListObjects = (Object[])postListObject;
				Map< String, Object > postMap = null;
				Magazine m = getMagazineByID( term_id );
				for( Object curObject : postListObjects )
				{
					postMap = (Map< String, Object >)curObject;
					Post curPost = new Post();
					curPost.postID = (Integer)postMap.get( "postId" );
					curPost.postTitle = (String)postMap.get( "title" );
					curPost.postExcerpt = (String)postMap.get( "description" );
					curPost.postModifiedDate = (Date)postMap.get( "dateModified");
					curPost.postAuthor = (String)postMap.get( "authorUserId" );
					curPost.postLink = (String)postMap.get( "link" );
					Post p = getPostByID( curPost.postID );
					if( p == null ){
						m.addPost( curPost );
					}
					else {
						if ( !p.postModifiedDate.equals( curPost.postModifiedDate ) )
						{
							p.postID = curPost.postID;
							p.postTitle = curPost.postTitle;
							p.postExcerpt = curPost.postExcerpt;
							p.postModifiedDate = curPost.postModifiedDate;
							p.postAuthor = curPost.postAuthor;
							p.postLink = curPost.postLink;
							p.postContent = null;
							p.medias.clear();
						}
						
					}
				}
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETPOSTSBYMAGAZINEID_COMPLETED;
				backendHandler.sendMessage(msg);
                FindMagazineContentFragment.articleListHandler.sendMessage(msg);
                Log.d( TAG, "get " + m.posts.size() + " posts");
				Log.d( TAG, "GetPostsByMagazineIDRunnable() return" );
				return;		
				
			} catch (XMLRPCException e)
			{
				e.printStackTrace();
				Log.d( TAG, "GetPostsByMagazineIDRunnable() FAILED"+e.toString() );
			}
		}
		
	}
	
	private class GetBookmarksRunnbale implements Runnable
	{

		@Override
		public void run()
		{
			Log.d( TAG, "GetBookmarksRunnbale.run()" );
			if( !isLogined )
			{
				return;
			}
			bookmarks.clear();
			XMLRPCClient client = new XMLRPCClient(uri);
			Object[] params = { BLOG_ID, username, password };
			try
			{
				Object bookmarksObject = client.callEx( "mark.getUserMark", params );
				Object[] bookmarksObjects = (Object[])bookmarksObject;
				for( Object curBookmark : bookmarksObjects )
				{
					bookmarks.add( (Integer)curBookmark );
				}				
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETBOOKMARKS_COMPLETED;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "GetBookmarksRunnbale.run() return" );
			} catch (XMLRPCException e)
			{
				e.printStackTrace();
				Log.d( TAG, "GetBookmarksRunnable.run()Failed:"+e.toString() );
			}
		}
		
	}
	private class ChangeBookmarkRunnable implements Runnable
	{
		private Integer bookmarkID;
		public ChangeBookmarkRunnable( Integer bookmarkID_ )
		{
			bookmarkID = bookmarkID_;
		}
		@Override
		public void run()
		{
			Log.d( TAG, "ChangeBookmarkRunnbale.run()" );
			if( !isLogined )
			{
				return;
			}
			XMLRPCClient client = new XMLRPCClient(uri);
			Object[] params = { BLOG_ID, username, password, bookmarkID, 1 };
			try
			{
				Object bookmarksObject = client.callEx( "mark.getMark", params );
				Object[] bookmarksObjects = (Object[])bookmarksObject;
				Integer status = (Integer)(bookmarksObjects[0]);
				if ( status == 1 )
				{
					bookmarks.add(bookmarkID);
				}
				else if( status == 0)
				{
					for( int i = 0 ; i < bookmarks.size() ; i++ )
					{
						if ( bookmarks.get(i) == bookmarkID )
						{
							bookmarks.remove(i);
						}
					}
				}
				Message msg = new Message();
				msg.what = BACKEND_MSG_CHANGEBOOKMARK_COMPLETED;
				msg.arg1 = bookmarkID;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "ChangeBookmarkRunnbale.run() return" );
			} catch (XMLRPCException e)
			{
				e.printStackTrace();
				Log.d( TAG, "ChangeBookmarkRunnbale.run()Failed:"+e.toString() );
			}
		}
		
	}
	private class QueryBookmarkStatusRunnable implements Runnable
	{
		private Integer bookmarkID;
		public QueryBookmarkStatusRunnable( Integer bookmarkID_ )
		{
			bookmarkID = bookmarkID_;
		}
		@Override
		public void run()
		{
			Log.d( TAG, "QueryBookmarkStatusRunnable.run()" );
			if( !isLogined )
			{
				return;
			}
			XMLRPCClient client = new XMLRPCClient(uri);
			Object[] params = { BLOG_ID, username, password, bookmarkID, 0 };
			try
			{
				Object bookmarksObject = client.callEx( "mark.getMark", params );
				Object[] bookmarksObjects = (Object[])bookmarksObject;
				Integer status = (Integer)(bookmarksObjects[0]);
				Message msg = new Message();
				msg.what = BACKEND_MSG_QUERYBOOKMARKSTATUS_COMPLETED;
				msg.arg1 = bookmarkID;
				msg.arg2 = status;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "QueryBookmarkStatusRunnable.run() return" );
			} catch (XMLRPCException e)
			{
				e.printStackTrace();
				Log.d( TAG, "QueryBookmarkStatusRunnable.run()Failed:"+e.toString() );
			}
		}
	}
	private class RetriveMarkedPostRunnable implements Runnable
	{
		private Integer postID;
		public RetriveMarkedPostRunnable( Integer bookmarkID_ )
		{
			postID = bookmarkID_;
		}
		@Override
		public void run()
		{
			Log.d( TAG, "RetriveMarkedPostRunnable.run()");
			if ( getPostByID(postID) != null )
			{
				Message msg = new Message();
				msg.what = BACKEND_MSG_RETRIVEMARKEDPOST_COMPLETED;
				msg.arg1 = postID;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "RetriveMarkedPostRunnable.run() return");
				return;
			}
			XMLRPCClient client = new XMLRPCClient(uri);
			Object[] fields = { "post_id", "post_title", 
					"post_modified", "post_excerpt", "post_author", "link", "terms" };
			Object[] params = { BLOG_ID, BLOG_USERNAME, BLOG_PASSWORD
					, postID, fields };
			try
			{
				Object postObject = client.callEx( "wp.getPost", params );
				@SuppressWarnings("unchecked")
				Map< String, Object > postMap = (Map< String, Object >)postObject;
				Post curPost = new Post();
				curPost.postID = Integer.valueOf((String)postMap.get( "post_id" ));
				curPost.postTitle = (String)postMap.get( "post_title" );
				curPost.postExcerpt = (String)postMap.get( "post_excerpt" );
				curPost.postModifiedDate = (Date)postMap.get( "post_modified");
				curPost.postAuthor = (String)postMap.get( "post_author" );
				Object[] termsObjects = (Object[])postMap.get( "terms" );
				for ( Object curTermObject : termsObjects ){
					@SuppressWarnings("unchecked")
					Map< String, Object > curTermMap = (Map<String, Object>)curTermObject;
					Integer magazine_id = Integer.valueOf((String)curTermMap.get("term_id") );
					Integer press_id = Integer.valueOf((String)curTermMap.get("parent") );
					if ( press_id != 0 )
					{
						Magazine m = getMagazineByID( magazine_id );
						m.addPost( curPost );
						break;
					}
				}
				Message msg = new Message();
				msg.what = BACKEND_MSG_RETRIVEMARKEDPOST_COMPLETED;
				msg.arg1 = postID;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "RetriveMarkedPostRunnable.run() return" );
				return;		
				
			} catch (XMLRPCException e)
			{
				e.printStackTrace();
				Log.d( TAG, "RetriveMarkedPostRunnable.run()FAILED:"+e.toString() );
			}
			
		
		}
		
	}
	
	/*private class GetTaxonomiesRunnable implements Runnable
	{

		@Override
		public void run()
		{
			Object[] params = { BLOG_ID, BLOG_USERNAME, BLOG_PASSWORD };
			XMLRPCClient client = new XMLRPCClient(uri);
			try
			{
				Object taxonomiestObject = client.callEx( WPAPI_GETTAXONOMIES, params );
				Object[] taxonomiesObjects = (Object[])taxonomiestObject;
				for ( Object curTaxonomyObject : taxonomiesObjects )
				{
					Log.d( TAG, curTaxonomyObject.toString() );
				}
			} catch (XMLRPCException e)
			{
				e.printStackTrace();
				Log.d( TAG, "GetTaxonomies()Failed"+e.toString() );
			}
		}
		
	}*/
	
	

	/*private class GetPostListRunnable implements Runnable
	{
		private int startPos;
		private int num;
		public GetPostListRunnable()
		{
			startPos = -1;
			num = -1;
		}
		public GetPostListRunnable( int startPos_, int num_ )
		{
			startPos = startPos_;
			num = num_;
		}
		@Override
		public void run()
		{
			XMLRPCClient client = new XMLRPCClient(uri);
			Log.d( TAG, "GetPostListRunnable.run()"+String.valueOf(startPos)+':'+String.valueOf(num) );
			Object[] fields = { WPAPI_POST_TITLE, WPAPI_POST_MODIFIED, 
					WPAPI_POST_ID, WPAPI_POST_AUTHOR, WPAPI_POST_EXCERPT, WPAPI_TERMS };
			Map< String, Object > filter = new HashMap<String, Object>();
			if ( startPos != -1 )
			{
				filter.put( "number", num );
				filter.put( "offset", startPos );				
			}

			Object[] params = { BLOG_ID, BLOG_USERNAME, BLOG_PASSWORD
					, filter, fields };
			try
			{
				Object postListObject = client.callEx( WPAPI_GETPOSTS, params );
				Object[] postListObjects = (Object[])postListObject;
				Log.d( TAG, postListObjects.toString() );
				Map< String, Object > postMap = null;
				for( Object curObject : postListObjects )
				{
					postMap = (Map< String, Object >)curObject;
					Post curPost = new Post();
					curPost.postID = Integer.valueOf((String)postMap.get( WPAPI_POST_ID ));
					curPost.postTitle = (String)postMap.get( WPAPI_POST_TITLE );
					curPost.postExcerpt = (String)postMap.get( WPAPI_POST_EXCERPT );
					curPost.postModifiedDate = ((Date)postMap.get( WPAPI_POST_MODIFIED) ).toString();
					curPost.postAuthor = (String)postMap.get( WPAPI_POST_AUTHOR );
					Object[] termsObjects = (Object[])postMap.get( WPAPI_TERMS );
					for ( Object curTermObject : termsObjects ){
						Map< String, Object > curTermMap = (Map<String, Object>)curTermObject;
						Term curTerm = new Term();
						curTerm.term_taxonomy_id = (String)curTermMap.get( "term_taxonomy_id" );
						curTerm.term_id = (String)curTermMap.get( "term_id" );
						curTerm.count = (Integer)curTermMap.get( "count" );
						curTerm.description = (String)curTermMap.get( "description" );
						curTerm.name = (String)curTermMap.get( "name" );
						curTerm.slug = (String)curTermMap.get( "slug" );
						curPost.terms.add( curTerm );
						
					}
					Post p = getPostByID( curPost.postID );
					if( p == null ){
						posts.add( curPost );
					}
					else {
						if ( !p.postModifiedDate.equals( curPost.postModifiedDate ) )
						{
							p.postID = curPost.postID;
							p.postTitle = curPost.postTitle;
							p.postExcerpt = curPost.postExcerpt;
							p.postModifiedDate = curPost.postModifiedDate;
							p.postAuthor = curPost.postAuthor;
							p.postContent = null;
							p.medias.clear();
							p.terms.clear();
						}
						
					}
					//Log.d( TAG, postMap.toString() );
				}
				isGetPostListCompleted = true;
				Message msg = new Message();
				msg.what = BACKEND_MSG_GETPOSTLIST_COMPLETED;
				msg.arg1 = startPos;
				msg.arg2 = num;
				backendHandler.sendMessage(msg);
				Log.d( TAG, "GetPostListRunnable.run() return" );
				return;		
				
			} catch (XMLRPCException e)
			{
				e.printStackTrace();
				Log.d( TAG, "GetPostListRunnable()FAILED"+e.toString() );
			}
			
		}
	};*/
	
	//获取博客中的全部文章，储存在数组posts中，注意，此函数不填充成员postContent，如果要获得某个文章
		//的postContent,需要调用getPost。
		//当取回文章列表完成时，发送BACKEND_MSG_GETPOSTLIST_COMPLETED消息,msg.arg1 = msg.art2 = -1。
		/*public void getPostList(  )
		{
			GetPostListRunnable getPostList = new GetPostListRunnable();
			new Thread(getPostList).start();
			return;
		}*/
		
		
		//从第startPos篇文章开始，取回num篇文章，注意，此函数不填充成员postContent，如果要获得某个文章
		//的postContent,需要调用getPost。
		//当取回文章列表完成时，发送BACKEND_MSG_GETPOSTLIST_COMPLETED消息,
		//msg.arg1 = startPos, msg.art2 = num。
		/*public void getPostList( int startPos , int num  )
		{
			GetPostListRunnable getPostList = new GetPostListRunnable( startPos, num );
			new Thread(getPostList).start();
			return;
		}*/
	

}
