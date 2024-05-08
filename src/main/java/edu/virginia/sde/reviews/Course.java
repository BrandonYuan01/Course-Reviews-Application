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

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public int getRating() {
        int total = 0;
        int count = 0;
        List<Review> reviews = this.getReviews();

        for (var review : reviews) {
            int rating  = review.getRating();
            total += rating;
            count += 1;
        }
        if (count == 0) {
            return -1;
        }
        return total/count;
    }
    public String toString() {
        String rating;
        String subject = "          " + this.getSubject();
        String courseNumber = String.format("%04d", this.getCourseNumber());
        String title = this.getTitle();
        if (this.getRating() == -1) {
            rating = "";
        }
        else {
            rating = String.format("%.2f",this.getRating());
        }
        return String.format("%-33s %-24s %-80s %-10s", subject, courseNumber, title, rating);
    }
}
