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
@Table(name = "companyStaffType")
@NamedQueries({
    @NamedQuery(name = "CompanyStaffType.findAll", query = "SELECT c FROM CompanyStaffType c"),
    @NamedQuery(name = "CompanyStaffType.findByCompanyStaffTypeID", query = "SELECT c FROM CompanyStaffType c WHERE c.companyStaffTypeID = :companyStaffTypeID"),
    @NamedQuery(name = "CompanyStaffType.findByCompanyStaffTypeName", query = "SELECT c FROM CompanyStaffType c WHERE c.companyStaffTypeName = :companyStaffTypeName")})
public class CompanyStaffType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "companyStaffTypeID")
    private Integer companyStaffTypeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "companyStaffTypeName")
    private String companyStaffTypeName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyStaffType")
    private List<CompanyStaff> companyStaffList;

    public CompanyStaffType() {
    }

    public CompanyStaffType(Integer companyStaffTypeID) {
        this.companyStaffTypeID = companyStaffTypeID;
    }

    public CompanyStaffType(Integer companyStaffTypeID, String companyStaffTypeName) {
        this.companyStaffTypeID = companyStaffTypeID;
        this.companyStaffTypeName = companyStaffTypeName;
    }

    public Integer getCompanyStaffTypeID() {
        return companyStaffTypeID;
    }

    public void setCompanyStaffTypeID(Integer companyStaffTypeID) {
        this.companyStaffTypeID = companyStaffTypeID;
    }

    public String getCompanyStaffTypeName() {
        return companyStaffTypeName;
    }

    public void setCompanyStaffTypeName(String companyStaffTypeName) {
        this.companyStaffTypeName = companyStaffTypeName;
    }

    public List<CompanyStaff> getCompanyStaffList() {
        return companyStaffList;
    }

    public void setCompanyStaffList(List<CompanyStaff> companyStaffList) {
        this.companyStaffList = companyStaffList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyStaffTypeID != null ? companyStaffTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyStaffType)) {
            return false;
        }
        CompanyStaffType other = (CompanyStaffType) object;
        if ((this.companyStaffTypeID == null && other.companyStaffTypeID != null) || (this.companyStaffTypeID != null && !this.companyStaffTypeID.equals(other.companyStaffTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.CompanyStaffType[ companyStaffTypeID=" + companyStaffTypeID + " ]";
    }
    
}
