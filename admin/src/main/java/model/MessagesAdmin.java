package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the MESSAGES_ADMIN database table.
 * 
 */
@Entity
@Table(name="messages_admin")
@NamedQuery(name="MessagesAdmin.findAll", query="SELECT m FROM MessagesAdmin m")
public class MessagesAdmin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MESSAGE_ID")
	private int messageId;

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

	@Column(name="MESSAGE_USER_ID")
	private int messageUserId;

	//bi-directional many-to-one association to Admin
	@ManyToOne
	@JoinColumn(name="MESSAGE_ADMIN_ID")
	private Admin admin;

	public MessagesAdmin() {
	}

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
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

	public int getMessageUserId() {
		return this.messageUserId;
	}

	public void setMessageUserId(int messageUserId) {
		this.messageUserId = messageUserId;
	}

	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

}