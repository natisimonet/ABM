import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

public class AppABM {

	public static void main(String[] args) {
		System.out.println("SISTEMA DE PERSONAS (ABM)");
		System.out.println("=========================");

		System.out.println("");

		int opcion = mostrarMenu();
		while (opcion != 0) {

			switch (opcion) {
			case 1:
				alta();
				break;
			case 2:
				modificacion();
				break;
			case 3:
				baja();
				break;
			case 4:
				listado();
				break;
			case 0:

				break;
			default:
				break;
			}

			opcion = mostrarMenu();
		}

		// INCORPORAR MODIFICACIONES EN LA BASE CON JDBC
	}

	private static int mostrarMenu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("MENU OPCIONES: 1 -Alta | 2-Modificación | 3- Baja  | 4- Listado | 0-Salir ");
		int opcion = sc.nextInt();
		return opcion;
	}

	private static void listado() {
		Statement st;
		try {

			Connection conexion = AdminBD.obtenerConexion();
			st = conexion.createStatement();
			ResultSet rs = st.executeQuery("select * from Persona");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(3) + "  " + rs.getDate(4));
				System.out.println("");
			}
			conexion.close();

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void baja() {
		try {
			Scanner scan = new Scanner(System.in);
			Connection conexion = AdminBD.obtenerConexion();
			Statement st = conexion.createStatement();
			ResultSet rs = st.executeQuery("select * from Persona");
			System.out.println("Ingrese ID a borrar");
			int ID2 = scan.nextInt();
			st.executeUpdate("DELETE From Persona Where " + ID2 + "");
			conexion.close();
		} catch (SQLException |

				ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void modificacion() { 
		try {
			Scanner scan = new Scanner(System.in);
			Connection conexion = AdminBD.obtenerConexion();
			Statement st = conexion.createStatement();
			ResultSet rs = st.executeQuery("select * from Persona");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(3) + "  " + rs.getDate(4));
				System.out.println("");
			}
			System.out.println("Si desea modificar 1. nombre, 2. Fecha_Nacimiento 3. ambas");
			int respuesta = scan.nextInt();
		
			switch (respuesta) {
			case 1: 
				System.out.println("Ingrese ID a modificar");
				int ID = scan.nextInt();
				System.out.println("Ingrese campo");
				String nombrecambiado = scan.next();
				st.executeUpdate(
    					"Update Persona SET NOMBRE = '" + nombrecambiado + "' where ID = " + ID + "");
    			conexion.close();
    			break;
			case 2:
				System.out.println("Ingrese ID a modificar");
				ID = scan.nextInt();
				System.out.println("Ingrese fecha de nacimiento YYYY-MM-DD");
				String fechadenac = scan.next();
				SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
				int edad = 0;
				try {
					Date fechaNac = sfd.parse(fechadenac);
					edad = calcularEdad(fechaNac);

				} catch (ParseException e1) {

					e1.printStackTrace();
					st.executeUpdate(
	    					"Update Persona SET Edad = " +edad+", FECHA_NACIMIENTO = '" +fechadenac+ "' where ID = " + ID + "");
	    			conexion.close();
				}
				break;
			case 3:
			System.out.println("Ingrese ID a modificar");
			ID = scan.nextInt();
			System.out.println("Ingrese nuevo Nombre");
			String nombrecambiado2 = scan.next();
			System.out.println("Ingrese fecha de nacimiento YYYY-MM-DD");
			fechadenac = scan.next();
			sfd = new SimpleDateFormat("yyyy-MM-dd");
			edad = 0;
			try {
				Date fechaNac = sfd.parse(fechadenac);
				edad = calcularEdad(fechaNac);

			} catch (ParseException e1) {

				e1.printStackTrace();
				st.executeUpdate(
    					"Update Persona SET NOMBRE = '" + nombrecambiado2 + "', Edad = " +edad+", FECHA_NACIMIENTO = '" +fechadenac+ "' where ID = " + ID + "");
    			conexion.close();
            	
            	
            }
			break;
			default:
				break;
			}			
			} catch (SQLException |

				ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}

	private static void alta() {

		try {
			Connection conexion = AdminBD.obtenerConexion();
			Statement st = conexion.createStatement();

			Scanner scan = new Scanner(System.in);
			System.out.println("Ingrese: Nombre");
			String nombre = scan.next();

			System.out.println("Ingrese: Fecha de Nacimiento en Formato YYYY-MM-DD");
			String fnac = scan.next();

			SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
			int edad = 0;
			try {
				Date fechaNac = sfd.parse(fnac);
				edad = calcularEdad(fechaNac);

			} catch (ParseException e1) {

				e1.printStackTrace();
			}
			st.executeUpdate("INSERT INTO PERSONA (NOMBRE, EDAD, FECHA_NACIMIENTO) VALUES ('" + nombre + "'," + edad
					+ ",'" + fnac + "')");
			conexion.close();

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static int calcularEdad(Date fechaNac) {
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar hoy = new GregorianCalendar();
		gc.setTime(fechaNac);
		int anioActual = hoy.get(Calendar.YEAR);
		int anioNacim = gc.get(Calendar.YEAR);
		int mesActual = hoy.get(Calendar.MONTH);
		int mesNacim = gc.get(Calendar.MONTH);
		int diaActual = hoy.get(Calendar.DATE);
		int diaNacim = gc.get(Calendar.DATE);
		int dif = anioActual - anioNacim;
		if (mesActual < mesNacim) {
			dif = dif - 1;
		} else {
			if (mesActual == mesNacim && diaActual < diaNacim) {
				dif = dif - 1;
			}
		}

		return dif;
	}
}
