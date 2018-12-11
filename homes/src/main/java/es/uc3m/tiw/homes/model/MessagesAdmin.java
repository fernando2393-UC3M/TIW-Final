package es.uc3m.tiw.homes.model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Date;


/**
 * The persistent class for the MESSAGES_ADMIN database table.
 * 
 */
@Entity
@Table(name="MESSAGES_ADMIN")
@NamedQuery(name="MessagesAdmin.findAll", query="SELECT m FROM MessagesAdmin m")
public class MessagesAdmin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MESSAGE_ID")
	private int messageId;

	@Column(name="MESSAGE_ADMIN_ID")
	private int messageAdminId;

	@Lob
	@Column(name="MESSAGE_CONTENT")
	private String messageContent;

	@Temporal(TemporalType.DATE)
	@Column(name="MESSAGE_DATE")
	private Date messageDate;

	@Column(name="MESSAGE_FROM_ADMIN")
	private byte messageFromAdmin;

	@Column(name="MESSAGE_READ")
	private byte messageRead;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="MESSAGE_USER_ID")
	@JsonBackReference
	private User user;

	public MessagesAdmin() {
	}

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getMessageAdminId() {
		return this.messageAdminId;
	}

	public void setMessageAdminId(int messageAdminId) {
		this.messageAdminId = messageAdminId;
	}

	public String getMessageContent() {
		return this.messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public Date getMessageDate() {
		return this.messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public byte getMessageFromAdmin() {
		return this.messageFromAdmin;
	}

	public void setMessageFromAdmin(byte messageFromAdmin) {
		this.messageFromAdmin = messageFromAdmin;
	}

	public byte getMessageRead() {
		return this.messageRead;
	}

	public void setMessageRead(byte messageRead) {
		this.messageRead = messageRead;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}