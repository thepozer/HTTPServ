// Package
package pozer.Server.HTTP;

// Importation
import java.io.*;
import java.util.*;

// Classe publique
public class HTTPServConfig {
  // Variables privées
  private String CfgPath = null;

  // HomePath
  // Port
  // Adresse
  // DefaultFile
  // DefaultExt
  // DefaultMimeType
  // CfgPath
  // Erreurxxx
  private Properties Prop = new Properties ();
  // Type Mime
  private Properties Mime = new Properties ();
  // ScriptAlias
	private Properties ScriptAlias = new Properties ();
  // CGIEnv
  private String [] tabCGIEnv = null;

  public HTTPServConfig () {
    this (".");
  }

  public HTTPServConfig (String theCfgPath) {
    CfgPath = theCfgPath;
    Prop.put("CfgPath", CfgPath);
    lectureConfig();
  }

  public String getMimeType (String ext) {
    String str = Mime.getProperty(ext);
    if (str != null)
      return str;
    else
      return getProperty("DefaultMimeType");
  }

  public String[] getEnvCGI () {
    return tabCGIEnv;
  }

  public String getProperty(String str) {
    return Prop.getProperty(str);
  }

  public void setProperty(String str, String val) {
    try {
      Prop.put(str, val);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Properties getProperties() {
    return Prop;
  }

  public void lectureConfig () {
    readConfigFile();
    readMimeFile();
  }

//  public void ecritureConfig () {
//
//  }

  public String toString() {
    return CfgPath + "\n" + Prop.toString() + "\n" + Mime.toString() + "\n" + ScriptAlias.toString() + "\n" + tabCGIEnv;
  }

  // HomePath
  // Port
  // Adresse
  // DefaultFile
  // DefaultExt
  // DefaultMimeType
  // CfgPath
  // Erreurxxx
  // + ScriptAlias

  private void readConfigFile () {
		StringBuffer strbuf = null;
 		StringTokenizer st = null;
    String str = null;
    String strlgn = null;
		String Args[] = new String [16]; // 1 commande et 15 arguments MAXXIMUM
		int iArgs = 0;
		boolean fin = false;

    try {
			BufferedReader br = new BufferedReader( new FileReader(new File(CfgPath + System.getProperty("file.separator") + "JWeb.cfg")));
			while ((strlgn = br.readLine()) != null) { // Lecture de la ligne
				// Decoupage de la ligne
       	st = new StringTokenizer(strlgn," \t\"", true);
        fin = false;
        iArgs = 0;
        while (st.hasMoreTokens() && !fin) {
					str = st.nextToken();
					if(str.equals(" ") | str.equals("\t"))
          	;
          else if (str.startsWith("#"))
          	fin = true;
          else if (str.equals("\"")) {
           	strbuf = new StringBuffer();
            do {
							if (st.hasMoreTokens()) {
								str = st.nextToken();
								if (!str.equals("\""))
                	strbuf.append(str);
              } else
              	str = "\"";
						} while(!str.equals("\""));
            Args[iArgs] = strbuf.toString();
            iArgs++;
          } else {
            Args[iArgs] = str;
            iArgs++;
          }
				}
        // Traitement de la ligne
        if (iArgs > 0)
	        if (Args[0].toUpperCase().equals("SCRIPTALIAS")) {
						if (Args[1] != null && Args[2] != null)
    	      	ScriptAlias.put(Args[1], Args[2]);
      	  } else {
        		Prop.put(Args[0], Args[1]);
        	}
      }
    	br.close();
    } catch (Exception e) {
			e.printStackTrace();
    }
  }

  private void readMimeFile (){
    StringTokenizer st = null;
    String strlgn = null;
    String str = null;
    String strmime = null;
    boolean rem = false;

    try {
      BufferedReader br = new BufferedReader( new FileReader(new File(CfgPath + System.getProperty("file.separator") + "mime.types")));
      while ((strlgn = br.readLine()) != null) {
        st = new StringTokenizer(strlgn, " \t", false);
        if (st.hasMoreTokens()) {
          str = st.nextToken();
          if (!str.startsWith("#")) {
            rem = false;
            strmime = str;
            while (!rem && st.hasMoreTokens()) {
              str = st.nextToken();
              if (str.startsWith("#"))
                rem = true;
              else
                Mime.put(str, strmime);
            }
          }
        }
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
