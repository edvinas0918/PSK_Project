/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "memberformfield")
@NamedQueries({
    @NamedQuery(name = "Memberformfield.findAll", query = "SELECT m FROM Memberformfield m"),
    @NamedQuery(name = "Memberformfield.findById", query = "SELECT m FROM Memberformfield m WHERE m.id = :id"),
    @NamedQuery(name = "Memberformfield.findByFieldName", query = "SELECT m FROM Memberformfield m WHERE m.fieldName = :fieldName"),
    @NamedQuery(name = "Memberformfield.findByVisible", query = "SELECT m FROM Memberformfield m WHERE m.visible = :visible")})
@XmlRootElement
public class Memberformfield implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private int optLockVersion;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "FieldName")
    private String fieldName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Visible")
    private boolean visible;

    public Memberformfield() {
    }

    public Memberformfield(Integer id) {
        this.id = id;
    }

    public Memberformfield(Integer id, String fieldName, boolean visible) {
        this.id = id;
        this.fieldName = fieldName;
        this.visible = visible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
        if (!(object instanceof Memberformfield)) {
            return false;
        }
        Memberformfield other = (Memberformfield) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.Memberformfield[ id=" + id + " ]";
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
    }
    
}
