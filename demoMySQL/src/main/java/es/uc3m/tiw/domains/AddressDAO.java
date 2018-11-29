package es.uc3m.tiw.domains;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AddressDAO extends CrudRepository<Address, Long>{
	public List<Address> findByPostcode(int pPostCode);
	public List<Address> findByStreet(String street);
}
