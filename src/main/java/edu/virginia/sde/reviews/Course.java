package edu.virginia.sde.reviews;

import java.util.List;

public class Course {
    int courseNumber;
    String subject;
    String title;
    List<Review> reviews;

    public Course(int courseNumber, String subject, String title, List<Review> reviews) {
        this.courseNumber = courseNumber;
        this.subject = subject;
        this.title = title;
        this.reviews = reviews;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
