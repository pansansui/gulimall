package com.panpan.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.panpan.common.validgroup.GreateGroup;
import com.panpan.common.validgroup.ListGroup;
import com.panpan.common.validgroup.UpdateGroup;
import com.panpan.gulimall.product.groups.GGreate;
import com.panpan.common.validnote.Number;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

/**
 * 品牌
 * 
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotBlank(message = "精确查找ID不能为空",groups = {ListGroup.class, UpdateGroup.class})
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空",groups = {GreateGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@URL(message = "必须是URL")
	@NotBlank(message = "不能为空")
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@Number(vals = {1,0},message = "只能为1或者0",groups = {GreateGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	private String firstLetter;
	/**
	 * 排序
	 */
	private Integer sort;

}
