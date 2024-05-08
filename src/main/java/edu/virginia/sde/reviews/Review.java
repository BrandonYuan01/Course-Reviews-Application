package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {
    int Id;
    int rating;
    Timestamp timestamp;
    String comment;
    String username;
    int courseid;
    public Review(int Id, int rating, Timestamp timestamp, String comment, String username, int courseid){
        this.Id = Id;
        this.rating = rating;
        this.timestamp = timestamp;
        this.comment = comment;
        this.username = username;
        this.courseid = courseid;
    }

    public int getId() { return Id; }

    public void setId(int Id) { this.Id = Id; }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCourse() {
        return courseid;
    }

    public void setCourse(int courseid) {
        this.courseid = courseid;
    }
}
