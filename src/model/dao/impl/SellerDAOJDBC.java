package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
		// TODO Auto-generated method stub
		
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
	            Department dep = new Department(); // cria um objeto Department para guardar os dados do departamento
	            dep.setId(rs.getInt("DepartmentId")); // pega o DepartmentId vindo da tabela seller
	            dep.setName(rs.getString("DepName")); // pega o nome do departamento usando o apelido DepName

	            Seller obj = new Seller(); // cria um objeto Seller para guardar os dados do vendedor
	            obj.setId(rs.getInt("Id")); // pega o Id do vendedor
	            obj.setName(rs.getString("Name")); // pega o Name do vendedor
	            obj.setEmail(rs.getString("Email")); // pega o Email do vendedor
	            obj.setBaseSalary(rs.getDouble("BaseSalary")); // pega o salário base do vendedor
	            obj.setBirthDate(rs.getDate("BirthDate")); // pega a data de nascimento do vendedor
	            obj.setDepartment(dep); // associa o departamento criado ao vendedor

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

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
