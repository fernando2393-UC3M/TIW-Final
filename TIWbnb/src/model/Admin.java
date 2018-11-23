package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ADMIN database table.
 * 
 */
@Entity
@Table(name="ADMIN")
@NamedQuery(name="Admin.findAll", query="SELECT a FROM Admin a")
public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ADMIN_ID")
	private int adminId;

	@Column(name="ADMIN_EMAIL")
	private String adminEmail;

	@Column(name="ADMIN_PASSWORD")
	private String adminPassword;

	//bi-directional many-to-one association to MessagesAdmin
	@OneToMany(mappedBy="admin")
	private List<MessagesAdmin> messagesAdmins;

	public Admin() {
	}

	public int getAdminId() {
		return this.adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getAdminEmail() {
		return this.adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminPassword() {
		return this.adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public List<MessagesAdmin> getMessagesAdmins() {
		return this.messagesAdmins;
	}

	public void setMessagesAdmins(List<MessagesAdmin> messagesAdmins) {
		this.messagesAdmins = messagesAdmins;
	}

	public MessagesAdmin addMessagesAdmin(MessagesAdmin messagesAdmin) {
		getMessagesAdmins().add(messagesAdmin);
		messagesAdmin.setAdmin(this);

		return messagesAdmin;
	}

	public MessagesAdmin removeMessagesAdmin(MessagesAdmin messagesAdmin) {
		getMessagesAdmins().remove(messagesAdmin);
		messagesAdmin.setAdmin(null);

		return messagesAdmin;
	}

}