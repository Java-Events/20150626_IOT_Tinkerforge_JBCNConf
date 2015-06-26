package org.rapidpm.demo.v003;

import com.tinkerforge.*;
import org.rapidpm.demo.WaitForQ;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by svenruppert on 26.06.15.
 */
public class Main {


  public static void main(String[] args) {

    final Map<String, Device> deviceMap = new HashMap<>();
    final IPConnection ipConnection = new IPConnection();

    ipConnection.addEnumerateListener(new IPConnection.EnumerateListener() {
      @Override
      public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
                            short[] firmwareVersion, int deviceIdentifier, short enumerationType) {
        System.out.println("uid = " + uid);
        System.out.println("deviceIdentifier = " + deviceIdentifier);

        if(deviceIdentifier == BrickletAmbientLight.DEVICE_IDENTIFIER){
          final BrickletAmbientLight light = new BrickletAmbientLight(uid, ipConnection);
          deviceMap.put("light", light);
          light.addIlluminanceListener(illuminance -> System.out.println("illuminance = " + illuminance));
          try {
            light.setIlluminanceCallbackPeriod(500L);
          } catch (TimeoutException | NotConnectedException e) {
            e.printStackTrace();
          }
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
