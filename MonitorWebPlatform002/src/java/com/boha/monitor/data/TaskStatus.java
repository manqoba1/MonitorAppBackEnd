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
@Table(name = "taskStatus")
@NamedQueries({
    @NamedQuery(name = "TaskStatus.findAll", query = "SELECT t FROM TaskStatus t"),
    @NamedQuery(name = "TaskStatus.findByTaskStatusID", query = "SELECT t FROM TaskStatus t WHERE t.taskStatusID = :taskStatusID"),
    @NamedQuery(name = "TaskStatus.findByTaskStatusName", query = "SELECT t FROM TaskStatus t WHERE t.taskStatusName = :taskStatusName")})
public class TaskStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "taskStatusID")
    private Integer taskStatusID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "taskStatusName")
    private String taskStatusName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskStatus")
    private List<ProjectSiteTaskStatus> projectSiteTaskStatusList;

    public TaskStatus() {
    }

    public TaskStatus(Integer taskStatusID) {
        this.taskStatusID = taskStatusID;
    }

    public TaskStatus(Integer taskStatusID, String taskStatusName) {
        this.taskStatusID = taskStatusID;
        this.taskStatusName = taskStatusName;
    }

    public Integer getTaskStatusID() {
        return taskStatusID;
    }

    public void setTaskStatusID(Integer taskStatusID) {
        this.taskStatusID = taskStatusID;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public List<ProjectSiteTaskStatus> getProjectSiteTaskStatusList() {
        return projectSiteTaskStatusList;
    }

    public void setProjectSiteTaskStatusList(List<ProjectSiteTaskStatus> projectSiteTaskStatusList) {
        this.projectSiteTaskStatusList = projectSiteTaskStatusList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskStatusID != null ? taskStatusID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaskStatus)) {
            return false;
        }
        TaskStatus other = (TaskStatus) object;
        if ((this.taskStatusID == null && other.taskStatusID != null) || (this.taskStatusID != null && !this.taskStatusID.equals(other.taskStatusID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.TaskStatus[ taskStatusID=" + taskStatusID + " ]";
    }
    
}
