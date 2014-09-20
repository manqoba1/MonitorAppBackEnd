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
    
    public static ResponseDTO processRequest(RequestDTO req, DataUtil dataUtil)throws DataException {
        ResponseDTO r = new ResponseDTO();
        switch(req.getRequestType()) {
            case RequestDTO.REGISTER_COMPANY:
                r = dataUtil.registerCompany(req.getCompany(), req.getCompanyStaff());
                break;
        }       
        return r;
    }
}
