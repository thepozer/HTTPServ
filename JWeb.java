
import pozer.Server.HTTP.*;

public class JWeb {
  // Fonction principale
	public static void main (String args[]) {
    HTTPServConfig cfg = new HTTPServConfig ("./config");
		new HTTPServ(cfg);
	}
}
