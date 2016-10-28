/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

/**
 *
 * @author Morten
 */
public class MES {

    public static void main(String[] args) {
        MES m = new MES();
        m.ERP();
    }
    
    
    private void ERP(){
        ERP_Connect obj2 = new ERP_Connect();
        obj2.getConnection();
        obj2.getDataFromERP();
    }

}
