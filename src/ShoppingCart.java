import java.io.Serializable;
import javax.persistence.Entity;

public class ShoppingCart implements Serializable{

	/**
	 * 
	 */
	private int ShoppingCartId;
	private Post post;
	private Bid bid;
	private User user;
	private int quantity;
	private Double price;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getShoppingCartId() {
		return ShoppingCartId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setShoppingCartId(int shoppingCartId) {
		ShoppingCartId = shoppingCartId;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public Bid getBid() {
		return bid;
	}
	public void setBid(Bid bid) {
		this.bid = bid;
	}
	
	
	
}
