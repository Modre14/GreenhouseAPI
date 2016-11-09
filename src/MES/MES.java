/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MES;

import Protocol.Protocol;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Morten
 */
public class MES {

    private List<Protocol> protocolArray;

    public static void main(String[] args) throws RemoteException {
        MES m = new MES();
        m.makeProtocols();
//        m.ERPConnect();
//        m.SCADAConnect();
    }

    private void makeProtocols() {
        protocolArray = new ArrayList<Protocol>();
        Protocol RadiseCherryBelle = new Protocol(0, 0, 0, 0, 0, 0);

        protocolArray.add(RadiseCherryBelle);

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
        c.sendDataToSCADA("Hello from MES");

    }

}
