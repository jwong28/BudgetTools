package jwong28.github.budgetapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post {

    public String dateOfPost;
    public String location;
    public String item;
    public String description = "";
    public Double amount;

    public Post(){

    }

    public Post(Date day, String l,String i, String d, String a){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateOfPost = dateFormat.format(day);
//        this.dateOfPost = day.toString();
        this.location = l;
        this.item = i;
        this.description = d;
        this.amount = Double.parseDouble(a);
    }

    public String getDate(){
        return dateOfPost;
    }

    public String getLocation() { return location; }

    public String getItem(){
        return item;
    }

    public String getDescription(){
        return description;
    }

    public Double getAmount(){
        return amount;
    }


}
