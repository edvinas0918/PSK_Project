package EJBComponents;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Dziugas on 4/2/2016.
 */
@Entity
@Table(name = "comments")
public class CommentsEntity {
    private int id;
    private String myuser;
    private String email;
    private String webpage;
    private Date datum;
    private String summary;
    private String comments;
    private String address;
    private String testytest;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "MYUSER")
    public String getMyuser() {
        return myuser;
    }

    public void setMyuser(String myuser) {
        this.myuser = myuser;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "WEBPAGE")
    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    @Basic
    @Column(name = "DATUM")
    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    @Basic
    @Column(name = "SUMMARY")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "COMMENTS")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "testytest")
    public String getTestytest() {
        return testytest;
    }

    public void setTestytest(String testytest) {
        this.testytest = testytest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentsEntity that = (CommentsEntity) o;

        if (id != that.id) return false;
        if (myuser != null ? !myuser.equals(that.myuser) : that.myuser != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (webpage != null ? !webpage.equals(that.webpage) : that.webpage != null) return false;
        if (datum != null ? !datum.equals(that.datum) : that.datum != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (testytest != null ? !testytest.equals(that.testytest) : that.testytest != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (myuser != null ? myuser.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (webpage != null ? webpage.hashCode() : 0);
        result = 31 * result + (datum != null ? datum.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (testytest != null ? testytest.hashCode() : 0);
        return result;
    }
}
