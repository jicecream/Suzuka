/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 *
 * @author Janie
 */
public class LineItemTransactionRecord {
    long salesRecordID;
long lineItemId;
 

public int createSalesRecordLineItem(Long salesRecordID, Long lineItemId) {
        
   this.salesRecordID= salesRecordID;
   this.lineItemId = lineItemId;
   
           try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");
            String stmt = "insert into salesrecordentity_lineitementity (SalesRecordEntity_ID, itemsPurchased_ID) VALUES(?,?)";
            
            PreparedStatement ps = conn.prepareStatement(stmt);
            
            ps = conn.prepareStatement(stmt);

            ps.setLong(1, lineItemId);
            
            ps.setLong(2, salesRecordID);
            
            
            int rowsCreated = ps.executeUpdate();
            
                    
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return 1;
    }
    
}
