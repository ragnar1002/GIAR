package it.unipi.giar.Data;

import static com.mongodb.client.model.Filters.eq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UnwindOptions;

import org.bson.Document;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.skip;
import static com.mongodb.client.model.Projections.computed;


import it.unipi.giar.MongoDriver;

public class Game {
	private int id;
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

	public Game(Document document) {
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-mm-dd");

		this.id = (document.get("id") == null) ? 0 : document.getInteger("id");
		this.slug = (document.get("slug") == null) ? "" : document.getString("slug");
		this.name = (document.get("name") == null) ? "" : document.getString("name");
		this.nameOriginal = (document.get("name_original") == null) ? "" : document.getString("name_original");
		this.description = (document.get("description_raw") == null) ? "" : document.getString("description_raw");
		this.metacritic = (document.get("metacritic") == null) ? 0 : document.getInteger("metacritic");
		this.backgroundImage = (document.get("background_image") == null) ? "" : document.getString("background_image");
		this.rating = (document.get("rating") == null) ? 0 : document.getDouble("rating");
		// this.addedWishlist = (document.get("added_by_status") == null) ? 0 :
		// document.getLong("wishlist");
		// this.addedMyGames = (document.get("mygames") == null) ? 0 :
		// document.getLong("mygames");
		// this.added = (this.addedWishlist + this.addedMyGames);
		
		List<Document> platformDoc = new ArrayList<Document>();
		platformDoc = (List<Document>)document.get("platforms");

		if (platformDoc.isEmpty()) {
			this.platforms = new ArrayList<Platform>();
		} else {
			this.platforms = new ArrayList<Platform>();

			for (Document doc : platformDoc) {
				this.platforms.add(new Platform((Document)doc.get("platform")));
			}
		}

		List<Document> developDoc = new ArrayList<Document>();
		developDoc = (List<Document>)document.get("developers");

		if (developDoc.isEmpty()) {
			this.developers = new ArrayList<Developer>();
		} else {
			this.developers = new ArrayList<Developer>();

			for (Document doc : developDoc) {
				this.developers.add(new Developer(doc));
			}
		}

		List<Document> genresDoc = new ArrayList<Document>();
		genresDoc = (List<Document>)document.get("genres");

		if (genresDoc.isEmpty()) {
			this.genres = new ArrayList<Genre>();
		} else {
			this.genres = new ArrayList<Genre>();

			for (Document doc : genresDoc) {
				this.genres.add(new Genre(doc));
			}
		}

		try {
			this.released = (document.get("released") == null) ? new Date()
					: formatDate.parse(document.getString("released"));
		} catch (ParseException e) {
			this.released = new Date();
		}
	}

	public Game(int id, String slug, String name, String nameOriginal, String description, int metacritic,
			Date released, String backgroundImage, double rating, ArrayList<Rating> ratings, long added,
			long addedWishlist, long addedMyGames, ArrayList<Platform> platforms, ArrayList<Developer> developers,
			ArrayList<Genre> genres) {
		this.id = id;
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
		this.platforms = platforms;
		this.developers = developers;
		this.genres = genres;
	}

	public static List<String> getAllPlatformsList() {
		// MATILDE, i need this function to populate the fields of the combobox for
		// platforms. this function returns the list of the platforms existing in the database.	
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		List<String> items = new ArrayList<>();		
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind("$platforms"), group("$platforms.platform.name"), sort(ascending("_id")))).iterator();
			while (cursor.hasNext()) {
				items.add(cursor.next().getString("_id"));
			}
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public static List<String> getAllYearsList() {
		// MATILDE, i need this function to populate the fields of the combobox for
		// years, this function returns the list of the years existing in the database.
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		List<String> items = new ArrayList<>();		
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(group("$year"), sort(ascending("_id")), skip(1))).iterator();
			while (cursor.hasNext()) {
				items.add(cursor.next().getString("_id"));
			}
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public static List<String> getAllGenresList() {
		// MATILDE, i need this function to populate the fields of the combobox for
		// genres this function returns the list of the genres existing in the database.
		
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;
		List<String> items = new ArrayList<>();		
		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(unwind("$genres"), group("$genres.name"), sort(ascending("_id")))).iterator();
			while (cursor.hasNext()) {
				items.add(cursor.next().getString("_id"));
			}
			cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return items;

		
	}
	
	public static ArrayList<Game> searchGames(String search) {
		ArrayList<Game> listGames = new ArrayList<Game>();
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;

		BasicDBObject query = new BasicDBObject();
		query.put("name", Pattern.compile(search, Pattern.CASE_INSENSITIVE));

		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.find(query).limit(10).iterator();

			try {
				while (cursor.hasNext()) {
					Document document = cursor.next();
					listGames.add(new Game(document));
				}
			} finally {
				cursor.close();
			}

			return listGames;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Game findGame(String name) {
		try {
			MongoDriver md = MongoDriver.getInstance();
			MongoCollection<Document> collection = md.getCollection("games");
			Document game = collection.find(eq("name", name)).first();

			return new Game(game);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static Document findGameDocument(String name) {
		try {
			MongoDriver md = MongoDriver.getInstance();
			MongoCollection<Document> collection = md.getCollection("games");
			Document game = collection.find(eq("name", name)).first();
			
			return game;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	public static ArrayList<Game> browseGames(String key, String value){
		ArrayList<Game> listGames = new ArrayList<Game>();
		MongoDriver driver = null;
		MongoCollection<Document> collection = null;

		BasicDBObject query = new BasicDBObject();
		query.put(key, value);

		try {
			driver = MongoDriver.getInstance();
			collection = driver.getCollection("games");
			MongoCursor<Document> cursor = collection.find(query).limit(10).iterator();	//TODO CHECK THE LIMIT, without limit, if the list is big, the system is slow.
			
			try {
				while (cursor.hasNext()) {
					Document document = cursor.next();
					listGames.add(new Game(document));
				}
			} finally {
				cursor.close();
			}
			
			return listGames;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ArrayList<Game> browseGamesPerPlatform(String value) {
		return browseGames("platforms.platform.name", value);
	}
	public static ArrayList<Game> browseGamesPerGenre(String value) {
		return browseGames("genres.name", value);
	}
	public static ArrayList<Game> browseGamesPerYear(String value) {
		return browseGames("year", value);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameOriginal(String nameOriginal) {
		this.nameOriginal = nameOriginal;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMetacritic(int metacritic) {
		this.metacritic = metacritic;
	}

	public void setReleased(Date released) {
		this.released = released;
	}

	public void setBackgroundImage(String backgroundImage) {
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

	public int getId() {
		return this.id;
	}

	public String getSlug() {
		return this.slug;
	}

	public String getName() {
		return this.name;
	}

	public String getNameOriginal() {
		return this.nameOriginal;
	}

	public String getDescription() {
		return this.description;
	}

	public int getMetacritic() {
		return this.metacritic;
	}

	public Date getReleased() {
		return this.released;
	}

	public String getBackgroundImage() {
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

	public ArrayList<Platform> getPlatforms() {
		return this.platforms;
	};

	public ArrayList<Developer> getDevelopers() {
		return this.developers;
	};

	public ArrayList<Genre> getGenres() {
		return this.genres;
	};

}