import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerUtil {
    
    private static EntityManagerUtil instance=new EntityManagerUtil();
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("bookBid");;
    
    public static EntityManagerUtil getInstance(){
            return instance;
    }
    
    private EntityManagerUtil(){
		entityManagerFactory = Persistence.createEntityManagerFactory("bookBid");
    }
    
    public static EntityManagerFactory getManager()
    {
    	return entityManagerFactory;
    }
    
    /*public static Session getSession(){
        Session session =  getInstance().sessionFactory.openSession();
        
        return session;
    }*/
}
