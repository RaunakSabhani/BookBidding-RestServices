import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.GeneratedValue;

@Entity
public class Bid {

	@Id
	@GeneratedValue
	private Long Id;
	
	//@ManyToOne(cascade=CascadeType.MERGE)
	private Post post;
	//@ManyToOne(cascade=CascadeType.MERGE)
	private User user;
	private Date bidDate;
	private Double bidValue;
	
    public Post getPost()
    {
    	return post;
    }
    public void setPost(Post post)
    {
    	this.post = post;
    }
    
    public User getUser()
    {
    	return user;
    }
    public void setUser(User user)
    {
    	this.user = user;
    }

    public Date getBidDate()
    {
    	return bidDate;
    }
    public void setBidDate(Date date)
    {
    	this.bidDate = bidDate;
    }
    
    public Double getBidValue()
    {
    	return bidValue;
    }
    public void setBidValue(Double bidValue)
    {
    	this.bidValue = bidValue;
    }

}
