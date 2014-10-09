/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.util;

import com.boha.monitor.dto.transfer.RequestDTO;
import com.boha.monitor.dto.transfer.ResponseDTO;

/**
 *
 * @author aubreyM
 */
public class TrafficCop {

    public static ResponseDTO processRequest(RequestDTO req, DataUtil dataUtil, ListUtil listUtil) throws DataException {
        ResponseDTO r = new ResponseDTO();
        switch (req.getRequestType()) {
            case RequestDTO.REGISTER_COMPANY:
                r = dataUtil.registerCompany(req.getCompany(), req.getCompanyStaff());
                break;
            case RequestDTO.REGISTER_COMPANY_STAFF:
                r = dataUtil.registerCompanyStaff(req.getCompanyStaff());
                break;
            case RequestDTO.REGISTER_PROJECT:
                r = dataUtil.registerProject(req.getProject());
                break;
            case RequestDTO.REGISTER_PROJECT_SITE:
                r = dataUtil.registerProjectSite(req.getProjectSite());
                break;
            case RequestDTO.REGISTER_PROJECT_SITE_STAFF:
                r = dataUtil.registerProjectSiteStaff(req.getProjectSiteStaff());
                break;
            //
            case RequestDTO.ADD_DEVICE:
                dataUtil.addDevice(req.getGcmDevice());
                break;
            case RequestDTO.ADD_PROJECT_DIARY_RECORD:
                r = dataUtil.addProjectDiaryRecord(req.getProjectDiaryRecord());
                break;
            case RequestDTO.ADD_PROJECT_SITE_TASK:
                r = dataUtil.addProjectSiteTask(req.getProjectSiteTask());
                break;
            case RequestDTO.ADD_PROJECT_SITE_TASK_STATUS:
                r = dataUtil.addProjectSiteTaskStatus(req.getProjectSiteTaskStatus());
                break;
            case RequestDTO.ADD_PROJECT_STATUS_TYPE:
                break;
            //lists
            case RequestDTO.GET_PROJECT_DATA:
                r = listUtil.getProjectData(req.getProjectID());
                break;
            case RequestDTO.GET_COMPANY_STAFF:
                r = listUtil.getCompanyStaff(req.getCompanyID());
                break;
            case RequestDTO.GET_COMPANY_DATA:
                r = listUtil.getCompanyData(req.getCompanyID());
                break;
            case RequestDTO.GET_TASK_STATUS_LIST:
                r = listUtil.getTaskStatusList();
                break;
            case RequestDTO.GET_ANDROID_ERROR_DATA:
                r = listUtil.getAndroidErrorList(req.getDay());
                break;
            case RequestDTO.LOGIN:
                r = dataUtil.login(req.getGcmDevice(),
                        req.getEmail(), req.getPin(),
                        req.getLoginType(), listUtil);
                break;
        }
        if (r.getStatusCode() == null) {
            r.setStatusCode(0);
        }
        return r;
    }
}
