import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;

public class PostDAO {

	public void addPost(Post bean) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Integer id = (Integer)session.save(bean);
		MemCacheUtil.putInCache(id.toString(), bean);
		session.getTransaction().commit();

		// get a new EM to make sure data is actually retrieved from the store
		// and not Hibernate's internal cache
		session.close();
	}

	public ArrayList<HashMap> getPosts() {
		ArrayList<HashMap> myList = new ArrayList<HashMap>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String stringQuery = "FROM Post ORDER BY postdate asc";
		Query query = session.createQuery(stringQuery);
		query.setMaxResults(10);
		List<Post> posts = query.getResultList();
		System.out.println("Let us c" + posts.get(0).getPostDate());
		
		for (int i = 0; i < posts.size(); i++) {
			HashMap hash = new HashMap();
			hash.put("postDate", posts.get(i).getPostDate());
			hash.put("bookName", posts.get(i).getBook().getBookName());
			hash.put("authorName", posts.get(i).getBook().getAuthorName());
			hash.put("publisherName", posts.get(i).getBook().getPublisherName());
			hash.put("price", posts.get(i).getPrice());
			hash.put("postID", posts.get(i).getPostId());
			myList.add(hash);
		}
		return myList;
		//Hibernate.initialize(posts.get(0).getUser());
		// session.close();
	}

	public ArrayList<HashMap> getMyPosts(String userid) {
		ArrayList<HashMap> myList = new ArrayList<HashMap>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String stringQuery = "FROM User WHERE user_id=" + Integer.parseInt(userid);
		Query query = session.createQuery(stringQuery);
		List<User> users = query.getResultList();
		System.out.println("USer id is: " + users.get(0).getUserId());
		stringQuery = "FROM Post WHERE user_id=" + users.get(0).getUserId();
		query = session.createQuery(stringQuery);
		List<Post> posts = query.getResultList();
		for (int i = 0; i < posts.size(); i++) {
			HashMap hash = new HashMap();
			hash.put("postDate", posts.get(i).getPostDate());
			hash.put("bookName", posts.get(i).getBook().getBookName());
			hash.put("authorName", posts.get(i).getBook().getAuthorName());
			hash.put("publisherName", posts.get(i).getBook().getPublisherName());
			hash.put("price", posts.get(i).getPrice());
			hash.put("postID", posts.get(i).getPostId());
			myList.add(hash);
		}
		return myList;
	}

	public HashMap getPost(Long postID) {
		HashMap hash = new HashMap();
		if (MemCacheUtil.getFromCache(postID.toString()) == null) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String stringQuery = "FROM Post WHERE post_id=" + postID;
		Query query = session.createQuery(stringQuery);
		List<Post> post = query.getResultList();
		System.out.println("Let us c" + post.get(0).getPostDate());
		hash.put("bookname", post.get(0).getBook().getBookName());
		hash.put("authorname", post.get(0).getBook().getAuthorName());
		hash.put("publishername", post.get(0).getBook().getPublisherName());
		hash.put("price", post.get(0).getPrice());
		hash.put("date", post.get(0).getPostDate());
		hash.put("postowner", post.get(0).getUser().getUserId());
		return hash;
		}
		
		Post singlePost = (Post)MemCacheUtil.getFromCache(postID.toString());
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd"); 
		hash.put("bookname", singlePost.getBook().getBookName());
		hash.put("authorname", singlePost.getBook().getAuthorName());
		hash.put("publishername", singlePost.getBook().getPublisherName());
		hash.put("price", singlePost.getPrice());
		System.out.println("Post date cache: "+df.format(singlePost.getPostDate()));
		hash.put("date", df.format(singlePost.getPostDate()));
		hash.put("postowner", singlePost.getUser().getUserId());
		// session.close();
		// return Response.ok(usr.get(0)).build();
		return hash;
	}

	public Post getPostForBid(Long postID) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String stringQuery = "FROM Post WHERE post_id=" + postID;
		Query query = session.createQuery(stringQuery);
		List<Post> post = query.getResultList();
		System.out.println("Let us c" + post.get(0).getPostDate());
		// session.close();
		return post.get(0);
	}

	public ArrayList<HashMap> getPostsByName(String bookname) {
		ArrayList<HashMap> myList = new ArrayList<HashMap>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String stringQuery = "FROM Book WHERE bookname='" + bookname + "'";
		Query query = session.createQuery(stringQuery);
		List<Book> books = query.getResultList();
		// System.out.println("Book name is: "+books.get(0).getBookName());

		if (books.size() > 0) {
			stringQuery = "FROM Post WHERE book_id=" + books.get(0).getBookId();
			query = session.createQuery(stringQuery);
			List<Post> posts = query.getResultList();

			for (int i = 0; i < posts.size(); i++) {
				HashMap hash = new HashMap();
				hash.put("postDate", posts.get(i).getPostDate());
				hash.put("bookName", posts.get(i).getBook().getBookName());
				hash.put("authorName", posts.get(i).getBook().getAuthorName());
				hash.put("publisherName", posts.get(i).getBook().getPublisherName());
				hash.put("price", posts.get(i).getPrice());
				hash.put("postID", posts.get(i).getPostId());
				myList.add(hash);
			}
		}
		// System.out.println("Let us c" + myList.get(0));
		// session.close();
		return myList;
	}
	
	public Post getPostForBid(int postId)
	{
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
	    String stringQuery = "FROM Post WHERE post_id=" + postId;
	    Query query = session.createQuery(stringQuery);
	    List<Post> post = query.getResultList();
	    System.out.println("Let us c" + post.get(0).getPostDate());
	    //session.close();
	    //return Response.ok(usr.get(0)).build();
	    return post.get(0);	
	}

}
