import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;

public class PostDAO {

	public void addPost(Post bean)
	{
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
        session.save( bean );

	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
	}
	
	public List<Post> getPosts()
	{
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
	    String stringQuery = "FROM Post ORDER BY postdate asc";
	    Query query = session.createQuery(stringQuery);
	    List<Post> posts = query.getResultList();
	    System.out.println("Let us c" + posts.get(0).getPostDate());
	    Hibernate.initialize(posts.get(0).getUser());
	    //session.close();
	    return posts;
	}
	
	public ArrayList<HashMap> getMyPosts(String username, String password)
	{
		ArrayList<HashMap> myList = new ArrayList<HashMap>();
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
	    String stringQuery = "FROM User WHERE userName='" + username + "' and password= '"+password+"'";
	    Query query = session.createQuery(stringQuery);
	    List<User> users = query.getResultList();
	    System.out.println("USer id is: "+users.get(0).getUserId());
	    stringQuery = "FROM Post WHERE user_id="+users.get(0).getUserId();
	    //stringQuery = "FROM Post WHERE User=" + users.get(0);
	    query = session.createQuery(stringQuery);
	    List<Post> posts = query.getResultList();
	    for (int i=0;i<posts.size();i++)
	    {
	    	HashMap hash = new HashMap();
	    	hash.put("postDate", posts.get(i).getPostDate());
	    	hash.put("bookName", posts.get(i).getBook().getBookName());
	    	hash.put("price", posts.get(i).getPrice());
	    	hash.put("postID", posts.get(i).getPostId());
	    	myList.add(hash);
	    }
	    System.out.println("Let us c" + myList.get(0));
	    //session.close();
	    return myList;
	}

	public HashMap getPost(Long postID)
	{
		HashMap hash = new HashMap();
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
	    //session.close();
	    //return Response.ok(usr.get(0)).build();
	    return hash;	
	}
	
	public Post getPostForBid(Long postID)
	{
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
	    String stringQuery = "FROM Post WHERE post_id=" + postID;
	    Query query = session.createQuery(stringQuery);
	    List<Post> post = query.getResultList();
	    System.out.println("Let us c" + post.get(0).getPostDate());
	    //session.close();
	    //return Response.ok(usr.get(0)).build();
	    return post.get(0);	
	}
	
	public ArrayList<HashMap> getPostsByName(String bookname)
	{
		ArrayList<HashMap> myList = new ArrayList<HashMap>();
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
    	
	    String stringQuery = "FROM Book WHERE bookname='" + bookname + "'";
	    Query query = session.createQuery(stringQuery);
	    List<Book> books = query.getResultList();
	    System.out.println("Book name is: "+books.get(0).getBookName());

	    stringQuery = "FROM Post WHERE book_id=" + books.get(0).getBookId();
	    query = session.createQuery(stringQuery);
	    //List<User> users = query.getResultList();
	    List<Post> posts = query.getResultList();
	    /*System.out.println("USer id is: "+users.get(0).getUserId());
	    stringQuery = "FROM Post WHERE user_id="+users.get(0).getUserId();
	    //stringQuery = "FROM Post WHERE User=" + users.get(0);
	    query = session.createQuery(stringQuery);*/
	    
	    for (int i=0;i<posts.size();i++)
	    {
	    	HashMap hash = new HashMap();
	    	hash.put("postDate", posts.get(i).getPostDate());
	    	hash.put("bookName", posts.get(i).getBook().getBookName());
	    	hash.put("price", posts.get(i).getPrice());
	    	hash.put("postID", posts.get(i).getPostId());
	    	myList.add(hash);
	    }
	    System.out.println("Let us c" + myList.get(0));
	    //session.close();
	    return myList;
	}
}
