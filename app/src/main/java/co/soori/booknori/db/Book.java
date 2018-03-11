package co.soori.booknori.db;

/**
 * Created by Soo-Young on 2018-03-10.
 */

public class Book {

    private int id;
    private String name;
    private String author;
    private String image;
    private String story;

    private String publisher;
    private String stars;
    private String[] booknotes;

    public Book(){}

    public Book(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Book(int id, String name, String author, String story, String image){
        this.id = id;
        this.name = name;
        this.author = author;
        this.story = story;
        this.image = image;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setStory(String story){
        this.story = story;
    }

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }

    public void setStars(String stars){
        this.stars = stars;
    }

    public void setBooknotes(String[] booknotes){
        this.booknotes = booknotes;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getAuthor(){
        return this.author;
    }

    public String getImage(){
        return this.image;
    }

    public String getStory(){
        return this.story;
    }

    public String getPublisher(){
        return this.publisher;
    }

    public String getStars(){
        return this.stars;
    }

    public String[] getBooknotes(){
        return this.booknotes;
    }


}
