/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.monitor.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author aubreyM
 */
@Entity
@Table(name = "companyStaff")
@NamedQueries({
    @NamedQuery(name = "CompanyStaff.findAll", query = "SELECT c FROM CompanyStaff c"),
    @NamedQuery(name = "CompanyStaff.findByCompanyStaffID", query = "SELECT c FROM CompanyStaff c WHERE c.companyStaffID = :companyStaffID"),
    @NamedQuery(name = "CompanyStaff.findByFirstName", query = "SELECT c FROM CompanyStaff c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "CompanyStaff.findByLastName", query = "SELECT c FROM CompanyStaff c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "CompanyStaff.findByEmail", query = "SELECT c FROM CompanyStaff c WHERE c.email = :email"),
    @NamedQuery(name = "CompanyStaff.findByCellphone", query = "SELECT c FROM CompanyStaff c WHERE c.cellphone = :cellphone")})
public class CompanyStaff implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "companyStaffID")
    private Integer companyStaffID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "firstName")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "lastName")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 50)
    @Column(name = "cellphone")
    private String cellphone;
    @JoinColumn(name = "companyStaffTypeID", referencedColumnName = "companyStaffTypeID")
    @ManyToOne(optional = false)
    private CompanyStaffType companyStaffType;
    @JoinColumn(name = "companyID", referencedColumnName = "companyID")
    @ManyToOne(optional = false)
    private Company company;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStaff")
    private List<ProjectSiteStaff> projectSiteStaffList;

    public CompanyStaff() {
    }

    public CompanyStaff(Integer companyStaffID) {
        this.companyStaffID = companyStaffID;
    }

    public CompanyStaff(Integer companyStaffID, String firstName, String lastName, String email) {
        this.companyStaffID = companyStaffID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getCompanyStaffID() {
        return companyStaffID;
    }

    public void setCompanyStaffID(Integer companyStaffID) {
        this.companyStaffID = companyStaffID;
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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public CompanyStaffType getCompanyStaffType() {
        return companyStaffType;
    }

    public void setCompanyStaffType(CompanyStaffType companyStaffType) {
        this.companyStaffType = companyStaffType;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<ProjectSiteStaff> getProjectSiteStaffList() {
        return projectSiteStaffList;
    }

    public void setProjectSiteStaffList(List<ProjectSiteStaff> projectSiteStaffList) {
        this.projectSiteStaffList = projectSiteStaffList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyStaffID != null ? companyStaffID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyStaff)) {
            return false;
        }
        CompanyStaff other = (CompanyStaff) object;
        if ((this.companyStaffID == null && other.companyStaffID != null) || (this.companyStaffID != null && !this.companyStaffID.equals(other.companyStaffID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.CompanyStaff[ companyStaffID=" + companyStaffID + " ]";
    }
    
}
