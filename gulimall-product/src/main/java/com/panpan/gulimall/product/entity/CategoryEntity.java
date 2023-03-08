package com.panpan.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.panpan.common.validgroup.GreateGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 商品三级分类
 * 
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 15:34:39
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Long catId;
	/**
	 * 分类名称
	 */
	@NotEmpty(message = "类型名不能为空",groups={GreateGroup.class})
	private String name;
	/**
	 * 父分类id
	 */
	private Long parentCid;
	/**
	 * 层级
	 */
	private Integer catLevel;
	/**
	 * 是否显示[0-不显示，1显示]
	 */
	@TableLogic
	private Integer showStatus;
	/**
	 * 排序
	 */

	private Integer sort;
	/**
	 * 图标地址
	 */
	private String icon;
	/**
	 * 计量单位
	 */
	private String productUnit;
	/**
	 * 商品数量
	 */
	private Integer productCount;
	/*
	* @Description:添加的子集合
	* @Param:
	* @return:
	* @Author: panpan
	* @Date:25/5/2021
	*/

	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@TableField(exist = false)
	private List<CategoryEntity> children;

}
