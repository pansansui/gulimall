package com.panpan.gulimall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author pansansui
 * @email 839818374@qq.com
 * @date 2021-05-22 18:14:01
 */
@Data
@TableName("undo_log")
public class UndoLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long branchId;
	/**
	 * 
	 */
	private String xid;
	/**
	 * 
	 */
	private String context;
	/**
	 * 
	 */
	private byte[] rollbackInfo;
	/**
	 * 
	 */
	private Integer logStatus;
	/**
	 * 
	 */
	private Date logCreated;
	/**
	 * 
	 */
	private Date logModified;
	/**
	 * 
	 */
	private String ext;

}
