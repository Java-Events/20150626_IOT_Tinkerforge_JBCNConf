package org.rapidpm.demo.v002;

import com.tinkerforge.*;
import org.rapidpm.demo.WaitForQ;

import java.io.IOException;

/**
 * Created by svenruppert on 25.06.15.
 */
public class Main {

  public static void main(String[] args) {

    final IPConnection ipConnection = new IPConnection();
    ipConnection.addEnumerateListener(new IPConnection.EnumerateListener() {
      @Override
      public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
                            short[] firmwareVersion, int deviceIdentifier, short enumerationType) {
//        System.out.println("connectedUid = " + connectedUid);
        System.out.println("uid = " + uid);
        System.out.println("deviceIdentifier = " + deviceIdentifier);

        if(deviceIdentifier == BrickletAmbientLight.DEVICE_IDENTIFIER){

        }
      }
    });


    try {
      ipConnection.connect("127.0.0.1", 4223);
      ipConnection.enumerate();


      final WaitForQ q = new WaitForQ();
      q.addShutDownAction(() -> {
        try {
          ipConnection.disconnect();
        } catch (NotConnectedException e) {
          e.printStackTrace();
        }
      });
      q.waitForQ();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (AlreadyConnectedException e) {
      e.printStackTrace();
    } catch (NotConnectedException e) {
      e.printStackTrace();
    }


  }

}
