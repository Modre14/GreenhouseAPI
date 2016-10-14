package RS485Com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import gnu.io.*;

public class RS485Client {
	public static void main(String[] args) {
		ApplicationServerListener applicationServerListener = new ApplicationServerListener();
		if(applicationServerListener.initClient()){
			Thread t2 = new Thread(applicationServerListener);
			t2.start();
		}
		else{
			System.out.println("Error in App server");
		}
			
	}
}

/**
 * 
 * @author jc
 */
class ApplicationServerListener implements Runnable, SerialPortEventListener {

	static CommPortIdentifier portId;
	static CommPortIdentifier saveportId;
	static Enumeration portList;
	InputStream inputStream;
	SerialPort serialPort;
	Thread readThread;

	//static String messageString = "r\r";
	static OutputStream outputStream = null;
	static boolean outputBufferEmptyFlag = false;
	
	boolean isTerminated = false;
		

	ApplicationServerListener() {
		System.out.println("ApplicationServerListener is starting");
	}

	public boolean initClient() {
		if (!initSerialPort())
			return false;
		
		return true;
	}
	
	@Override
	public void run() {
		System.out.println("ApplicationServerListener is in running state");
		int count = 0;
		while(count < 10){
			try {
				String messageString = "t";
				writetoport(messageString);
				Thread.sleep(1000);
				messageString = "s";
				writetoport(messageString);
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count++;
		}
	}
	

	private boolean initSerialPort() {
		boolean result = true;
		boolean portFound = false;
		String defaultPort = "COM1";

		System.out.println("Start of initSerialPort");
		String osname = System.getProperty("os.name", "").toLowerCase();
		if (osname.startsWith("windows")) {
			defaultPort = "COM11";
			System.out.println(defaultPort);
		} else if (osname.startsWith("linux")) {
			defaultPort = "/dev/ttyS0";
		} else {
			System.out.println("Sorry, your operating system is not supported");
			return false;
		}

		System.out.println("Set default port to " + defaultPort);

		// parse ports and if the default port is found, initialized the reader
		portList = CommPortIdentifier.getPortIdentifiers();
		System.out.println("Set default port to " + defaultPort);

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(defaultPort)) {
					System.out.println("Found port: " + defaultPort);
					portFound = true;
					break;
				}
			}

		}

		if (!portFound) {
			System.out.println("port " + defaultPort + " not found.");
			return false;
		}

		// initalize serial port
		try {
			serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
		} catch (PortInUseException e) {
			System.out.print("port in used");

			return false;
		}

		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
			return false;
		}

		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			return false;
		}

		// activate the DATA_AVAILABLE notifier
		serialPort.notifyOnDataAvailable(true);

		
		try {
			// set port parameters
			serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			return false;
		}
		
		
	    try {
	         // get the outputstream
	         outputStream = serialPort.getOutputStream();
	      } catch (IOException e) { return false;}

	      try {
	         // activate the OUTPUT_BUFFER_EMPTY notifier
	          serialPort.notifyOnOutputEmpty(true);
	      } catch (Exception e) {
	         System.out.println("Error setting event notification");
	         System.out.println(e.toString());
	        return false;
	      }
			
		
		return result;
	}

	public void writetoport(String messageString) {
	      System.out.println("Writing \""+messageString+"\" to "+serialPort.getName());
	      try {
	         // write string to serial port
	         outputStream.write(messageString.getBytes());
	      } catch (IOException e) {}
	   }

	public void serialEvent(SerialPortEvent event) {

		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[16];
			try {
				// read data
				int numBytes = 0;
				numBytes = inputStream.read(readBuffer, 0, 16);

				// print data
				String result = new String(readBuffer);
				System.out.println("Read s: "+ numBytes+"_"+result.substring(0, numBytes));
			} catch (IOException e) {
			}
			break;
		}

	}
}
