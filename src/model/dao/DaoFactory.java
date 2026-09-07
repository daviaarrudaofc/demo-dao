package model.dao;

import db.DB;
import model.dao.impl.SellerDAOJDBC;
import model.dao.impl.DepartmentDAOJDBC;

public class DaoFactory {
	public static SellerDAO craeteSellerDao() {
		return new SellerDAOJDBC(DB.getConnection());//para n expor a implemen
	}
	public static DepartmentDAO createDepartmentDao() {
		return new DepartmentDAOJDBC(DB.getConnection());
	}
}
