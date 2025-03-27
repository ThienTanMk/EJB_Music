/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "Track")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Track.findAll", query = "SELECT t FROM Track t"),
    @NamedQuery(name = "Track.findById", query = "SELECT t FROM Track t WHERE t.id = :id"),
    @NamedQuery(name = "Track.findByTitle", query = "SELECT t FROM Track t WHERE t.title = :title"),
    @NamedQuery(name = "Track.findByDescription", query = "SELECT t FROM Track t WHERE t.description = :description"),
    @NamedQuery(name = "Track.findByFilename", query = "SELECT t FROM Track t WHERE t.filename = :filename"),
    @NamedQuery(name = "Track.findByCreatedat", query = "SELECT t FROM Track t WHERE t.createdat = :createdat"),
    @NamedQuery(name = "Track.findByImagename", query = "SELECT t FROM Track t WHERE t.imagename = :imagename")})
    @NamedQuery(name = "Track.findByUserId", query = "SELECT t FROM Track t WHERE t.userid.id = :userId")
public class Track implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Title")
    private String title;
    @Size(max = 2147483647)
    @Column(name = "Description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "File_name")
    private String filename;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdat;
    @Size(max = 100)
    @Column(name = "Image_name")
    private String imagename;
    @JoinColumn(name = "User_id", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private User userid;

    public Track() {
    }

    public Track(String id) {
        this.id = id;
    }

    public Track(String title, String description, String filename, Date createdat, String imageName) {
        this.id = generateID();
        this.title = title;
        this.filename = filename;
        this.createdat = createdat;
        this.imagename = imageName;
        this.description = description;
        this.userid = new User("user1");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Track)) {
            return false;
        }
        Track other = (Track) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Track[ id=" + id + " ]";
    }

    public static String generateID() {
        return UUID.randomUUID().toString();
    }
}
