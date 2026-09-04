package model.dao;

import model.dao.impl.SellerDAOJDBC;

public class DaoFactory {
	public static SellerDAO craeteSellerDao() {
		return new SellerDAOJDBC();//para n expor a implemen
	}
}
