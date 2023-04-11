/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.dao.empleado;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.neodatis.odb.*;
import org.neodatis.odb.core.oid.OIDFactory;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.IValuesQuery;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

import modelo.Empleado;
import modelo.dao.AbstractGenericDao;
import modelo.exceptions.InstanceNotFoundException;
import util.ConnectionFactory;
import util.Utils;

/**
 *
 * @author mfernandez
 */
public class EmpleadoNeoDatisDao 
extends AbstractGenericDao<Empleado> 
implements IEmpleadoDao {

	private ODB dataSource;

	public EmpleadoNeoDatisDao() {
		this.dataSource = ConnectionFactory.getConnection();
	}

	@Override
	public long create(Empleado entity) {
		OID oid = null;
		long oidlong =-1;
		try {
			
			oid = this.dataSource.store(entity);
			this.dataSource.commit();

		} catch (Exception ex) {
			
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
			this.dataSource.rollback();
			oid = null;
		}
		if(oid!=null) {
			oidlong= oid.getObjectId();
		}
		return oidlong;
	}

	@Override
	public Empleado read(long id) throws InstanceNotFoundException {
		Empleado empleado = null;
		try {
			OID oid = OIDFactory.buildObjectOID(id);
			empleado = (Empleado) this.dataSource.getObjectFromId(oid);
		} catch (ODBRuntimeException ex) {
		
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
//Suponemos que no lo encuentra
			throw new InstanceNotFoundException(id, getEntityClass());
		}
		catch(Exception ex) {
			
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

		}
		return empleado;
	}

	@Override
	public boolean update(Empleado entity) {
		boolean exito = false;
		try {
			this.dataSource.store(entity);
			this.dataSource.commit();
			exito = true;
		} catch (Exception ex) {			
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
			this.dataSource.rollback();
			

		}
		return exito;																	// nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}







	

	@Override
	public float findAvgSalary() {
		BigDecimal media =BigDecimal.ZERO;
		ValuesCriteriaQuery valuesCriteriaQuery = new ValuesCriteriaQuery(Empleado.class);
		IValuesQuery ivc = valuesCriteriaQuery.avg("sal");
		Values values = this.dataSource.getValues(ivc);
		while(values.hasNext()) {
			ObjectValues objectValues = (ObjectValues) values.next();			
			media = (BigDecimal) objectValues.getByIndex(0); 
			
		}
		return media.floatValue();
	}

	


	@Override
	public boolean delete(Empleado entity) {
		boolean exito = false;
		try {
			this.dataSource.delete(entity);
			this.dataSource.commit();
			exito = true;
		} catch (Exception ex) {
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
			this.dataSource.rollback();


		}
		return exito;

	}

	@Override
	public List<Empleado> findAll() {
		CriteriaQuery query=new CriteriaQuery(Empleado.class);
		IQuery iQuery=query.orderByAsc("empno");
		Objects<Empleado> empleados= dataSource.getObjects(iQuery);
		return Utils.toList(empleados);
	}

	@Override
	public List<Empleado> findByJob(String job) {
		ICriterion criterio = Where.equal("job", job);
		IQuery query = new CriteriaQuery(Empleado.class, criterio);
		Objects<Empleado> empleados= dataSource.getObjects(query);
		List<Empleado>emp=Utils.toList(empleados);
		return emp;
	}

	@Override
	public boolean exists(Integer empno) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Empleado> findEmployeesByHireDate(Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
