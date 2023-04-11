package util;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConnectionFactory {
	private static ODB connection = null;
	private static final String RUTA_FICHERO_CONFIF="src/main/resources/db.properties";
	private static final String FILE_KEY="db.file";


	private ConnectionFactory() {

	}

	public static ODB getConnection() {
		if(connection==null){
			Properties properties=new Properties();
			FileInputStream fis= null;
			try {
				fis = new FileInputStream(RUTA_FICHERO_CONFIF);
				properties.load(fis);
				String fichero= properties.getProperty(FILE_KEY);
				connection= ODBFactory.open(fichero);
			} catch (FileNotFoundException e) {
				System.err.println("Ha ocurrido una excepcion FileNotFoundException");
			} catch (IOException e) {
				System.err.println("Ha ocurrido una excepcion IDE: " +e.getMessage());
			} catch(Exception e){
				System.err.println("Ha ocurrido una excepcion: "+e.getMessage());
			}

		}
		return connection;
	}

	public static void closeConnection() {
		if(connection!=null){
			connection.close();
		}

	}

}
