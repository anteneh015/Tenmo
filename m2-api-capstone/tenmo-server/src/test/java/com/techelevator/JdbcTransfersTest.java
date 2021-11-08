package com.techelevator;

import com.techelevator.tenmo.dao.JdbcTransfersDAO;
import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.model.Transfers;
import jdk.jfr.BooleanFlag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

public class JdbcTransfersTest extends DAOIntegrationTest{

    private TransfersDAO transfers;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(getDataSource());
        transfers = new JdbcTransfersDAO(jdbcTemplate);
    }

    @After
    public void rollback() throws SQLException {
        getDataSource().getConnection().rollback();
    }

    @Test
    public void get_transfers_from_account_id_test() {
        List<Transfers> transfersListOne = transfers.getAllTransfers();
        Transfers transfersOne = createTransfer(1,1,2001,2002,10);
        Transfers transfersTwo = createTransfer(2,2,2001,2002,20);
        insertTransfer(transfersOne);
        insertTransfer(transfersTwo);
        List<Transfers> transfersListTwo = transfers.getAllTransfers();
        Assert.assertEquals(transfersListTwo.size(), transfersListOne.size() + 2);
    }

    @Test
    public void get_transfers_from_account_to_test() {
        List<Transfers> transfersListOne = transfers.getTransfersFromAccountToId(2002);
        Transfers transfersOne = createTransfer(1,1,2001,2002,10);
        Transfers transfersTwo = createTransfer(2,2,2001,2002,20);
        insertTransfer(transfersOne);
        insertTransfer(transfersTwo);
        List<Transfers> transfersListTwo = transfers.getTransfersFromAccountToId(2002);
        Assert.assertEquals(transfersListTwo.size(), transfersListOne.size() + 2);
    }

    @Test
    public void get_transfers_from_account_from_test() {
        List<Transfers> transfersListOne = transfers.getTransfersFromAccountFromId(2001);
        Transfers transfersOne = createTransfer(1,1,2001,2002,10);
        Transfers transfersTwo = createTransfer(2,2,2001,2002,20);
        insertTransfer(transfersOne);
        insertTransfer(transfersTwo);
        List<Transfers> transfersListTwo = transfers.getTransfersFromAccountFromId(2001);
        Assert.assertEquals(transfersListTwo.size(), transfersListOne.size() + 2);
    }

    @Test
    public void create_transfer_test() {
        List<Transfers> transfersListOne = transfers.getAllTransfers();
        Transfers transfersOne = createTransfer(1,1,2001,2002,10);
        transfers.sendMoneyTransferCreation(transfersOne);
        List<Transfers> transfersListTwo = transfers.getAllTransfers();
        Assert.assertEquals(transfersListTwo.size(), transfersListOne.size() + 1);
    }

    private Transfers createTransfer(int typeId, int statusId, int accountFrom, int accountTo, double transfer) {
        Transfers transfers = new Transfers();
        transfers.setTypeId(typeId);
        transfers.setStatusId(statusId);
        transfers.setAccountFrom(accountFrom);
        transfers.setAccountTo(accountTo);
        transfers.setAmountTransfer(transfer);
        return transfers;
    }

    private void insertTransfer(Transfers transfers) {
        String url = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING transfer_id";
        Integer transferId = jdbcTemplate.queryForObject(url, Integer.class, transfers.getTypeId(), transfers.getStatusId(), transfers.getAccountFrom(), transfers.getAccountTo(), transfers.getAmountTransfer());
    transfers.setTransferId(transferId);
    }

    private void truncateTransfers() {
        jdbcTemplate.update("TRUNCATE transfers CASCADE");
    }
}
