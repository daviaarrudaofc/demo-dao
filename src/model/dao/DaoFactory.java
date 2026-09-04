package model.dao;

import db.DB;
import model.dao.impl.SellerDAOJDBC;

public class DaoFactory {
	public static SellerDAO craeteSellerDao() {
		return new SellerDAOJDBC(DB.getConnection());//para n expor a implemen
	}
}
