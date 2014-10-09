/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.dto.transfer;

import com.boha.monitor.dto.CompanyDTO;
import com.boha.monitor.dto.CompanyStaffDTO;
import com.boha.monitor.dto.CompanyStaffTypeDTO;
import com.boha.monitor.dto.ErrorStoreAndroidDTO;
import com.boha.monitor.dto.ErrorStoreDTO;
import com.boha.monitor.dto.ProjectDTO;
import com.boha.monitor.dto.ProjectDiaryRecordDTO;
import com.boha.monitor.dto.ProjectSiteDTO;
import com.boha.monitor.dto.ProjectSiteStaffDTO;
import com.boha.monitor.dto.ProjectSiteTaskDTO;
import com.boha.monitor.dto.ProjectSiteTaskStatusDTO;
import com.boha.monitor.dto.ProjectStatusTypeDTO;
import com.boha.monitor.dto.TaskStatusDTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ResponseDTO implements Serializable{

    private Integer statusCode;
    private String message, sessionID, GCMRegistrationID;

    //erros
    public static final int OK = 0;
    public static final int SERVER_ERROR = 404;
    public static final int DATABASE_ERROR = 403;
    public static final int DATA_NOT_FOUND = 401;
    public static final int UNKNOWN_REQUEST = 400;
    
    List<String> taskImageFileNameList;
    List<String> siteImageFileNameList;
    List<TaskStatusDTO> taskStatusList = new ArrayList<>();
    List<ProjectStatusTypeDTO> projectStatusTypeList = new ArrayList<>();
    List<ProjectSiteDTO> projectSiteList = new ArrayList<>();
    List<ProjectDTO> projectList = new ArrayList<>();
    List<CompanyStaffDTO> companyStaffList = new ArrayList<>();
    List<ProjectSiteStaffDTO> projectSiteStaffList = new ArrayList<>();
    List<CompanyStaffTypeDTO> companyStaffTypeList = new ArrayList<>();
    List<ProjectDiaryRecordDTO> projectDiaryRecordList = new ArrayList<>();
    List<ProjectSiteTaskDTO> projectSiteTaskList = new ArrayList<>();
    List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList = new ArrayList<>();
    List<ErrorStoreDTO> errorStoreList = new ArrayList<>();
    List<ErrorStoreAndroidDTO> errorStoreAndroidList = new ArrayList<>();
    //
    CompanyDTO company;

    public String getGCMRegistrationID() {
        return GCMRegistrationID;
    }

    public void setGCMRegistrationID(String GCMRegistrationID) {
        this.GCMRegistrationID = GCMRegistrationID;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getTaskImageFileNameList() {
        return taskImageFileNameList;
    }

    public void setTaskImageFileNameList(List<String> taskImageFileNameList) {
        this.taskImageFileNameList = taskImageFileNameList;
    }

    public List<String> getSiteImageFileNameList() {
        return siteImageFileNameList;
    }

    public void setSiteImageFileNameList(List<String> siteImageFileNameList) {
        this.siteImageFileNameList = siteImageFileNameList;
    }

    public List<ErrorStoreDTO> getErrorStoreList() {
        return errorStoreList;
    }

    public void setErrorStoreList(List<ErrorStoreDTO> errorStoreList) {
        this.errorStoreList = errorStoreList;
    }

    public List<ErrorStoreAndroidDTO> getErrorStoreAndroidList() {
        return errorStoreAndroidList;
    }

    public void setErrorStoreAndroidList(List<ErrorStoreAndroidDTO> errorStoreAndroidList) {
        this.errorStoreAndroidList = errorStoreAndroidList;
    }

    public List<TaskStatusDTO> getTaskStatusList() {
        return taskStatusList;
    }

    public void setTaskStatusList(List<TaskStatusDTO> taskStatusList) {
        this.taskStatusList = taskStatusList;
    }

    public List<ProjectStatusTypeDTO> getProjectStatusTypeList() {
        return projectStatusTypeList;
    }

    public void setProjectStatusTypeList(List<ProjectStatusTypeDTO> projectStatusTypeList) {
        this.projectStatusTypeList = projectStatusTypeList;
    }

    public List<ProjectSiteDTO> getProjectSiteList() {
        return projectSiteList;
    }

    public void setProjectSiteList(List<ProjectSiteDTO> projectSiteList) {
        this.projectSiteList = projectSiteList;
    }

    public List<ProjectDTO> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectDTO> projectList) {
        this.projectList = projectList;
    }

    public List<CompanyStaffDTO> getCompanyStaffList() {
        return companyStaffList;
    }

    public void setCompanyStaffList(List<CompanyStaffDTO> companyStaffList) {
        this.companyStaffList = companyStaffList;
    }

    public List<ProjectSiteStaffDTO> getProjectSiteStaffList() {
        return projectSiteStaffList;
    }

    public void setProjectSiteStaffList(List<ProjectSiteStaffDTO> projectSiteStaffList) {
        this.projectSiteStaffList = projectSiteStaffList;
    }

    public List<CompanyStaffTypeDTO> getCompanyStaffTypeList() {
        return companyStaffTypeList;
    }

    public void setCompanyStaffTypeList(List<CompanyStaffTypeDTO> companyStaffTypeList) {
        this.companyStaffTypeList = companyStaffTypeList;
    }

    public List<ProjectDiaryRecordDTO> getProjectDiaryRecordList() {
        return projectDiaryRecordList;
    }

    public void setProjectDiaryRecordList(List<ProjectDiaryRecordDTO> projectDiaryRecordList) {
        this.projectDiaryRecordList = projectDiaryRecordList;
    }

    public List<ProjectSiteTaskDTO> getProjectSiteTaskList() {
        return projectSiteTaskList;
    }

    public void setProjectSiteTaskList(List<ProjectSiteTaskDTO> projectSiteTaskList) {
        this.projectSiteTaskList = projectSiteTaskList;
    }

    public List<ProjectSiteTaskStatusDTO> getProjectSiteTaskStatusList() {
        return projectSiteTaskStatusList;
    }

    public void setProjectSiteTaskStatusList(List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList) {
        this.projectSiteTaskStatusList = projectSiteTaskStatusList;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

}
