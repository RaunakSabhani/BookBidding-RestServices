import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import java.sql.Date;

import org.hibernate.Session;

public class UserDAO {
    
    public void addUser(User bean){
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

        Integer id = (Integer)session.save( bean );
        MemCacheUtil.putInCache(bean.getUsername(), bean);
	    session.getTransaction().commit();
	    //HashMap cacheObject = beanToHashMap(bean);
	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
        
    }
        
    public void updateUser(String username, String userid, String name, String email, String gender) {
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

	    String stringQuery = "UPDATE User SET username = :username, name = :name, email = :email, gender= :gender WHERE user_id="+Integer.parseInt(userid);
	    Query query = session.createQuery(stringQuery);
	    query.setParameter("username", username);
	    query.setParameter("name", name);
	    query.setParameter("email", email);
	    query.setParameter("gender", gender);
	    query.executeUpdate();
	    
	    MemCacheUtil.removeFromCache(username);
	    
	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();

    }
    
    public void updateLoginDetails(Date lastLoginDate, String lastLoginTime, String location, String userid)
    {
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

	    String stringQuery = "UPDATE User SET last_login_date= :lastLoginDate, last_login_time= :lastLoginTime, location= :location WHERE user_id="+userid;
	    Query query = session.createQuery(stringQuery);
	    query.setParameter("lastLoginDate", lastLoginDate);
	    query.setParameter("lastLoginTime", lastLoginTime);
	    query.setParameter("location", location);
	    query.executeUpdate();
	    
	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
    }

    public User getUser(String username, String password)
    {
    	User user = (User)MemCacheUtil.getFromCache(username);    	 
    	if ( user != null) {
    		return user;
    	}
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

	    String stringQuery = "FROM User WHERE username='" + username + "' and password= '"+password+"'";
	    Query query = session.createQuery(stringQuery);
	    List<User> usr = query.getResultList();
	   // System.out.println("Let us c" + (String)usr.get(0).getName());
	    //User user = new User();
	    //user = usr.get(0).
	    session.close();
	    MemCacheUtil.putInCache(usr.get(0).getUsername(), usr.get(0));
	    return usr.get(0);
    }
    
    public User getUserById(String userId)
    {
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

	    String stringQuery = "FROM User WHERE user_id=" + userId;
	    Query query = session.createQuery(stringQuery);
	    List<User> usr = query.getResultList();
	    session.close();
	    return usr.get(0);
    }
}
