package edu.virginia.sde.reviews;

import java.sql.Timestamp;

public class Review {
    int rating;
    Timestamp timestamp;
    String comment;
    String username;
    Course course;
    public Review(int rating, Timestamp timestamp, String comment, String username, Course course){
        this.rating = rating;
        this.timestamp = timestamp;
        this.comment = comment;
        this.username = username;
        this.course = course;
    }

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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
