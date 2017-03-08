import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

public class BookDAO {

	public Book getBook(String bookName, String authorName)
	{
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

	    String stringQuery = "FROM Book WHERE bookname='" + bookName + "' and author_name= '"+authorName+"'";
	    Query query = session.createQuery(stringQuery);
	    List<Book> book = query.getResultList();
	    session.close();
	    if (book.size() != 0)
	    {
	    	return book.get(0);
	    } else {
	    	return null;
	    }
	}
	
	public void addBook(Book book)
	{
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

        session.save( book );

	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
	}
}
