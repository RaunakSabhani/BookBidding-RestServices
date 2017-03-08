import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.persistence.Query;

import org.hibernate.Session;

public class ShoppingCartDao {

	
	public void addItem (ShoppingCart bean){
		Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
        session.save( bean );

	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
	}
	public void updateItem(String ShoppingCartId ,String quanity){
		Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
    	int quan = Integer.parseInt(quanity);
    	int shoppingcartid=Integer.parseInt(ShoppingCartId);
    	
    	ShoppingCart sc=getCartItem(shoppingcartid);
    	
    	
    	double totalPrice=sc.getBid().getBidPrice()*quan;
    	
	    String stringQuery = "UPDATE ShoppingCart SET  quantity = :quantity,  price=:totalPrice where ShoppingCartId = :ShoppingCartId";
	    Query query = session.createQuery(stringQuery);
	   query.setParameter("quantity", quan);
	   query.setParameter("totalPrice", totalPrice);
	   query.setParameter("ShoppingCartId", shoppingcartid);
	    
	    int result=query.executeUpdate();
	    System.out.println("ress:"+result);
	    session.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    session.close();
	}
	public ArrayList<HashMap> getCartItems(String userid) {
		// TODO Auto-generated method stub
		ArrayList<HashMap> myList = new ArrayList<HashMap>();
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
	    String stringQuery = "FROM User WHERE user_id=" + userid;
	    Query query = session.createQuery(stringQuery);
	    List<User> users = query.getResultList();
	    System.out.println("USer id is: "+users.get(0).getUserId());
	    stringQuery = "FROM ShoppingCart WHERE user_id="+users.get(0).getUserId();
	    
	    query = session.createQuery(stringQuery);
	    List<ShoppingCart> shoppingcart = query.getResultList();
	    for(int i=0;i<shoppingcart.size();i++){
	    	
	    	HashMap hash = new HashMap();
	    	hash.put("bookname", shoppingcart.get(i).getPost().getBook().getBookName());
	    	hash.put("bidPrice", shoppingcart.get(i).getBid().getBidPrice());
	    	hash.put("quantity",shoppingcart.get(i).getQuantity() );
	    	hash.put("totalPrice", shoppingcart.get(i).getPrice());
	    	hash.put("shoppingcartid", shoppingcart.get(i).getShoppingCartId());
	    	hash.put("bidId",shoppingcart.get(i).getBid().getBidId());
	    	hash.put("postId", shoppingcart.get(i).getPost().getPostId());
	    	myList.add(hash);
	    	
	    	
	    }
	    //Sys tem.out.println("Let us c1" + myList.get(0));
	    
		return myList;
	}
	public void removeCartItems(String shoppingcartid) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(shoppingcartid);
		
		Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
	    String stringQuery = "delete ShoppingCart where shoppingCartId = :shoppingCartId";
	    
	    Query query = session.createQuery(stringQuery).setParameter("shoppingCartId", id);
	    
		int result=query.executeUpdate();
		System.out.println("result"+result);

		session.close();	
		
	}
	public ShoppingCart getCartItem(int shoppingcartid){
		Session session = HibernateUtil.getSessionFactory().openSession();
    	session.beginTransaction();
	    String stringQuery = "FROM ShoppingCart WHERE shoppingCartId ="+shoppingcartid;
	    Query query = session.createQuery(stringQuery);
	    List<ShoppingCart> shoppingcart = query.getResultList();
	    
		
		
		return shoppingcart.get(0);
	}
	public void submitCart(ArrayList<HashMap> entries) {
		// TODO Auto-generated method stub
		System.out.println("in submit cart");
		for (HashMap hashMap : entries) {
			int bidId=(int) hashMap.get("bidId");
			BidDAO bdao= new BidDAO();
			Bid bid=bdao.getBid(bidId);
			User biduser=bid.getUser();
			PostDAO pdao = new PostDAO();
			int postId=(int) hashMap.get("postId");
			Post post=pdao.getPostForBid(postId);
			User postUser=post.getUser();
			String postemail=postUser.getEmail();
			String bidemail=biduser.getEmail();
			System.out.println("Sending mail to:"+postemail+" and "+bidemail);
			try {
				SendConfirmationMail.generateAndSendEmail(bidemail, postemail);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("removing cart items");
			int id=(int) hashMap.get("shoppingcartid");
			removeCartItems(Integer.toString(id));
			
			
			
		}
		
		
	}
	
	
}
