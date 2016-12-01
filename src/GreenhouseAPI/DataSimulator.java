package GreenhouseAPI;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;
import java.util.function.BinaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christian
 */
public class DataSimulator {

    private AtomicReference<Double> kelvinAtomic;
    private Random generator = new Random();
    double temp;

//    double valD;
    public DataSimulator(double temp) {
        this.temp = temp;
    }

    public void simulateTemperature(int kelvin) {
        

        new Thread(() -> {
            
            while (temp < (kelvin+0.0)) {
                double valD =0;
                int r = generator.nextInt(5)+1;
                

                int neg = generator.nextInt(2);
                System.out.println("                   "+neg);
                
                switch(neg){
                    case 0:
                        valD = r / 10.0;
                        break;
                    case 1:
                        valD = (r / 10.0)*(-1);
                        break;
                }
                temp= temp+valD;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(DataSimulator.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.println(temp + "");
            }
        }).start();
//                    double val = kelvinAtomic.get() + valD;
//                    System.out.println(val + " 1");
//                    double temp = 10.0;
//                    double tempa = temp + valD;
//                    kelvinAtomic.getAndSet(tempa);
//                    System.out.println(kelvinAtomic + " 2");
                    
    }

    public static void main(String[] args) {
        DataSimulator ds = new DataSimulator(10.0);
        ds.simulateTemperature(20);
    }
}
