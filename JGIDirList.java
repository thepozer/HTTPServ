// Package
package pozer.Server.HTTP.JGI;

// Importation
import java.util.*;
import java.io.*;

// Classe Principale
public class JGIDirList extends JavaGI {
  String text = null;

  public JGIDirList () {}

  // Founiture des données de travail
  public void setData(Properties p){
		super.setData(p);
    genText();
  }

  // Récupération du résultat
  public String getMimeType(){
		return "text/html";
	}

  public int getSizeData() {
  	if (text == null)
    	return 0;
		return text.length();
  }

  public String getData(){
    return text;
  }

  private void genText() {
    StringBuffer tempo = new StringBuffer ("<HTML>\n<HEAD>\n<TITLE>Directory : " + Prop.get("Directory") + "</TITLE>\n</HEAD>\n<BODY>\n");
    try {
      // Récupération des noms de fichiers
      String Dir = (String)Prop.get("Directory");
      String sep = System.getProperty("file.separator");
	    File RepBase = new File (Prop.get("HomePath") + sep + Dir);
  		String fics[] = RepBase.list();
			File fils[] = new File [fics.length];
      String noms[] = new String [fics.length] ;
      int pos  = 0;
			for (int i = 0; i < fics.length; i++) {
				pos = fics[i].lastIndexOf(sep);
        fils[i] = new File((new File(RepBase, fics[i])).getCanonicalPath());
        if (pos > -1)
	        noms[i] = fics[i].substring(pos+1);
				else
        	noms[i] = fics[i];
      }
      // Generation du lien de repertoire parent
      System.out.println("Coucou : " + Dir);
      String back = null;
  		if (!Dir.endsWith("/") && !Dir.equals("/"))
				Dir = Dir + "/";
      if (Dir.equals("/"))
        back = "/";
      else {
      	pos = (Dir.substring(0, Dir.length() - 2)).lastIndexOf("/");
        back = Dir.substring(0, pos+1);
      }
			// Génération de la page HTML
			tempo.append("<H1>Directory : " + Prop.get("Directory") + "</H1>\n<HR>\n");
      tempo.append("<P><A HREF=\"" + back + "\">Parent Directory</A></P>\n<P>\n");
      tempo.append("<TABLE BORDER=0>\n<TR VALIGN=\"MIDDLE\" ALIGN=\"CENTER\"><TH>FileName :<TH>Length :<TH>Type :</TR>\n");
			for (int j = 0; j < noms.length; j++) {
	      tempo.append("<TR VALIGN=\"MIDDLE\" ALIGN=\"CENTER\"><TD><A HREF=\"" + Dir);
        if (fils[j].isDirectory())
          tempo.append(noms[j] + "/\">" + noms[j] + "/");
        else
          tempo.append(noms[j] + "\">" + noms[j]);
        tempo.append("</A><TD ALIGN=\"RIGHT\">" + fils[j].length() + "<TD>");
        if (!fils[j].isDirectory())
          tempo.append("File");
        else
          tempo.append("Directory");
        tempo.append("</TR>\n");
      }
			tempo.append("</TABLE></P>\n</BODY>\n</HTML>\n");
    } catch (Exception e) {
     	e.printStackTrace();
    }
    text = tempo.toString();
  }
}