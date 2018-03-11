package co.soori.booknori.db;

/**
 * Created by Soo-Young on 2018-03-10.
 */

public class Person {
    private int id;
    private String name;
    private String story;
    private String image;

    public Person(){}

    public Person(int id){
        this.id = id;
    }

    public Person(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Person(int id, String name, String story, String image){
        this.id = id;
        this.name = name;
        this.story = story;
        this.image = image;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setStory(String story){
        this.story = story;
    }

    public void setImage(String image){
        this.image = image;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getStory(){
        return this.story;
    }

    public String getImage(){
        return this.image;
    }


}
