package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	public SellerDaoJDBC(Connection conn) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
	}
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller" +
					"(Name, Email, BirthDate, BaseSalary, DepartmentId)"+ 
					"VALUES" + 
					"(?, ?, ?, ?, ?)",Statement.RETURN_GENERATED_KEYS
					);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthdate().getTime()));
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
			}else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			// TODO: handle exception
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatemnet(st);
		}
		
		
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		// TODO Auto-generated method stub
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					
					"SELECT seller.*, department.Name as DepName " +
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " +
					"WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instatantiateSeller(rs, dep);
				
				return obj;
			}
			return null;
		}catch (SQLException e) {
			// TODO: handle exception
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatemnet(st);
			DB.closeResultSet(rs);
		}
		
	}

	private Seller instatantiateSeller(ResultSet rs, Department dep) throws SQLException {
		// TODO Auto-generated method stub
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthdate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
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
					
					"SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " + 
					"ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					 dep = instantiateDepartment(rs);
					 map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = instatantiateSeller(rs, dep);
				list.add(obj);
				
			}
			return list ;
		}catch (SQLException e) {
			// TODO: handle exception
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatemnet(st);
			DB.closeResultSet(rs);
		}
	}
	@Override
	public List<Seller> findByDepartment(Department department) {
		// TODO Auto-generated method stub
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					
					"SELECT seller.*,department.Name as DepName " + 
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " + 
					"WHERE DepartmentId = ? " + 
					"ORDER BY Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					 dep = instantiateDepartment(rs);
					 map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = instatantiateSeller(rs, dep);
				list.add(obj);
				
			}
			return list ;
		}catch (SQLException e) {
			// TODO: handle exception
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatemnet(st);
			DB.closeResultSet(rs);
		}
		
	}

}
