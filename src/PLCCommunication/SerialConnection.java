/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PLCCommunication;

import static PLCCommunication.IMessage.DIRECTION;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import gnu.io.*;

/**
 *
 * @author Steffen Skov
 */
public class SerialConnection extends PLCConnection implements IMessage
{
    private String port = "COM1";
    private boolean isInit = false;
    private Message mess = null; 
    private ApplicationServerListener respondListener;
    public enum State {Idle, Created, Ready, Sending, Clear_Buffer, Receiving};
    
    static final boolean TEST = true; //Test without PLC
    
    private CommPortIdentifier portId;
    private CommPortIdentifier saveportId;
    private Enumeration portList;
    private InputStream inputStream;
    private SerialPort serialPort;
    private Thread readThread;
    private OutputStream outputStream = null;
    private State comState = State.Idle;
    
    /**
     * Create a serial connection
     * @param cmd PLC command
     * @param data PLC data
     * @param port communication port
     */
    public SerialConnection(byte cmd, byte [] data, String port)
    {
        addMessage(new Message(cmd,data));
        this.port = port;
        respondListener = new ApplicationServerListener();
        comState = State.Created;
        if(initClient())
        {
            isInit = true;
            comState = State.Ready;
        }
        
    }
    
    /**
     * Create a serial connection
     * @param port communication port
     */
    public SerialConnection( String port)
    {
        mess = null;
        this.port = port;
        respondListener = new ApplicationServerListener();
        if(initClient())
        {
            isInit = true;
            comState = State.Created;
        }
      
    }
    
    /**
     * Add a message to be send
     * @param m 
     */
    
    public void addMessage(Message m)
    {
        super.addMessage(m);
        comState = State.Ready;
    }
    
    /**
     * Send a message over a serial connection and get back an answer
     * @return true if communication succed
     */
    public boolean send()
    {   
        if (comState == State.Ready)
        {
            byte [] p = mess.packMessage();
            System.out.println("Send:" + p[0] + "," + p[1] + ","  + p[2] + ".....");
            comState = State.Sending;
            writetoport(p);
            Thread t2 = null;
            if (isInit)
            {
                t2 = new Thread(respondListener, "Respond listener");
                t2.start();
                try
                {
                Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    System.out.println(e.getMessage());
                
                }
            }
            System.out.println("Send ended");
            if (t2 != null) t2.stop();
        }
        else
            return false;
        System.out.println("Send return");
        return true;
    }
    
    private void writetoport(byte [] message) 
    {
          try 
          {
             System.out.print("Writing:");
             outputStream.write(message);
             for (int i = 0; i <message.length;i++)
                System.out.print(","+message[i]);
             System.out.println(";");
          } catch (IOException e) 
          {
              System.out.println(e.getMessage());
          }
    }
    
    private boolean initClient() 
    {
            if (!initSerialPort())
                    return false;
            return true;
    }
    
    /**
     * Scan the computer for present ports
     * @param port the choosen port
     * @return a list om ports etc.
     */
    static public String getPortList(String port)
    {
        CommPortIdentifier portId;
        Enumeration portList;
        boolean portFound = false;
        StringBuilder res = new StringBuilder();
        String osname = System.getProperty("os.name", "").toLowerCase();
        
        if (osname.startsWith("windows") || osname.startsWith("linux")) 
        {
            System.out.println("The operating system is " + osname);
        }
        else 
        {
            return "Sorry, your operating system is not supported";
        }
        portList = CommPortIdentifier.getPortIdentifiers();
        res.append(osname + ", ");
        while (portList.hasMoreElements()) 
        {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println(portId.getName());
            res.append(portId.getName() + " ");
            
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) 
            {
                if (portId.getName().equals(port)) 
                {
                    System.out.println("Found serial port to use: " + port);
                    portFound = true;
                    //break;
                }
            }
        }
        if (!portFound) 
            System.out.println("Port " + port + "not found");
        System.out.println(res);
        return osname + ", " + portList + port;     
    }
    
    private boolean initSerialPort() 
    {
        boolean result = true;
        boolean portFound = false;
        System.out.println("Start of initSerialPort");
        String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.startsWith("windows")) 
        {
                
                System.out.println(port);
        } else if (osname.startsWith("linux")) {
                port = "/dev/ttyS0";
        } else {
                System.out.println("Sorry, your operating system is not supported");
                return false;
        }

        System.out.println("Set default port to " + port);

        // parse ports and if the default port is found, initialized the reader
        portList = CommPortIdentifier.getPortIdentifiers();
        System.out.println("Set default port to " + port + "portlist" + portList);

        while (portList.hasMoreElements()) 
        {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println(portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(port)) 
                {
                        System.out.println("Found port: " + port);
                        portFound = true;
                        break;
                }
            }

        }
        if (!portFound) 
        {
            System.out.println("Port " + port + " not found.");
            return false;
        }
        // initalize serial port
        try
        {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
            inputStream = serialPort.getInputStream();
            serialPort.addEventListener(respondListener);
            serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            outputStream = serialPort.getOutputStream();
            serialPort.notifyOnOutputEmpty(true);
        }
        catch (PortInUseException e) 
        {
            System.out.print("port in used");
            return false;
        }

        catch (TooManyListenersException e) 
        {
            System.out.println(e.getMessage());
            return false;
        }
        catch (UnsupportedCommOperationException e) 
        {
            System.out.println(e.getMessage());
            return false;
        }
        catch (IOException e) 
        {
            System.out.println(e.getMessage());
            return false;
        }
        catch (Exception e) 
        {
            System.out.println(e.toString());
            return false;
        }            
        return result;
    }


/**
* Listen for answer from PLC
* @author Steffen Skov
*/
class ApplicationServerListener implements Runnable, SerialPortEventListener 
{
    boolean outputBufferEmptyFlag = false;
    boolean isTerminated = false;


    ApplicationServerListener() 
    {
            System.out.println("ApplicationServerListener is starting");
    }

    

    @Override
    public void run() 
    {
        System.out.println("Communication state " + comState.name());
        int count = 0;
        while(count < 10 && comState != State.Ready)
        {
            try 
            {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                   e.printStackTrace();
            }
            count++;
            System.out.println(count);
        }
        boolean state = this.isTerminated;
        System.out.println("Thread Ended" + state);
    }
    
    /**
     * Handle events from serial port
     * @param event 
     */
    public void serialEvent(SerialPortEvent event) 
    {

            switch (event.getEventType()) 
            {
                case SerialPortEvent.BI:
                case SerialPortEvent.OE:
                case SerialPortEvent.FE:
                case SerialPortEvent.PE:
                case SerialPortEvent.CD:
                case SerialPortEvent.CTS:
                case SerialPortEvent.DSR:
                case SerialPortEvent.RI:
                    break;
                    
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                    serialPort.notifyOnDataAvailable(true);
                    comState = State.Clear_Buffer;
                    System.out.println("Communication state 1 " + comState.name());
                    System.out.println("All data is send");
                    break;
                case SerialPortEvent.DATA_AVAILABLE:
                        try {
                                // read data
                                int numBytes = 0;
                                System.out.println("Start read");
                                numBytes = inputStream.read(mess.answer, 0, 125);
                                System.out.println("Read "+ numBytes+" :"+ mess.answer[0]+","+ mess.answer[1]+"....");
                                // The message is the send message
                                if ((mess.getResult())[DIRECTION] == TOPLC && comState == State.Clear_Buffer)
                                {
                                    if (TEST == false)
                                        comState = State.Receiving;
                                    else
                                        comState = State.Ready;

                                    System.out.println("Communication state 2 " + comState.name());
                                }
                                else if ((mess.getResult())[DIRECTION] == FROMPLC && comState == State.Receiving)
                                {
                                    if (mess.answerIsValid() == true)
                                    {
                                        comState = State.Ready;
                                        System.out.println("Communication state 3 " + comState.name());
                                    }
                                    else if (TEST == true)
                                    {
                                        comState = State.Ready;
                                        serialPort.close();
                                        System.out.println("Communication state " + comState.name());
                                    }
                                }
                        } 
                        catch (IOException e) 
                        {
                            System.out.println(e.getMessage());
                        }
                        break;
            }
        }
    }
  
}


