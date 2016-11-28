import javax.persistence.EntityManager;

public class BidDAO {

	public void addBid(Bid bean)
	{
		EntityManager entityManager = EntityManagerUtil.getManager().createEntityManager();
	    entityManager.getTransaction().begin();

        entityManager.persist( bean );

	    entityManager.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    entityManager.close();
	}
}
