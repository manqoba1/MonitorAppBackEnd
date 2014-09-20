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

/**
 *
 * @author aubreyM
 */
@Entity
@Table(name = "projectDiaryRecord")
@NamedQueries({
    @NamedQuery(name = "ProjectDiaryRecord.findByProjectSite", 
            query = "SELECT p FROM ProjectDiaryRecord p where p.projectSiteStaff.projectSite.projectSiteID = :projectSiteID order by p.diaryDate desc"),
    @NamedQuery(name = "ProjectDiaryRecord.findByProject", 
            query = "SELECT p FROM ProjectDiaryRecord p where p.projectSiteStaff.projectSite.project.projectID = :projectID order by p.diaryDate desc"),
    @NamedQuery(name = "ProjectDiaryRecord.findByDiaryDate", query = "SELECT p FROM ProjectDiaryRecord p WHERE p.diaryDate = :diaryDate")})
public class ProjectDiaryRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "projectDiaryRecordID")
    private Integer projectDiaryRecordID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "diaryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diaryDate;
    @JoinColumn(name = "projectStatusTypeID", referencedColumnName = "projectStatusTypeID")
    @ManyToOne(optional = false)
    private ProjectStatusType projectStatusType;
    @JoinColumn(name = "projectSiteStaffID", referencedColumnName = "projectSiteStaffID")
    @ManyToOne(optional = false)
    private ProjectSiteStaff projectSiteStaff;

    public ProjectDiaryRecord() {
    }

    public ProjectDiaryRecord(Integer projectDiaryRecordID) {
        this.projectDiaryRecordID = projectDiaryRecordID;
    }

    public ProjectDiaryRecord(Integer projectDiaryRecordID, Date diaryDate) {
        this.projectDiaryRecordID = projectDiaryRecordID;
        this.diaryDate = diaryDate;
    }

    public Integer getProjectDiaryRecordID() {
        return projectDiaryRecordID;
    }

    public void setProjectDiaryRecordID(Integer projectDiaryRecordID) {
        this.projectDiaryRecordID = projectDiaryRecordID;
    }

    public Date getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(Date diaryDate) {
        this.diaryDate = diaryDate;
    }

    public ProjectStatusType getProjectStatusType() {
        return projectStatusType;
    }

    public void setProjectStatusType(ProjectStatusType projectStatusType) {
        this.projectStatusType = projectStatusType;
    }

    public ProjectSiteStaff getProjectSiteStaff() {
        return projectSiteStaff;
    }

    public void setProjectSiteStaff(ProjectSiteStaff projectSiteStaff) {
        this.projectSiteStaff = projectSiteStaff;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectDiaryRecordID != null ? projectDiaryRecordID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectDiaryRecord)) {
            return false;
        }
        ProjectDiaryRecord other = (ProjectDiaryRecord) object;
        if ((this.projectDiaryRecordID == null && other.projectDiaryRecordID != null) || (this.projectDiaryRecordID != null && !this.projectDiaryRecordID.equals(other.projectDiaryRecordID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectDiaryRecord[ projectDiaryRecordID=" + projectDiaryRecordID + " ]";
    }
    
}
