/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.monitor.util;

import com.boha.monitor.dto.transfer.ResponseDTO;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author aubreyM
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ListUtil {

    @PersistenceContext
    EntityManager em;
    
    public ResponseDTO getProjectData(Integer projectID) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        
        try {
            
        } catch (Exception e) {
            
            throw new DataException("Failed");
        }
        
        
        
        return resp;
    }
    public ResponseDTO getCompanyData(Integer companyID) throws DataException {
        ResponseDTO resp = new ResponseDTO();
        
        try {
            
        } catch (Exception e) {
            
            throw new DataException("Failed");
        }
        
        
        
        return resp;
    }
}
