// Package
package pozer.Server.HTTP;

// Importation
import java.net.*;
import java.util.*;
import java.io.*;

// Classe principale
public class HTTPServ {

	private ServerSocket srvr = null;
  private HTTPServConfig Config = null;

  public HTTPServ(HTTPServConfig cfg) {
    if (cfg != null) {
      Config = cfg;
  		initServ();
      runServ();
    }
  }

	private void initServ() {
    String adr = Config.getProperty("Adresse");
    int prt = Integer.parseInt(Config.getProperty("Port"));

		try {
			InetAddress iadr = InetAddress.getByName(adr);
			srvr = new ServerSocket(prt, 10, iadr);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Serveur initialise a l'adresse " + adr + " et au port " + prt + ".");
	}

	private void runServ() {
    Socket sckt = null;
    HTTPConnection clnt = null;
    Thread t = null;

     while(true) {
      // Attente d'une connection
      try {
				sckt = srvr.accept();
      } catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
      }
      // Creation de la connection
      clnt = new HTTPConnection(sckt, Config);
      // Création et lancement du Thread
      t = new Thread(clnt);
      t.start();
      // Et on recommance ...
    }
	}
}
