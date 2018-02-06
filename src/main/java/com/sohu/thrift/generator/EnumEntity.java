/**
 * 
 */
package com.sohu.thrift.generator;

import java.util.List;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-30 下午12:18:39
 */
public class EnumEntity {

	private String name;
	
	private List<EnumField> fields;


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the fields
	 */
	public List<EnumField> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<EnumField> fields) {
		this.fields = fields;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnumEntity other = (EnumEntity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	
	
}
