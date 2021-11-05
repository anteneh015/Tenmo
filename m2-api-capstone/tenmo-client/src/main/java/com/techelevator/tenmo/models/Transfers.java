package com.techelevator.tenmo.models;

public class Transfers {
    private int transferId;
    private int typeId;
    private int statusId;
    private int accountTo;
    private int accountFrom;
    private double amountTransfer;
    private String typeDescription;
    private String statusDescription;

    public Transfers(int typeId, int statusId, int accountTo, int accountFrom, double amountTransfer){
        this.typeId = typeId;
        this.statusId = statusId;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.amountTransfer = amountTransfer;
        
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public double getAmountTransfer() {
        return amountTransfer;
    }

    public void setAmountTransfer(double amountTransfer) {
        this.amountTransfer = amountTransfer;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
