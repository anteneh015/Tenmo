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
    public List<Transfers> getTransfersFromUserId(int accountId) {
        List<Transfers> transfersList = new ArrayList<Transfers>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "WHERE account_from = ? OR account_to = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (rows.next()) {
            transfersList.add(mapRowToTransfers(rows));
        }
        return transfersList;
    }

    @Override
    public List<Transfers> getPastTransfersFromUserId(int accountId) {
        List<Transfers> transfersList = new ArrayList<Transfers>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "WHERE (account_from = ? OR account_to = ?) AND transfer_status_id IN(2, 3)";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (rows.next()) {
            transfersList.add(mapRowToTransfers(rows));
        }
        return transfersList;
    }

    @Override
    public List<Transfers> getCurrentTransfersFromUserId(int accountId) {
        List<Transfers> transfersList = new ArrayList<Transfers>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount FROM transfers " +
                "WHERE (account_from = ? OR account_to = ?) AND transfer_status_id = 1";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (rows.next()) {
            transfersList.add(mapRowToTransfers(rows));
        }
        return transfersList;
    }

    @Transactional
    @Override
    public void transferMoney(String usernameFrom, String usernameTo,  double transferAmount) {
        Accounts accountFrom = accountsDAO.getsAccountsByUsername(usernameFrom);
        Accounts accountTo = accountsDAO.getsAccountsByUsername(usernameTo);

            withdrawForTransfer(accountFrom.getBalance(), transferAmount, accountFrom.getUserId());
            depositForTransfer(accountTo.getBalance(), transferAmount, accountTo.getUserId());


    }

    @Override
    public void withdrawForTransfer (double balance, double transferAmount, int accountId) {
        String sql = "UPDATE accounts SET balance = ?  WHERE account_from = ?";
        jdbcTemplate.update(sql, balance - transferAmount, accountId);
    }

    @Override
    public void depositForTransfer (double balance, double transferAmount, int accountId){
        String sql = "UPDATE accounts SET balance = ?  WHERE account_to = ?";
        jdbcTemplate.update(sql, balance + transferAmount, accountId);
    }

    @Override
    public Transfers sendMoneyTransferCreation (int accountTo, int accountFrom, double transferAmount){
       Transfers transfer = new Transfers();
        String sql = "INSERT INTO transfers VALUES (default, ?, ? , ?, ?, ?) RETURNING transfer_id";
        int transferId = jdbcTemplate.queryForObject(sql, int.class, 2, 2, accountFrom, accountTo, transferAmount);
        transfer.setTransferId(transferId);
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);
        transfer.setAmountTransfer(transferAmount);
        transfer.setStatusId(2);
        transfer.setTypeId(2);
        transfer.setTypeDescription("Send");
        transfer.setStatusDescription("Approved");
        return transfer;
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
