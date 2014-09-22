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
@Table(name = "projectStatusType")
@NamedQueries({
    @NamedQuery(name = "ProjectStatusType.findAll", 
            query = "SELECT p FROM ProjectStatusType p order by p.projectStatusName"),
    @NamedQuery(name = "ProjectStatusType.findByProjectStatusTypeID", query = "SELECT p FROM ProjectStatusType p WHERE p.projectStatusTypeID = :projectStatusTypeID"),
    @NamedQuery(name = "ProjectStatusType.findByProjectStatusName", query = "SELECT p FROM ProjectStatusType p WHERE p.projectStatusName = :projectStatusName")})
public class ProjectStatusType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "projectStatusTypeID")
    private Integer projectStatusTypeID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "projectStatusName")
    private String projectStatusName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "projectStatusType")
    private List<ProjectDiaryRecord> projectDiaryRecordList;
    
    public ProjectStatusType() {
    }

    public ProjectStatusType(Integer projectStatusTypeID) {
        this.projectStatusTypeID = projectStatusTypeID;
    }

    public ProjectStatusType(Integer projectStatusTypeID, String projectStatusName) {
        this.projectStatusTypeID = projectStatusTypeID;
        this.projectStatusName = projectStatusName;
    }

    public Integer getProjectStatusTypeID() {
        return projectStatusTypeID;
    }

    public void setProjectStatusTypeID(Integer projectStatusTypeID) {
        this.projectStatusTypeID = projectStatusTypeID;
    }

    public String getProjectStatusName() {
        return projectStatusName;
    }

    public void setProjectStatusName(String projectStatusName) {
        this.projectStatusName = projectStatusName;
    }

    public List<ProjectDiaryRecord> getProjectDiaryRecordList() {
        return projectDiaryRecordList;
    }

    public void setProjectDiaryRecordList(List<ProjectDiaryRecord> projectDiaryRecordList) {
        this.projectDiaryRecordList = projectDiaryRecordList;
    }

 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectStatusTypeID != null ? projectStatusTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectStatusType)) {
            return false;
        }
        ProjectStatusType other = (ProjectStatusType) object;
        if ((this.projectStatusTypeID == null && other.projectStatusTypeID != null) || (this.projectStatusTypeID != null && !this.projectStatusTypeID.equals(other.projectStatusTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectStatusType[ projectStatusTypeID=" + projectStatusTypeID + " ]";
    }
    
}
