package com.project.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "PARA_SWIFT_CODE_NH")
public class SwiftCodeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "swiftcode_seq")
	@SequenceGenerator(name = "swiftcode_seq", sequenceName = "PARA_SWIFT_CODE_NH_SEQ", allocationSize = 1)
	@Column(name = "ID")
	private int ID;
	
	@Column(name = "BANK_TYPE")
	private String BANK_TYPE;
	
	@Column(name = "SWIFT_CODE")
	private String SWIFT_CODE;

    @Column(name="CONNECTED_TO_SWIFT")
    private String CONNECTED_TO_SWIFT;

	@Column(name = "SWIFT_CONNECTION")
	private String SWIFT_CONNECTION;

	@Column(name = "BANK_NAME")
	private String BANK_NAME;

	@Column(name = "BRANCH")
	private String BRANCH;

	@Column(name = "ADDRESS_1")
	private String ADDRESS_1;

	@Column(name = "ADDRESS_2")
	private String ADDRESS_2;

	@Column(name = "ADDRESS_3")
	private String ADDRESS_3;

	@Column(name = "ADDRESS_4")
	private String ADDRESS_4;

	@Column(name = "CITY")
	private String CITY;

	@Column(name = "COUNTRY_CODE")
	private String COUNTRY_CODE;

	@Column(name = "PARA_STATUS")
	private int PARA_STATUS;

	@Column(name = "ACTIVE_STATUS")
	private int ACTIVE_STATUS;

	@Column(name = "JSON_DATA")
	private String JSON_DATA;

	
	
	
	public SwiftCodeEntity(int iD, String bANK_TYPE, String sWIFT_CODE, String cONNECTED_TO_SWIFT,
			String sWIFT_CONNECTION, String bANK_NAME, String bRANCH, String aDDRESS_1, String aDDRESS_2,
			String aDDRESS_3, String aDDRESS_4, String cITY, String cOUNTRY_CODE, int pARA_STATUS, int aCTIVE_STATUS,
			String jSON_DATA) {
		super();
		ID = iD;
		BANK_TYPE = bANK_TYPE;
		SWIFT_CODE = sWIFT_CODE;
		CONNECTED_TO_SWIFT = cONNECTED_TO_SWIFT;
		SWIFT_CONNECTION = sWIFT_CONNECTION;
		BANK_NAME = bANK_NAME;
		BRANCH = bRANCH;
		ADDRESS_1 = aDDRESS_1;
		ADDRESS_2 = aDDRESS_2;
		ADDRESS_3 = aDDRESS_3;
		ADDRESS_4 = aDDRESS_4;
		CITY = cITY;
		COUNTRY_CODE = cOUNTRY_CODE;
		PARA_STATUS = pARA_STATUS;
		ACTIVE_STATUS = aCTIVE_STATUS;
		JSON_DATA = jSON_DATA;
	}

	public SwiftCodeEntity() {
		
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getBANK_TYPE() {
		return BANK_TYPE;
	}

	public void setBANK_TYPE(String bANK_TYPE) {
		BANK_TYPE = bANK_TYPE;
	}

	public String getSWIFT_CODE() {
		return SWIFT_CODE;
	}

	public void setSWIFT_CODE(String sWIFT_CODE) {
		SWIFT_CODE = sWIFT_CODE;
	}

	public String getCONNECTED_TO_SWIFT() {
		return CONNECTED_TO_SWIFT;
	}

	public void setCONNECTED_TO_SWIFT(String cONNECTED_TO_SWIFT) {
		CONNECTED_TO_SWIFT = cONNECTED_TO_SWIFT;
	}

	public String getSWIFT_CONNECTION() {
		return SWIFT_CONNECTION;
	}

	public void setSWIFT_CONNECTION(String sWIFT_CONNECTION) {
		SWIFT_CONNECTION = sWIFT_CONNECTION;
	}

	public String getBANK_NAME() {
		return BANK_NAME;
	}

	public void setBANK_NAME(String bANK_NAME) {
		BANK_NAME = bANK_NAME;
	}

	public String getBRANCH() {
		return BRANCH;
	}

	public void setBRANCH(String bRANCH) {
		BRANCH = bRANCH;
	}

	public String getADDRESS_1() {
		return ADDRESS_1;
	}

	public void setADDRESS_1(String aDDRESS_1) {
		ADDRESS_1 = aDDRESS_1;
	}

	public String getADDRESS_2() {
		return ADDRESS_2;
	}

	public void setADDRESS_2(String aDDRESS_2) {
		ADDRESS_2 = aDDRESS_2;
	}

	public String getADDRESS_3() {
		return ADDRESS_3;
	}

	public void setADDRESS_3(String aDDRESS_3) {
		ADDRESS_3 = aDDRESS_3;
	}

	public String getADDRESS_4() {
		return ADDRESS_4;
	}

	public void setADDRESS_4(String aDDRESS_4) {
		ADDRESS_4 = aDDRESS_4;
	}

	public String getCITY() {
		return CITY;
	}

	public void setCITY(String cITY) {
		CITY = cITY;
	}

	public String getCOUNTRY_CODE() {
		return COUNTRY_CODE;
	}

	public void setCOUNTRY_CODE(String cOUNTRY_CODE) {
		COUNTRY_CODE = cOUNTRY_CODE;
	}

	public int getPARA_STATUS() {
		return PARA_STATUS;
	}

	public void setPARA_STATUS(int pARA_STATUS) {
		PARA_STATUS = pARA_STATUS;
	}

	public int getACTIVE_STATUS() {
		return ACTIVE_STATUS;
	}

	public void setACTIVE_STATUS(int aCTIVE_STATUS) {
		ACTIVE_STATUS = aCTIVE_STATUS;
	}

	public String getJSON_DATA() {
		return JSON_DATA;
	}

	public void setJSON_DATA(String jSON_DATA) {
		JSON_DATA = jSON_DATA;
	}
	
	
}
