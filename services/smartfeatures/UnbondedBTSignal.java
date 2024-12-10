package services.smartfeatures;

import java.net.ConnectException;

public interface UnbondedBTSignal {
  void BTbroadcast() throws ConnectException;
}