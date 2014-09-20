/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.monitor.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author aubreyM
 */
@Entity
@Table(name = "projectSiteStaff")
@NamedQueries({
    @NamedQuery(name = "ProjectSiteStaff.login", 
            query = "SELECT p FROM ProjectSiteStaff p where p.companyStaff.email = :email and p.pin = :pin"),
    @NamedQuery(name = "ProjectSiteStaff.findBySiteAndStaff", 
            query = "SELECT p FROM ProjectSiteStaff p "
                    + "where p.companyStaff.companyStaffID = :companyStaffID "
                    + "and p.projectSite.projectSiteID = :projectSiteID"),
    @NamedQuery(name = "ProjectSiteStaff.findByProjectSite", 
            query = "SELECT p FROM ProjectSiteStaff p "
                    + "WHERE p.projectSite.projectSiteID = :projectSiteID "
                    + "order by p.companyStaff.lastName, p.companyStaff.firstName"),
    @NamedQuery(name = "ProjectSiteStaff.findByProject", 
            query = "SELECT p FROM ProjectSiteStaff p "
                    + "WHERE p.projectSite.project.projectID = :projectID "
                    + "order by p.companyStaff.lastName, p.companyStaff.firstName")})
public class ProjectSiteStaff implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "projectSiteStaffID")
    private Integer projectSiteStaffID;
    @Column(name = "activeFlag")
    private Integer activeFlag;
    @Column(name = "pin")
    private String pin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dateRegistered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistered;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectSiteStaff")
    private List<ProjectDiaryRecord> projectDiaryRecordList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectSiteStaff")
    private List<ProjectSiteTaskStatus> projectSiteTaskStatusList;
    @JoinColumn(name = "projectSiteID", referencedColumnName = "projectSiteID")
    @ManyToOne(optional = false)
    private ProjectSite projectSite;
    @JoinColumn(name = "companyStaffID", referencedColumnName = "companyStaffID")
    @ManyToOne(optional = false)
    private CompanyStaff companyStaff;

    public ProjectSiteStaff() {
    }

    public ProjectSiteStaff(Integer projectSiteStaffID) {
        this.projectSiteStaffID = projectSiteStaffID;
    }

    public ProjectSiteStaff(Integer projectSiteStaffID, Date dateRegistered) {
        this.projectSiteStaffID = projectSiteStaffID;
        this.dateRegistered = dateRegistered;
    }

    public Integer getProjectSiteStaffID() {
        return projectSiteStaffID;
    }

    public void setProjectSiteStaffID(Integer projectSiteStaffID) {
        this.projectSiteStaffID = projectSiteStaffID;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public List<ProjectDiaryRecord> getProjectDiaryRecordList() {
        return projectDiaryRecordList;
    }

    public void setProjectDiaryRecordList(List<ProjectDiaryRecord> projectDiaryRecordList) {
        this.projectDiaryRecordList = projectDiaryRecordList;
    }

    public List<ProjectSiteTaskStatus> getProjectSiteTaskStatusList() {
        return projectSiteTaskStatusList;
    }

    public void setProjectSiteTaskStatusList(List<ProjectSiteTaskStatus> projectSiteTaskStatusList) {
        this.projectSiteTaskStatusList = projectSiteTaskStatusList;
    }

    public ProjectSite getProjectSite() {
        return projectSite;
    }

    public void setProjectSite(ProjectSite projectSite) {
        this.projectSite = projectSite;
    }

    public CompanyStaff getCompanyStaff() {
        return companyStaff;
    }

    public void setCompanyStaff(CompanyStaff companyStaff) {
        this.companyStaff = companyStaff;
    }

  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectSiteStaffID != null ? projectSiteStaffID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectSiteStaff)) {
            return false;
        }
        ProjectSiteStaff other = (ProjectSiteStaff) object;
        if ((this.projectSiteStaffID == null && other.projectSiteStaffID != null) || (this.projectSiteStaffID != null && !this.projectSiteStaffID.equals(other.projectSiteStaffID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectSiteStaff[ projectSiteStaffID=" + projectSiteStaffID + " ]";
    }
    
}
