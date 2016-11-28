
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
 
@Path("ctofservice1")
public class CtoFService { 
    
    @POST
    @Path("/signup")
    public Response addUser(@FormParam("name") String name, @FormParam("userName") String userName, @FormParam("password") String password, @FormParam("email") String email, @FormParam("birthDate") String birthDate, @FormParam("gender") String gender){
    	System.out.println("Date is: "+birthDate);
    	try {
    	User usr = new User();
        usr.setName(name);
        usr.setUsername(userName);
        usr.setPassword(password);
        usr.setEmail(email);
        
        System.out.println("Initial constraints are: "+usr.getName() + usr.getUsername() + usr.getEmail());
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date parsedDate = format.parse(birthDate);
		
		System.out.println("Parsed date is: "+parsedDate);
		java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
        usr.setBirthDate(sqlDate);
        usr.setCreatedDate(new java.sql.Date(new Date().getTime()));
        usr.setLastLoginDate(new java.sql.Date(new Date().getTime()));
        usr.setLocation("Richardson");
        usr.setGender(gender);
        
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar calobj = Calendar.getInstance();
        usr.setLastLoginTime(df.format(calobj.getTime()));
                
        UserDAO dao = new UserDAO();
        dao.addUser(usr);
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return Response.ok().build();
    }

    @POST
    @Path("/updateUser")
    public Response updateUser(@FormParam("name") String name, @FormParam("userName") String userName, @FormParam("password") String password, @FormParam("email") String email, @FormParam("birthDate") String birthDate){
    	System.out.println("Date is: "+birthDate);
        
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date parsedDate = new Date();
		try {
			parsedDate = format.parse(birthDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Parsed date is: "+parsedDate);
		java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
        String gender = "M";
                
        UserDAO dao = new UserDAO();
        dao.updateUser(userName, password, name, email, sqlDate, gender);
        
        return Response.ok().build();
    }

    @POST
    @Path("/updateLoginDetails")
    public Response updateLoginDetails(@FormParam("date") String lastLoginDate, @FormParam("time") String lastLoginTime, @FormParam("username") String username, @FormParam("password") String password)
    {
    	try {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
    		Date parsedDate = format.parse(lastLoginDate);
    		System.out.println("Parsed date is: "+parsedDate);
    		java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
    		UserDAO dao = new UserDAO();
    		dao.updateLoginDetails(sqlDate, lastLoginTime, username, password);
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
    @Path("/getBook/{name}/{isbnNo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBook(@PathParam("name") String name, @PathParam("isbnNo") String isbnNo){
    	BookDAO bookDAO = new BookDAO();
    	Book book = bookDAO.getBook(name, isbnNo);
    	return book;
    }
    
    @GET
    @Path("/signin/{userName}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("userName") String userName, @PathParam("password") String password) {
    	UserDAO dao = new UserDAO();
    	User currentUser = dao.getUser(userName, password);
    	return currentUser;
    }
    
    @POST
    @Path("/addPost")
    public Response addPost(@FormParam("username") String username, @FormParam("password") String password, @FormParam("price") Double price, @FormParam("bookName") String bookName, @FormParam("authorName") String authorName, @FormParam("publisherName") String publisherName)
    {
    	System.out.println(username + password + price + bookName + authorName + publisherName);
    	UserDAO dao = new UserDAO();
    	User currentUser = dao.getUser(username, password);
    	
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
    @Path("/getPost/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postId") Long postId)
    {
    	PostDAO dao = new PostDAO();
    	HashMap entries =  dao.getPost(postId);
    	return Response.status(200).entity(entries).build();
    }
    
    @GET
    @Path("/getPosts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> getPosts()
    {
    	PostDAO dao = new PostDAO();
    	return dao.getPosts();
    }

    @GET
    @Path("/getMyPosts/{username}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyPosts(@PathParam("username") String username, @PathParam("password") String password)
    {//JSONObject jsonObject = new JSONObject();
    	PostDAO dao = new PostDAO();
    	ArrayList<HashMap> entries = dao.getMyPosts(username, password);
    	
/*			jsonObject.append("posts", entries);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	//return entries;
    	GenericEntity<ArrayList<HashMap>> entity = new GenericEntity<ArrayList<HashMap>>(entries) {};
    	return Response.status(200).entity(entries).build();
    	//return new JSONArray(entries);
    }

    @GET
    @Path("/addBid/{userName}/{password}/{postID}/{bidValue}")
    public Response addBid(@PathParam("userName") String userName, @PathParam("password") String password, @PathParam("postID") Long postID, @PathParam("bidValue") Double bidValue)
    {
    	UserDAO dao = new UserDAO();
    	User currentUser = dao.getUser(userName, password);

    	PostDAO postDAO = new PostDAO();
    	//Post post = postDAO.getPost(postID);
    	Post post = new Post();
    	Bid bid = new Bid();

    	bid.setUser(currentUser);
    	bid.setBidDate(new Date());
    	bid.setBidValue(bidValue);
    	bid.setPost(post);

    	BidDAO bidDAO = new BidDAO();
    	bidDAO.addBid(bid);

    	return Response.ok().build();
    }
    

}
