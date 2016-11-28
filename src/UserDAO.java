import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.core.Response;

import java.sql.Date;
import org.hibernate.Session;


public class UserDAO {
    
    public void addUser(User bean){
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

        session.save( bean );

	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
        
    }
    
    public void updateUser(String username, String password, String name, String email, Date birthDate, String gender) {
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

	    String stringQuery = "UPDATE User SET username = :username, name = :name, email = :email, birthDate= :birthDate, gender= :gender WHERE password= '"+password+"'";
	    Query query = session.createQuery(stringQuery);
	    query.setParameter("username", username);
	    query.setParameter("name", name);
	    query.setParameter("email", email);
	    query.setParameter("birthDate", birthDate);
	    query.setParameter("gender", gender);
	    query.executeUpdate();
	    
	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();

    }
    
    public void updateLoginDetails(Date lastLoginDate, String lastLoginTime, String username, String password)
    {
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

	    String stringQuery = "UPDATE User SET last_login_date= :lastLoginDate, last_login_time= :lastLoginTime WHERE password= '"+password+"'";
	    Query query = session.createQuery(stringQuery);
	    query.setParameter("lastLoginDate", lastLoginDate);
	    query.setParameter("lastLoginTime", lastLoginTime);
	    query.executeUpdate();
	    
	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
    }

    public User getUser(String userName, String password)
    {
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

	    String stringQuery = "FROM User WHERE userName='" + userName + "' and password= '"+password+"'";
	    Query query = session.createQuery(stringQuery);
	    List<User> usr = query.getResultList();
	    System.out.println("Let us c" + usr.get(0).getBirthDate());
	    session.close();
	    //return Response.ok(usr.get(0)).build();
	    return usr.get(0);
    }
}
