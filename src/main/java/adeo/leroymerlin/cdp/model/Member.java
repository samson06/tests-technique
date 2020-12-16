package adeo.leroymerlin.cdp.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * MEMBER table informations and her relationships for database.
 */
@Entity
@Table(name = "MEMBER")
public class Member implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -290973822391869641L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.id, this.name);
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

        Member other = (Member) pObj;

        return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name);
    }
}
