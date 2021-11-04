package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;

import java.util.List;

public interface TransfersDAO {
    public List<Transfers> getPastTransfersFromUserId(int accountId);
    public List<Transfers> getTransfersFromUserId(int accountId);
    public List<Transfers> getCurrentTransfersFromUserId(int accountId);
}
