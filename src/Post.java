import java.io.Serializable;
import java.util.Date;

/*import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;*/

public class Post implements Serializable {
 
    private int postId;
    private User user;
    private Date postDate;
    private Book book;
    private Double price;
    
    public int getPostId() {
    	return this.postId;
    }
    
    public void setPostId(int postId) {
    	this.postId = postId;
    }

    public User getUser()
    {
    	return user;
    }
    public void setUser(User user)
    {
    	this.user = user;
    }

    public Date getPostDate() {
        return postDate;
    }
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
    
    public Book getBook()
    {
    	return book;
    }
    public void setBook(Book book)
    {
    	this.book = book;
    }
    
    public Double getPrice()
    {
    	return this.price;
    }
    public void setPrice(Double price)
    {
    	this.price = price;
    }
}
