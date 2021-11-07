package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Accounts;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransfersDAO implements TransfersDAO{
    private AccountsDAO accountsDAO;

    private JdbcTemplate jdbcTemplate;

    public JdbcTransfersDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfers> getTransfersFromAccountFromId(int accountId) {
        List<Transfers> transfersList = new ArrayList<Transfers>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "WHERE account_from = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId);
        while (rows.next()) {
            transfersList.add(mapRowToTransfers(rows));
        }
        return transfersList;
    }

    @Override
    public List<Transfers> getTransfersFromAccountToId(int accountId) {
        List<Transfers> transfersList = new ArrayList<Transfers>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "WHERE account_to = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId);
        while (rows.next()) {
            transfersList.add(mapRowToTransfers(rows));
        }
        return transfersList;
    }

    @Override
    public List<Transfers> getAllTransfers() {
        List<Transfers> transfersList = new ArrayList<Transfers>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers ";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        while (rows.next()) {
            transfersList.add(mapRowToTransfers(rows));
        }
        return transfersList;
    }

    //TODO: non-mvp
//    @Override
//    public List<Transfers> getPastTransfersFromUserId(int accountId) {
//        List<Transfers> transfersList = new ArrayList<Transfers>();
//        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
//                "WHERE (account_from = ? OR account_to = ?) AND transfer_status_id IN(2, 3)";
//        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
//        while (rows.next()) {
//            transfersList.add(mapRowToTransfers(rows));
//        }
//        return transfersList;
//    }

    //TODO: non-mvp
//    @Override
//    public List<Transfers> getCurrentTransfersFromUserId(int accountId) {
//        List<Transfers> transfersList = new ArrayList<Transfers>();
//        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
//                "WHERE (account_from = ? OR account_to = ?) AND transfer_status_id = 1";
//        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
//        while (rows.next()) {
//            transfersList.add(mapRowToTransfers(rows));
//        }
//        return transfersList;
//    }

    @Override
    public Transfers sendMoneyTransferCreation(Transfers transfers) {
        String sql = "INSERT INTO transfers VALUES (default, ?, ? , ?, ?, ?) RETURNING transfer_id";
        int transferId = jdbcTemplate.queryForObject(sql, int.class, transfers.getTypeId(), transfers.getStatusId(),
                transfers.getAccountFrom(), transfers.getAccountTo(), transfers.getAmountTransfer());
        transfers.setTransferId(transferId);
        return transfers;
    }

    private Transfers mapRowToTransfers(SqlRowSet row) {
        Transfers transfer = new Transfers();
        transfer.setAccountFrom(row.getInt("account_from"));
        transfer.setAccountTo(row.getInt("account_to"));
        transfer.setAmountTransfer(row.getDouble("amount"));
        transfer.setStatusId(row.getInt("transfer_status_id"));
        transfer.setTransferId(row.getInt("transfer_id"));
        transfer.setTypeId(row.getInt("transfer_type_id"));
        if(transfer.getStatusId() == 1) {
            transfer.setStatusDescription("Pending");
        } else if(transfer.getStatusId() == 2) {
            transfer.setStatusDescription("Approved");
        } else {
            transfer.setStatusDescription("Rejected");
        }
        if(transfer.getTypeId() == 1) {
            transfer.setTypeDescription("Request");
        } else {
            transfer.setTypeDescription("Send");
        }
        return transfer;
    }

}
