package com.bqjr.model;

import java.io.Serializable;

public class EntityName implements Serializable {
    /**
	 *@Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)    
	 */
	
	private static final long serialVersionUID = 186216532847448351L;

	private String SERIALNO;

    private String USERNAME;

    private String USERCODE;

    private Short MQTYPE;

    private String MQKEY;

    public String getSERIALNO() {
        return SERIALNO;
    }

    public void setSERIALNO(String SERIALNO) {
        this.SERIALNO = SERIALNO == null ? null : SERIALNO.trim();
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME == null ? null : USERNAME.trim();
    }

    public String getUSERCODE() {
        return USERCODE;
    }

    public void setUSERCODE(String USERCODE) {
        this.USERCODE = USERCODE == null ? null : USERCODE.trim();
    }

    public Short getMQTYPE() {
        return MQTYPE;
    }

    public void setMQTYPE(Short MQTYPE) {
        this.MQTYPE = MQTYPE;
    }

    public String getMQKEY() {
        return MQKEY;
    }

    public void setMQKEY(String MQKEY) {
        this.MQKEY = MQKEY == null ? null : MQKEY.trim();
    }

	/* (非 Javadoc)  
	 * <p>Title: toString</p>    
	 * <p>Description:  TODO(描述) </p>    
	 * @return    
	 * @see java.lang.Object#toString()    
	 */
	@Override
	public String toString() {
		return "EntityName [SERIALNO=" + SERIALNO + ", USERNAME=" + USERNAME + ", USERCODE=" + USERCODE + ", MQTYPE="
				+ MQTYPE + ", MQKEY=" + MQKEY + "]";
	}
    
}