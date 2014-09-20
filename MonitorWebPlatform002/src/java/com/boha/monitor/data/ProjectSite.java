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
import javax.validation.constraints.Size;

/**
 *
 * @author aubreyM
 */
@Entity
@Table(name = "projectSite")
@NamedQueries({
    @NamedQuery(name = "ProjectSite.findByProjectAndSiteName",
            query = "SELECT p FROM ProjectSite p where p.project.projectID = :projectID and p.projectSiteName = :name"),
    @NamedQuery(name = "ProjectSite.findByProject",
            query = "SELECT p FROM ProjectSite p WHERE p.project.projectID = :projectID order by p.projectSiteName"),
    @NamedQuery(name = "ProjectSite.findByCompany",
            query = "SELECT p FROM ProjectSite p WHERE p.project.company.companyID = :companyID order by p.project.projectName, p.projectSiteName"),
    @NamedQuery(name = "ProjectSite.findByActiveFlag",
            query = "SELECT p FROM ProjectSite p WHERE p.activeFlag = :activeFlag")})
public class ProjectSite implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectSite")
    private List<GcmDevice> gcmDeviceList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "projectSiteID")
    private Integer projectSiteID;
    @Size(max = 255)
    @Column(name = "projectSiteName")
    private String projectSiteName;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "activeFlag")
    private Integer activeFlag;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectSite")
    private List<ProjectSiteTask> projectSiteTaskList;
    @JoinColumn(name = "projectID", referencedColumnName = "projectID")
    @ManyToOne(optional = false)
    private Project project;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectSite")
    private List<ProjectSiteStaff> projectSiteStaffList;

    public ProjectSite() {
    }

    public ProjectSite(Integer projectSiteID) {
        this.projectSiteID = projectSiteID;
    }

    public Integer getProjectSiteID() {
        return projectSiteID;
    }

    public void setProjectSiteID(Integer projectSiteID) {
        this.projectSiteID = projectSiteID;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public List<ProjectSiteTask> getProjectSiteTaskList() {
        return projectSiteTaskList;
    }

    public void setProjectSiteTaskList(List<ProjectSiteTask> projectSiteTaskList) {
        this.projectSiteTaskList = projectSiteTaskList;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
        hash += (projectSiteID != null ? projectSiteID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectSite)) {
            return false;
        }
        ProjectSite other = (ProjectSite) object;
        if ((this.projectSiteID == null && other.projectSiteID != null) || (this.projectSiteID != null && !this.projectSiteID.equals(other.projectSiteID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectSite[ projectSiteID=" + projectSiteID + " ]";
    }

    public List<GcmDevice> getGcmDeviceList() {
        return gcmDeviceList;
    }

    public void setGcmDeviceList(List<GcmDevice> gcmDeviceList) {
        this.gcmDeviceList = gcmDeviceList;
    }

}
