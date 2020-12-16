package adeo.leroymerlin.cdp.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * BAND table informations and her relationships for database.
 */
@Entity
@Table(name = "BAND")
public class Band implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1017283206684889872L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Member> members; // Association with MEMBER

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

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

        Band other = (Band) pObj;

        return Objects.equals(this.id, other.id) && Objects.equals(this.name, other.name);
    }
}
