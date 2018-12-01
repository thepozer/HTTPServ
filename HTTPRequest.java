// Package
package pozer.Server.HTTP;

// Importations
import java.util.*;
import java.io.*;

// Classe HTTPRequest
public class HTTPRequest {
  private String Type = null;
  private String URI = null;
  private Properties CGIArgs = new Properties();
  private float Ver = 0;
  private Properties PrpOpt = new Properties();

  public HTTPRequest (BufferedReader br) {
    String str = null;
    String args = null;
    String nom = null;
    int pos = 0;
    int posd = 0;
    int posf = 0;
    try {
    // Lecture de la commande
      str = br.readLine().trim();
      System.out.println(str);
      posd = str.indexOf(' ');
      Type = str.substring(0, posd).toUpperCase();
      posf = str.lastIndexOf("/1");
      if (posf > 0) {
        Ver = Float.valueOf(str.substring(posf + 1).trim()).floatValue();
        str = str.substring(posd, posf - 4).trim();
      } else {
        Ver = 0.9f;
        str = str.substring(posd).trim();
      }
      // Recuperation de la chaine d'inforamtion pour le CGI
      if ((pos = str.indexOf('?')) > -1) {
        // Decoupage des arguments
        StringTokenizer stcgi = new StringTokenizer(str.substring(pos+1), "+&=", true);
        StringBuffer sbcgi = new StringBuffer();
        str = str.substring(0, pos);
        while (stcgi.hasMoreTokens()) {
          args = stcgi.nextToken();
          if (args.equals("+")) {
            sbcgi.append(" ");
          } else if (args.equals("=")) {
            nom = sbcgi.toString();
            sbcgi = new StringBuffer();
          } else if (args.equals("&")) {
            CGIArgs.put(nom, sbcgi.toString());
            sbcgi = new StringBuffer();
          } else
            sbcgi.append(args);
        }
        CGIArgs.put(nom, sbcgi.toString());
      }
      // Mise en forme de la chaine et suppression des doublons de '/'
      str = tradEscCode(str);
      str.replace('\\', '/');
      StringTokenizer st = new StringTokenizer("/" + str, "/", true);
      StringBuffer sb = new StringBuffer();
      String cour = new String();
      String prec = null;
      while (st.hasMoreTokens()) {
        prec = cour;
        cour = st.nextToken();
        if (!(cour.equals("/") && prec.equals(cour)))
          sb.append(cour);
      }
      URI = sb.toString();
      System.out.println("URI : " + URI);
      // Lecture des options
      while (!(str = br.readLine()).equals("")) {
        pos = str.indexOf(':');
        if (pos > 0)
          PrpOpt.put(str.substring(0, pos).trim(), str.substring(pos+1).trim());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getType () { return Type; }
  public String getURI () { return URI; }
  public float getVersion () { return Ver; }
  public Properties getOptions () { return PrpOpt; }

  public String toString() { return Type + " " + URI + " HTTP/" + Ver + "\n" + CGIArgs; }//+ "\n" + PrpOpt; }

  private String tradEscCode(String strURI) {
    String str = new String (strURI);
    int pos = -1;
    char nbr = 0;
    try {
      while ((pos = str.indexOf('%')) >= 0) {
				nbr = (char)Integer.parseInt(str.substring(pos+1, pos+3), 16);
        str = str.substring(0, pos) + nbr + str.substring(pos+3);
      }
	    return str;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
