package adeo.leroymerlin.cdp.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * EVENT table informations and her relationships for database.
 */
@Entity
@Table(name = "EVENT")
public class Event implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8822122579775666920L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String imgUrl;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Band> bands;

    @Column
    private Integer nbStars;

    @Column(name = "EVT_COMMENT")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<Band> getBands() {
        return bands;
    }

    public void setBands(Set<Band> bands) {
        this.bands = bands;
    }

    public Integer getNbStars() {
        return nbStars;
    }

    public void setNbStars(Integer nbStars) {
        this.nbStars = nbStars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.id, this.title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object pObj)
    {
        if (this == pObj)
        {
            return true;
        }
        if (pObj == null || getClass() != pObj.getClass())
        {
            return false;
        }

        Event other = (Event) pObj;

        return Objects.equals(this.id, other.id) && Objects.equals(this.title, other.title);
    }
}
