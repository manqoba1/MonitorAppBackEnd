/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.monitor.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author aubreyM
 */
@Entity
@Table(name = "executive")
@NamedQueries({
    @NamedQuery(name = "Executive.findByCompany", 
            query = "SELECT e FROM Executive e "
                    + "where e.company.companyID = :companyID "
                    + "order by e.lastName, e.firstName"),
    @NamedQuery(name = "Executive.login", 
            query = "SELECT e FROM Executive e WHERE e.email = :email and e.pin = :pin"),
    @NamedQuery(name = "Executive.findByFirstName", query = "SELECT e FROM Executive e WHERE e.firstName = :firstName"),
    @NamedQuery(name = "Executive.findByLastName", query = "SELECT e FROM Executive e WHERE e.lastName = :lastName"),
    @NamedQuery(name = "Executive.findByEmail", query = "SELECT e FROM Executive e WHERE e.email = :email"),
    @NamedQuery(name = "Executive.findByDateRegistered", query = "SELECT e FROM Executive e WHERE e.dateRegistered = :dateRegistered"),
    @NamedQuery(name = "Executive.findByPin", query = "SELECT e FROM Executive e WHERE e.pin = :pin")})
public class Executive implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "executiveID")
    private Integer executiveID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "firstName")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "lastName")
    private String lastName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 140)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateRegistered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistered;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "pin")
    private String pin;
    @JoinColumn(name = "companyID", referencedColumnName = "companyID")
    @ManyToOne(optional = false)
    private Company company;

    public Executive() {
    }

    public Executive(Integer executiveID) {
        this.executiveID = executiveID;
    }

    public Executive(Integer executiveID, String firstName, String lastName, String email, Date dateRegistered, String pin) {
        this.executiveID = executiveID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateRegistered = dateRegistered;
        this.pin = pin;
    }

    public Integer getExecutiveID() {
        return executiveID;
    }

    public void setExecutiveID(Integer executiveID) {
        this.executiveID = executiveID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (executiveID != null ? executiveID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Executive)) {
            return false;
        }
        Executive other = (Executive) object;
        if ((this.executiveID == null && other.executiveID != null) || (this.executiveID != null && !this.executiveID.equals(other.executiveID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.Executive[ executiveID=" + executiveID + " ]";
    }
    
}
