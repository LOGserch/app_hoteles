package fes.aragon.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import fes.aragon.interfaz.IBaseDatos;
import fes.aragon.modelo.Gerente;
import fes.aragon.modelo.Habitacion;
import fes.aragon.modelo.Hotel;

public class HotelesImp<E> implements IBaseDatos<E> {
	Integer id=1;
	/*
	 * clase HotelesImpl para la implementacion
	 * de los datos obtenidos en la app en la
	 * base de datos
	 * */
	public HotelesImp() {
	}

	@Override
	public ArrayList<E> consulta() throws Exception{
		String query = "select * from hoteles a,gerentes b where a.id_gre=b.id_gre";
		ArrayList<E> datos = new ArrayList<>();
		Statement solicitud = Conexion.getInstancia().getCnn().createStatement();
		ResultSet resultado = solicitud.executeQuery(query);
		if (!resultado.next()) {
			System.out.println("Sin datos");
		} else {
			do {
				Hotel ht = new Hotel();
				ht.setId(resultado.getInt(1));
				ht.setNombre(resultado.getString(2));
				ht.setDireccion(resultado.getString(3));
				ht.setCorreo(resultado.getString(4));
				ht.setTelefono(resultado.getString(5));
				Gerente gr = new Gerente();
				gr.setId(resultado.getInt(6));
				gr.setNombre(resultado.getString(8));
				gr.setApellidoPaterno(resultado.getString(9));
				gr.setApellidoMaterno(resultado.getString(10));
				gr.setRfc(resultado.getString(11));
				gr.setCorreo(resultado.getString(12));
				gr.setTelefono(resultado.getString(13));
				ht.setGerente(gr);
				// buscar habitaciones de cada hotel
				try {
					HabitacionesImpl<Habitacion> cc = new HabitacionesImpl<>();
					for (Habitacion e : cc.buscarIdHotel(ht.getId())) {
						ht.getHabitaciones().add(e);
					}					
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}

				datos.add((E) ht);
			} while (resultado.next());
		}
		solicitud.close();
		resultado.close();
		return datos;
	}

	@Override
	public ArrayList<E> buscar(String patron) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void insertar(E obj) throws Exception {
		// TODO Auto-generated method stub
		String query="insert into hoteles(nombre,direccion,correo,telefono,id_gre) "
				+ "values(?,?,?,?,?)";
		PreparedStatement insercion=Conexion.getInstancia().getCnn().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		Hotel dato=(Hotel)obj;
		
		insercion.setString(1,dato.getNombre());
		insercion.setString(2,dato.getDireccion());
		insercion.setString(3,dato.getCorreo());
		insercion.setString(4,dato.getTelefono());
		insercion.setInt(5, dato.getGerente().getId());
		insercion.executeUpdate();		
		ResultSet res = insercion.getGeneratedKeys();
		
		if(res.next()) {
			dato.setId(res.getInt(1));
		}
		res.close();
		insercion.close();
	}

	@Override
	public void modificar(E obj) throws Exception {
		// TODO Auto-generated method stub
		String query="update hoteles set nombre=?,direccion=? "
				+ ",correo=?,telefono=? where"
				+ " id_htl=? ";
		PreparedStatement solicitud=Conexion.getInstancia().getCnn().prepareStatement(query);
		Hotel dato=(Hotel)obj;		
		solicitud.setString(1,dato.getNombre());
		solicitud.setString(2,dato.getDireccion());
		solicitud.setString(3,dato.getCorreo());
		solicitud.setString(4,dato.getTelefono());
		solicitud.setInt(5, dato.getId());
		solicitud.executeUpdate();		
		solicitud.close();

	}

	@Override
	public E consulta(Integer id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminar(Integer id) throws Exception {
		// TODO Auto-generated method stub
		String query="delete from hoteles "
				+ "where  id_htl = ? ";
		PreparedStatement solicitud=Conexion.getInstancia().getCnn().prepareStatement(query);
		solicitud.setInt(1, id);
		solicitud.executeUpdate();		
		solicitud.close();
	}

	@Override
	public void cerrar() throws Exception {
		// TODO Auto-generated method stub
		Conexion.getInstancia().cerrar();
	}

	@Override
	public void eliminarProc(Integer id) throws Exception {
		// TODO Auto-generated method stub
		String query = "{call eliminar(?)}";
		PreparedStatement solicitud=Conexion.getInstancia().getCnn().prepareStatement(query);
		solicitud.setInt(1, id);
		solicitud.executeUpdate();		
		solicitud.close();
	}

}
