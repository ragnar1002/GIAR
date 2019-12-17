package it.unipi.giar.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.client.MongoCollection;

import it.unipi.giar.MongoDriver;

public class Game {
	private final long id;
	private String slug;
	private String name;
	private String nameOriginal;
	private String description;
	private int metacritic;
	private Date released;
	private String backgroundImage;
	private double rating;
	private ArrayList<Rating> ratings;
	private long added;
	private long addedWishlist;
	private long addedMyGames;
	private ArrayList<Platform> platforms;
	private ArrayList<Developer> developers;
	private ArrayList<Genre> genres;
	private ArrayList<Game> searchGames;

	public Game(String slug, String name, String nameOriginal, String description, int metacritic, Date released,
			String backgroundImage, double rating, ArrayList<Rating> ratings, long added, long addedWishlist,
			long addedMyGames) {
		this.slug = slug;
		this.name = name;
		this.nameOriginal = nameOriginal;
		this.description = description;
		this.metacritic = metacritic;
		this.released = released;
		this.backgroundImage = backgroundImage;
		this.rating = rating;
		this.ratings = ratings;
		this.added = added;
		this.addedWishlist = addedWishlist;
		this.addedMyGames = addedMyGames;
	}

	public static List<String> getAllPlatformsList() {
		//MATILDE, i need this function to populate the fields of the combobox for platforms
		//this function returns the list of the platforms existing in the database. distinct.
	
	  }
	
	public static List<String> getAllYearsList() {
		//MATILDE, i need this function to populate the fields of the combobox for years
		//this function returns the list of the years existing in the database. distinct.

	  }
	
	public static List<String> getAllGenresList() {
		//MATILDE, i need this function to populate the fields of the combobox for genres
		//this function returns the list of the genres existing in the database. distinct.
		
	    //MongoDriver md = MongoDriver.getInstance();
	    //MongoCollection<Document> collection = md.getCollection("games");
	  }

	public ArrayList<Game> searchGames(String search) {
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;

		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.find(eq("slug", "game")).iterator();
			try {
				while (cursor.hasNext()) {
					System.out.println(cursor.next().toJson());
				}
			} finally {
				cursor.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setnameOriginal(String nameOriginal) {
		this.nameOriginal = nameOriginal;
	}

	public void setdescription(String description) {
		this.description = description;
	}

	public void setMetacritic(int metacritic) {
		this.metacritic = metacritic;
	}

	public void setReleased(Date released) {
		this.released = released;
	}

	public void setbackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setRatings(ArrayList<Rating> ratings) {
		this.ratings = ratings;
	}

	public void setAdded(long added) {
		this.added = added;
	}

	public void setAddedWishlist(long addedWishlist) {
		this.addedWishlist = addedWishlist;
	}

	public void setAddedMyGames(long addedMyGames) {
		this.addedMyGames = addedMyGames;
	}

	public String getSlug() {
		return this.slug;
	}

	public String getName() {
		return this.name;
	}

	public String getnameOriginal() {
		return this.nameOriginal;
	}

	public String getdescription() {
		return this.description;
	}

	public int getMetacritic() {
		return this.metacritic;
	}

	public Date getReleased() {
		return this.released;
	}

	public String getbackgroundImage() {
		return this.backgroundImage;
	}

	public double getRating() {
		return this.rating;
	}

	public ArrayList<Rating> getRatings() {
		return this.ratings;
	}

	public long getAdded() {
		return this.added;
	}

	public long getAddedWishlist() {
		return this.addedWishlist;
	}

	public long getAddedMyGames() {
		return this.addedMyGames;
	}

}
