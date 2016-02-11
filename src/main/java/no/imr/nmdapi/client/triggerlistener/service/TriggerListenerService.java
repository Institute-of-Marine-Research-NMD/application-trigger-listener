package no.imr.nmdapi.client.triggerlistener.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.dbcp.DelegatingConnection;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author sjurl
 */
@Service("triggerListener")
public class TriggerListenerService {
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TriggerListenerService.class);
    
    @Autowired
    private DataSource dataSource;
    
    private Connection conn;

    /**
     * Method that collects messages from the postgresql data queue and returns
     * them
     *
     * @return
     * @throws SQLException
     */
    public String listen() throws SQLException {
        
        StringBuilder messages = new StringBuilder();
        LOGGER.info("Checking for new messages");
        try {
            if (conn == null) {
                conn = dataSource.getConnection();
                Statement listenStatement = conn.createStatement();
                listenStatement.execute("LISTEN exporttrigger");
                listenStatement.close();
            }
            PGConnection pgConn = (PGConnection) ((DelegatingConnection) conn).getInnermostDelegate();
            
            Statement selectStatement = conn.createStatement();
            ResultSet rs = selectStatement.executeQuery("SELECT 1");
            rs.close();
            selectStatement.close();
            
            PGNotification[] notifications = pgConn.getNotifications();
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
        } catch (SQLException ex) {
            LOGGER.error("Something wrong with the connection", ex);
            conn = null;
        }
        return messages.toString();
    }
}
