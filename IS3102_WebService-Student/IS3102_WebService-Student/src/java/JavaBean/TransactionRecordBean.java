/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaBean;

import Entity.TransactionRecord;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Janie
 */
public class TransactionRecordBean {

    //create transaction record using the member Id and amount paid :)
    
    //this will be initialized in the ECommerceFacadeRest. 
    //however, values are located in the value bean 
        
    public TransactionRecord createTransRecord(Long memberID, double amountPaid) {
        TransactionRecord tr = new TransactionRecord();
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/islandfurniture-it07?zeroDateTimeBehavior=convertToNull&user=root&password=12345");
            String stmt = "INSERT INTO salesrecordentity (member_Id, amountDue, amountPaid, STORE_ID) VALUES (?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, memberID);
            ps.setDouble(2, amountPaid);
            ps.setDouble(3, amountPaid);
            ps.setNull(4, java.sql.Types.BIGINT);
            int rowsCreated = ps.executeUpdate();
            long trID = 0;

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trID = generatedKeys.getInt(1);
                    tr.setMemberId(memberID);
                    tr.setDue(amountPaid);
                    tr.setPayment(amountPaid);
                    tr.setMemberId(trID);
                   
                    
                } 
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tr;
    }

}
