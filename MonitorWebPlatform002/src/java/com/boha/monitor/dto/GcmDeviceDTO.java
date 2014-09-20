/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.dto;

import com.boha.monitor.data.GcmDevice;


/**
 *
 * @author aubreyM
 */
public class GcmDeviceDTO {

    private int gcmDeviceID;
    private String registrationID;
    private String manufacturer;
    private String model;
    private String product;
    private int messageCount;
    private long dateRegistered;
    private String serialNumber;
    private int companyStaffID;
   
    
    public GcmDeviceDTO(GcmDevice a) {
        gcmDeviceID = a.getGcmDeviceID();
        registrationID = a.getRegistrationID();
        manufacturer = a.getManufacturer();
        model = a.getModel();
        product = a.getProduct();
        messageCount = a.getMessageCount();
        dateRegistered =  a.getDateRegistered().getTime();
        serialNumber = a.getSerialNumber();
        if (a.getCompanyStaff()!= null) {
            companyStaffID = a.getCompanyStaff().getCompanyStaffID();
        }
        
    }

    public int getGcmDeviceID() {
        return gcmDeviceID;
    }

    public void setGcmDeviceID(int gcmDeviceID) {
        this.gcmDeviceID = gcmDeviceID;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getCompanyStaffID() {
        return companyStaffID;
    }

    public void setCompanyStaffID(int companyStaffID) {
        this.companyStaffID = companyStaffID;
    }

}
