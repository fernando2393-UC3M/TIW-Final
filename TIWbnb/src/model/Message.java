package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the MESSAGES database table.
 * 
 */
@Entity
@Table(name="MESSAGES")
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
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

	@Column(name="MESSAGE_READ")
	private boolean messageRead;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="MESSAGE_SENDER_ID")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="MESSAGE_RECEIVER_ID")
	private User user2;

	public Message() {
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

	public boolean getMessageRead() {
		return this.messageRead;
	}

	public void setMessageRead(boolean messageRead) {
		this.messageRead = messageRead;
	}

	public User getUser1() {
		return this.user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return this.user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

}