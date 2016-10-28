/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import java.rmi.RemoteException;

/**
 *
 * @author Morten
 */
public class MES {

    public static void main(String[] args) throws RemoteException {
        MES m = new MES();
//        m.ERPConnect();
        m.SCADAConnect();
    }

    private void ERPConnect() {
        ERP_Connect obj2 = new ERP_Connect();
        obj2.getConnection();
        obj2.getDataFromERP();
    }

    private void SCADAConnect() throws RemoteException {
        RMI_Client c = new RMI_Client();
        c.clientConnect();
        c.getInfoFromSCADA();
        c.sendDataToSCADA("potato");
        
    }
    

}
