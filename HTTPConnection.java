// Package
package pozer.Server.HTTP;

// Importations
import pozer.Server.HTTP.JGI.*;
import java.io.*;
import java.net.*;
import java.util.*;

// Classe HTTPConnection
public class HTTPConnection implements Runnable {

	private Socket skt = null;
	private BufferedReader Lect = null;
	private PrintWriter	Ecrit = null;
	private String HomePath = null;
  private HTTPServConfig Config = null;

	public HTTPConnection (Socket theSkt, HTTPServConfig cfg) {
		skt = theSkt;
		Config = cfg;
		// Connection des flux d'entree/sortie
		try {
	    HomePath = (new File(cfg.getProperty("HomePath"))).getCanonicalPath();
			Lect = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			Ecrit = new PrintWriter(skt.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {
		String str = null;
		boolean fin = false;
    HTTPRequest req = null;

		try {
      // Récupération de la requète HTTP
			req = new HTTPRequest(Lect);
      System.out.println(req);
      // Test de la version de la requète
      if (req.getVersion() == 1.0f || req.getVersion() == 1.1f)
        // Test s'il s'agit d'une commande normale
        if (req.getType().equals("GET") || req.getType().equals("HEAD"))
          doGetHeadRequest(req);
      // Déconnection
			Lect.close();
			Ecrit.close();
			skt.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


  private void doGetHeadRequest(HTTPRequest req) {
    try {
      char tab [] = new char [4096];
      int taille = -1;
      // Generation du nom physique du fichier
      File f = new File ((new File (HomePath, req.getURI())).getCanonicalPath());
      // Test si l'on sort d'arborecence
      if (!f.getCanonicalPath().startsWith(HomePath))
        // Si oui envois du fichier d'erreur
       	errorNum(req, 400);
      else {
        // Test s'il s'agit d'un répertoire pour rajouter le nom de fichier par defaut
        if (f.isDirectory())
          f = new File(f, System.getProperty("file.separator") + Config.getProperty("DefaultFile"));
        // S'il n'existe pas, liste le répertoire
        if (!f.exists())
    			doDirRequest(req);
        else {
          // Sinon envois du fichier demandé
        	sendHeaderResponce(req.getVersion(),200 ,(int)f.length(), Config.getMimeType(getExtension(f)));
          if (req.getType().equals("GET"))
    		  	if (f != null) {
	            FileReader fr = new FileReader (f);
  	          while ((taille = fr.read(tab, 0, 4096)) > -1)
    	          Ecrit.write(tab, 0, taille);
        	    Ecrit.flush();
      	      fr.close();
            }
        }
      }
    } catch(FileNotFoundException e) {
      // On ne fait rien ... :)
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void doDirRequest(HTTPRequest req) {
		try {
	    Properties p = new Properties();
  	  JGIDirList dl = new JGIDirList();
			p.put("HomePath", HomePath);
	    p.put("Directory",req.getURI());
  	  dl.setData(p);

      // Envois de l'entete
      sendHeaderResponce(req.getVersion(),200 ,dl.getSizeData(), dl.getMimeType());
      if (req.getType().equals("GET")) {
  			// Envois du corps
        Ecrit.print(dl.getData());
        Ecrit.flush();
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void sendHeaderResponce(float ver, int status, int len, String TypMime) {
    try {
      Ecrit.println("HTTP/" + Float.toString(ver) + " " + Integer.toString(status) + " Retour en cours");
 	    Ecrit.println("Server: " + Config.getProperty("ServerVersion"));
   	  Ecrit.println("Allow: GET, HEAD, POST");
      Ecrit.println("Content-Length: " + len);
     	Ecrit.println("Content-Type: " + TypMime);
 	    Ecrit.println("");
   	  Ecrit.flush();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void errorNum(HTTPRequest req, int num) {

  	String str = null;

		try {
    	str = "Erreur"+(new Integer(num)).toString().trim();
      File f = new File (Config.getProperty(str));
      char tab [] = new char [4096];
      int taille = -1;
      sendHeaderResponce(req.getVersion(), num, (int)f.length(), "text/html");

      FileReader fr = new FileReader (f);
      while ((taille = fr.read(tab, 0, 4096)) > -1)
        Ecrit.write(tab, 0, taille);
     	Ecrit.flush();
      fr.close();
    } catch (Exception e) {
			e.printStackTrace();
    }

  }

  private String getExtension(File f) {
    String str = f.getName();
    int pos = -1;
    try {
      pos = str.lastIndexOf('.');
      if (pos >= 0)
        return str.substring(pos+1);
      else
        return Config.getProperty("DefaultExt");
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
