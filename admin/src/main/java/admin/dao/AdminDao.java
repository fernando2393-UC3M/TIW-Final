package admin.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import model.Admin;

public interface AdminDao extends CrudRepository<Admin, Integer>{

	public List<Admin> findByAdminEmail(String email);
	public List<Admin> findByAdminEmailAndAdminPassword(String email, String password);
	public List<Admin> findAll();
}

