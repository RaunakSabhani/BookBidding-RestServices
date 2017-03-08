
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

 
@Path("ctofservice1")
public class CtoFService { 
    
	static String restSecretKey = "abcd";
    @POST
    @Path("/signup")
    public Response addUser(@FormParam("name") String name, @FormParam("userName") String userName, @FormParam("password") String password, @FormParam("email") String email, @FormParam("gender") String gender, @FormParam("secretKey") String secretKey){
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	System.out.println("NAme is: "+name);
    	System.out.println("Username is: "+userName);
    	System.out.println("Password is: "+password);

    	User usr = new User();
        usr.setName(name);
        usr.setUsername(userName);
        usr.setPassword(password);
        usr.setEmail(email);
        
        System.out.println("Initial constraints are: "+usr.getName() + usr.getUsername() + usr.getEmail());

        usr.setCreatedDate(new java.sql.Date(new Date().getTime()));
        usr.setLastLoginDate(new java.sql.Date(new Date().getTime()));
        usr.setLocation("Location not set");
        usr.setGender(gender);
        
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar calobj = Calendar.getInstance();
        usr.setLastLoginTime(df.format(calobj.getTime()));
                
        UserDAO dao = new UserDAO();
        dao.addUser(usr);
        
        return Response.ok().build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/updateUser")
    public Response updateUser(@FormParam("name") String name, @FormParam("username") String username, @FormParam("userid") String userid, @FormParam("email") String email, @FormParam("gender") String gender, @FormParam("secretKey") String secretKey) throws UnsupportedEncodingException{
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}        
        UserDAO dao = new UserDAO();
        dao.updateUser(URLDecoder.decode(username, "UTF-8"), URLDecoder.decode(userid, "UTF-8"),URLDecoder.decode(name, "UTF-8"), URLDecoder.decode(email, "UTF-8"), URLDecoder.decode(gender, "UTF-8"));
        
        return Response.status(200).build();
    }

    @POST
    @Path("/updateLoginDetails")
    public Response updateLoginDetails(@FormParam("date") String lastLoginDate, @FormParam("time") String lastLoginTime, @FormParam("location") String location, @FormParam("userid") String userid, @FormParam("secretKey") String secretKey)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	try {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    		Date parsedDate = format.parse(lastLoginDate);
    		System.out.println("Parsed date is: "+parsedDate);
    		java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
    		UserDAO dao = new UserDAO();
    		dao.updateLoginDetails(sqlDate, lastLoginTime, location, userid);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return Response.ok().build();
    }

    /*@GET
    @Path("/addBook/{name}/{isbnNo}")
    public Response addBook(@PathParam("name") String name, @PathParam("isbnNo") String isbnNo){
		Book book = new Book();
		book.setBookName(name);
		book.setIsbnNo(isbnNo);
		BookDAO bookDAO = new BookDAO();
		bookDAO.addBook(book);
		return Response.ok().build();	
    }*/
    
    @GET
    @Path("/getBook/{secretKey}/{name}/{isbnNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBook(@PathParam("secretKey") String secretKey, @PathParam("name") String name, @PathParam("isbnNo") String isbnNo){
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	BookDAO bookDAO = new BookDAO();
    	Book book = bookDAO.getBook(name, isbnNo);
    	return book;
    }
    
    @GET
    @Path("/signin/{secretKey}/{userName}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("secretKey") String secretKey, @PathParam("userName") String userName, @PathParam("password") String password) {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	UserDAO dao = new UserDAO();
    	User currentUser = dao.getUser(userName, password);
    	return currentUser;
    }
    
    @GET
    @Path("/getprofile/{secretKey}/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("secretKey") String secretKey, @PathParam("userid") String userid) {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	UserDAO dao = new UserDAO();
    	User currentUser = dao.getUserById(userid);
    	return currentUser;
    }

    @POST
    @Path("/addPost")
    public Response addPost(@FormParam("userid") String userid, @FormParam("price") Double price, @FormParam("bookName") String bookName, @FormParam("authorName") String authorName, @FormParam("publisherName") String publisherName, @FormParam("secretKey") String secretKey)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	System.out.println(userid + price + bookName + authorName + publisherName);
    	UserDAO dao = new UserDAO();
    	User currentUser = dao.getUserById(userid);
    	
    	BookDAO bookDAO = new BookDAO();
    	Book book = bookDAO.getBook(bookName, authorName);
    	if (book == null) {
    		book = new Book();
    		book.setBookName(bookName);
    		book.setAuthorName(authorName);
    		book.setPublisherName(publisherName);
    		bookDAO.addBook(book);
    	}

    	Post post = new Post();

    	post.setUser(currentUser);
    	post.setPostDate(new Date());
    	post.setPrice(price);
    	post.setBook(book);

    	PostDAO postDAO = new PostDAO();
    	postDAO.addPost(post);

    	return Response.ok().build();
    }

    @GET
    @Path("/getPost/{secretKey}/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("secretKey") String secretKey, @PathParam("postId") Long postId)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	System.out.println("Over here");
    	PostDAO dao = new PostDAO();
    	HashMap entries =  dao.getPost(postId);
    	return Response.status(200).entity(entries).build();
    }
    
    @GET
    @Path("/getPosts/{secretKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPosts(@PathParam("secretKey") String secretKey)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	System.out.println("Over here");
    	PostDAO dao = new PostDAO();
    	ArrayList<HashMap> entries = dao.getPosts();
    	
    	GenericEntity<ArrayList<HashMap>> entity = new GenericEntity<ArrayList<HashMap>>(entries) {};
    	return Response.status(200).entity(entries).build();
    }

    @GET
    @Path("/getMyPosts/{secretKey}/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyPosts(@PathParam("secretKey") String secretKey, @PathParam("userid") String userid)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	PostDAO dao = new PostDAO();
    	ArrayList<HashMap> entries = dao.getMyPosts(userid);
    	
    	GenericEntity<ArrayList<HashMap>> entity = new GenericEntity<ArrayList<HashMap>>(entries) {};
    	return Response.status(200).entity(entries).build();
    }

    @POST
    @Path("/addBid")
    public Response addBid(@FormParam("userid") String userid, @FormParam("postID") Long postID, @FormParam("bidPrice") String bidPrice, @FormParam("secretKey") String secretKey)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	UserDAO dao = new UserDAO();
    	User currentUser = dao.getUserById(userid);

    	PostDAO postDAO = new PostDAO();
    	Post post = postDAO.getPostForBid(postID);
    	Bid bid = new Bid();

    	bid.setUser(currentUser);
    	bid.setBidDate(new Date());
    	bid.setBidPrice(Double.parseDouble(bidPrice));
    	bid.setPost(post);

    	BidDAO bidDAO = new BidDAO();
    	bidDAO.addBid(bid);

    	return Response.ok().build();
    }
    
    @GET
    @Path("/getBids/{secretKey}/{postid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBids(@PathParam("secretKey") String secretKey, @PathParam("postid") Long postId)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	BidDAO dao = new BidDAO();
    	ArrayList<HashMap> entries = dao.getBids(postId);
    	GenericEntity<ArrayList<HashMap>> entity = new GenericEntity<ArrayList<HashMap>>(entries) {};
    	return Response.status(200).entity(entries).build();
    }
    
    @GET
    @Path("/getPostsByName/{secretKey}/{bookname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsByName(@PathParam("secretKey") String secretKey, @PathParam("bookname") String bookName) throws UnsupportedEncodingException
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	System.out.println("Book name is: "+bookName);
    	System.out.println("Decoded name is: "+URLDecoder.decode(bookName, "UTF-8"));
    	PostDAO dao = new PostDAO();
    	ArrayList<HashMap> entries = dao.getPostsByName(URLDecoder.decode(bookName, "UTF-8"));
    	
    	GenericEntity<ArrayList<HashMap>> entity = new GenericEntity<ArrayList<HashMap>>(entries) {};
    	return Response.status(200).entity(entries).build();
    }
    
    @POST
    @Path("/addShoppingCart")
    public Response addShoppingCart(@FormParam("userid") String userid, @FormParam("bidId") String bidId,@FormParam("postId") String postId, @FormParam("secretKey") String secretKey){
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	System.out.println("HIII");
    	UserDAO dao = new UserDAO();
    	User currentUser = dao.getUserById(userid);
    	PostDAO postDAO = new PostDAO();
    	Post post = postDAO.getPostForBid(Integer.parseInt(postId));
    	
    	BidDAO bidDAO = new BidDAO();
    	System.out.println("bidid"+bidId);
    	Bid bid  = bidDAO.getBid(Long.parseLong(bidId));
    	
    	ShoppingCart shoppingcart = new ShoppingCart();
    	shoppingcart.setUser(currentUser);
    	shoppingcart.setBid(bid);
    	shoppingcart.setPost(post);
    	shoppingcart.setQuantity(1);
    	shoppingcart.setPrice(bid.getBidPrice()*shoppingcart.getQuantity());
    	Book book = post.getBook();
    	
    	
    	shoppingcart.setDescription(book.getAuthorName()+" ,"+book.getPublisherName());
    	ShoppingCartDao shoppingcartdao = new ShoppingCartDao();
    	shoppingcartdao.addItem(shoppingcart);
    	
    	return Response.ok().build();
    	
    }
    
    @GET
    @Path("/getMyCart/{secretKey}/{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyCart(@PathParam("secretKey") String secretKey, @PathParam("userid") String userid)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	System.out.println("Hello");
    	ShoppingCartDao dao = new ShoppingCartDao();
    	ArrayList<HashMap> entries = dao.getCartItems(userid);
    	
    	GenericEntity<ArrayList<HashMap>> entity = new GenericEntity<ArrayList<HashMap>>(entries) {};
    	return Response.status(200).entity(entries).build();
    }

    @DELETE
    @Path("/deleteItemCart/{shoppingcartid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteItemCart(@PathParam("shoppingcartid") String shoppingcartid){
    	System.out.println("in delete");
    	ShoppingCartDao dao = new ShoppingCartDao();
    	dao.removeCartItems(shoppingcartid);
    	
    	return Response.ok().build();
    }

    @PUT
    @Path("/updateItemCart/{shoppingcartid}/{quantity}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateItemCart(@PathParam("shoppingcartid") String shoppingcartid , @PathParam("quantity") String quantity){
    	System.out.println("in update");
    	ShoppingCartDao dao = new ShoppingCartDao();
    	dao.updateItem(shoppingcartid,quantity);
    	return Response.ok().build();
    }

    @POST
    @Path("/submitShoppingCart")
    public Response submitShoppingCart(@FormParam("userid") String userid, @FormParam("secretKey") String secretKey)
    {
    	if (!restSecretKey.equals(secretKey)) {
    		return null;
    	}
    	ShoppingCartDao dao = new ShoppingCartDao();
    	ArrayList<HashMap> entries = dao.getCartItems(userid);
    	dao.submitCart(entries);
    	
    	return Response.ok().build();
    }
    

}
