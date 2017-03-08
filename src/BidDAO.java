import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

public class BidDAO {

	public void addBid(Bid bean)
	{
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
        session.save( bean );

	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
	}
	
	public ArrayList<HashMap> getBids(Long postId)
	{
		ArrayList<HashMap> myList = new ArrayList<HashMap>();
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();

		String stringQuery = "FROM Bid WHERE post_id="+postId;
	    Query query = session.createQuery(stringQuery);
	    List<Bid> bids = query.getResultList();
	    for (int i=0;i<bids.size();i++)
	    {
	    	HashMap hash = new HashMap();
	    	hash.put("bidDate", bids.get(i).getBidDate());
	    	hash.put("bidId", bids.get(i).getBidId());
	    	hash.put("username", bids.get(i).getUser().getUsername());
	    	hash.put("bidPrice", bids.get(i).getBidPrice());
	    	hash.put("name", bids.get(i).getUser().getName());
	    	hash.put("postId", bids.get(i).getPost().getPostId());
	    	myList.add(hash);
	    }
	    //System.out.println("Let us c" + myList.get(0));
	    //session.close();
	    return myList;
	}
	
	public Bid getBid(long bidId){
		Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
	    String stringQuery = "FROM Bid WHERE bid_Id=" + bidId;
	    Query query = session.createQuery(stringQuery);
	    List<Bid> bid = query.getResultList();
	    System.out.println("bid:"+bid.get(0).getBidId()+bid.get(0).getBidPrice());
	    return bid.get(0);
		
	}
}
