package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class SellerDAOJDBC implements SellerDAO {

	private Connection conn;
	
	public SellerDAOJDBC(Connection conn) {
		this.conn=conn;
	}
	
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?) ",Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected erros! No rows effected");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally{
			DB.closeStatement(st);
		}

		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		// busca um vendedor pelo id recebido
	    PreparedStatement st = null; // objeto usado para montar/executar SQL com parâmetro
	    ResultSet rs = null; // objeto que guarda o resultado da consulta

	    try {
	        st = conn.prepareStatement(
	                "SELECT seller.*, department.Name as DepName " // pega todos os dados do vendedor e o nome do departamento
	                + "FROM seller INNER JOIN department " // junta a tabela seller com a tabela department
	                + "ON seller.DepartmentId = department.Id " // regra da junção: FK do seller = ID do department
	                + "WHERE seller.Id = ? " // filtra para buscar apenas o vendedor com esse id
	        );

	        st.setInt(1, id); // coloca o valor de id no primeiro ? da query

	        rs = st.executeQuery(); // executa o SELECT e guarda o resultado em rs

	        if (rs.next()) { // se encontrou algum registro no resultado
	            Department dep = instantiateDepartment(rs);

	            Seller obj = instantiateSeller(rs, dep);

	            return obj; // retorna o vendedor completo, com departamento
	        }

	        return null; // se não encontrou vendedor com esse id, retorna null

	    } catch (SQLException e) { // se der erro no banco/SQL
	        throw new DbException(e.getMessage()); // lança uma exceção personalizada da aplicação

	    } finally {
	        DB.closeStatement(st); // fecha o PreparedStatement para liberar recurso
	        DB.closeResultSet(rs); // fecha o ResultSet para liberar recurso
	    }
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj =new Seller(); // cria um objeto Seller para guardar os dados do vendedor
        obj.setId(rs.getInt("Id")); // pega o Id do vendedor
        obj.setName(rs.getString("Name")); // pega o Name do vendedor
        obj.setEmail(rs.getString("Email")); // pega o Email do vendedor
        obj.setBaseSalary(rs.getDouble("BaseSalary")); // pega o salário base do vendedor
        obj.setBirthDate(rs.getDate("BirthDate")); // pega a data de nascimento do vendedor
        obj.setDepartment(dep); // associa o departamento criado ao vendedor
        return obj;
		
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null; 
	    ResultSet rs = null; 
	    try {
	        st = conn.prepareStatement(
	        		"SELECT seller.*,department.Name as DepName "
	        		+ "FROM seller INNER JOIN department "
	        		+"ON seller.DepartmentId = department.Id "
	        		+"ORDER BY Name "
	        );

	        rs = st.executeQuery(); 

	        List<Seller> list = new ArrayList<Seller>();
	        Map<Integer, Department> map = new HashMap<>();
	        
	        while (rs.next()) { 
	        	Department dep = map.get(rs.getInt("DepartmentId"));
	        	
	        	if(dep == null) {
	        		dep = instantiateDepartment(rs);
	        		map.put(rs.getInt("DepartmentId"), dep);
	        	}
	        	
	            Seller obj = instantiateSeller(rs, dep);
	            list.add(obj); 
	        }

	        return list; 

	    } catch (SQLException e) { 
	        throw new DbException(e.getMessage()); 

	    } finally {
	        DB.closeStatement(st); 
	        DB.closeResultSet(rs); 
	    }
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		
	    PreparedStatement st = null; 
	    ResultSet rs = null; 
	    try {
	        st = conn.prepareStatement(
	        		"SELECT seller.*,department.Name as DepName "
	        		+ "FROM seller INNER JOIN department "
	        		+"ON seller.DepartmentId = department.Id "
	        		+"WHERE DepartmentId = ? "
	        		+"ORDER BY Name "
	        );

	        st.setInt(1, department.getId()); 
	        rs = st.executeQuery(); 

	        List<Seller> list = new ArrayList<Seller>();
	        Map<Integer, Department> map = new HashMap<>();
	        
	        while (rs.next()) { 
	        	Department dep = map.get(rs.getInt("DepartmentId"));
	        	
	        	if(dep == null) {
	        		dep = instantiateDepartment(rs);
	        		map.put(rs.getInt("DepartmentId"), dep);
	        	}
	        	
	            Seller obj = instantiateSeller(rs, dep);
	            list.add(obj); 
	        }

	        return list; 

	    } catch (SQLException e) { 
	        throw new DbException(e.getMessage()); 

	    } finally {
	        DB.closeStatement(st); 
	        DB.closeResultSet(rs); 
	    }
		
	}
	
}
