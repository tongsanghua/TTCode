package com.github.tongsanghua.code.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaColumn {
	/**
	 * 列名
	 */
	private String columnName;
	/**
	 * 列名类型
	 */
	private String columnType;
	/**
	 * 列名备注
	 */
	private String columnComment;

	/**
	 * 主键标识
	 */
	private String columnKey;

}
