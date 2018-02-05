    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author Janie
 */

 // this is the value bean for transaction record
   
public class TransactionRecord {
    
    private Long memberId;
    private double payment;
     private double due;
     private Long transactionId;
     
     public Long getTransactionId() {
           return transactionId;
       }
     
       public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
        public void setPayment(double amountPaid) {
        this.payment = amountPaid;
    }
       public void setDue(double amountDue) {
        this.due = amountDue;
    
       
       }

       
       
       
    
}
