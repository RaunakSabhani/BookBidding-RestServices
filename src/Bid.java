import java.io.Serializable;
import java.util.Date;


public class Bid implements Serializable{

	private int bidId;
	private Post post;
	private User user;
	private Date bidDate;
	private Double bidPrice;
	
	public int getBidId()
	{
		return this.bidId;
	}
	public void setBidId(int bidId)
	{
		this.bidId = bidId;
	}

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
    	this.bidDate = date;
    }
    
    public Double getBidPrice()
    {
    	return bidPrice;
    }
    public void setBidPrice(Double bidPrice)
    {
    	this.bidPrice = bidPrice;
    }

}
