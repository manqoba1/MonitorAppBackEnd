/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.dto.transfer;

import java.util.List;

/**
 *
 * @author aubreyM
 */
public class PhotoUploadDTO {

    public static final int SITE_IMAGE = 1, TASK_IMAGE = 2, SITE_STAFF_IMAGE=3;
    private int companyID, projectSiteStaffID, projectID, projectSiteID, projectSiteTaskID, pictureType;
    private List<String> tags;

    public int getPictureType() {
        return pictureType;
    }

    public void setPictureType(int pictureType) {
        this.pictureType = pictureType;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getProjectSiteStaffID() {
        return projectSiteStaffID;
    }

    public void setProjectSiteStaffID(int projectSiteStaffID) {
        this.projectSiteStaffID = projectSiteStaffID;
    }

    

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getProjectSiteID() {
        return projectSiteID;
    }

    public void setProjectSiteID(int projectSiteID) {
        this.projectSiteID = projectSiteID;
    }

    public int getProjectSiteTaskID() {
        return projectSiteTaskID;
    }

    public void setProjectSiteTaskID(int projectSiteTaskID) {
        this.projectSiteTaskID = projectSiteTaskID;
    }

    

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
   

}
