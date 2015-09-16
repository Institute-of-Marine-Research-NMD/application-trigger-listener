package no.imr.nmdapi.client.triggerlistener.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbcp.DelegatingConnection;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author sjurl
 */
@Service("triggerListener")
public class TriggerListenerService {

    @Autowired
    private Connection conn;

    public String listen() throws SQLException, InterruptedException {
        StringBuilder messages = new StringBuilder();
        PGConnection pgConn = (PGConnection) ((DelegatingConnection) conn).getInnermostDelegate();

        Statement selectStatement = conn.createStatement();
        ResultSet rs = selectStatement.executeQuery("SELECT 1");
        rs.close();
        selectStatement.close();

        PGNotification notifications[] = pgConn.getNotifications();
        boolean first = true;
        if (notifications != null) {
            for (PGNotification pgNotification : notifications) {
                if (!first) {
                    messages.append(";");
                }
                messages.append(pgNotification.getParameter());
                first = false;
            }
        }
        return messages.toString();
    }
}
