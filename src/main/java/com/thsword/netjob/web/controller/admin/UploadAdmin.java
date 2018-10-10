package com.thsword.netjob.web.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.thsword.netjob.global.Global;
import com.thsword.netjob.service.UserService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.util.PathUtil;
import com.thsword.utils.file.FileUtil;


@Controller
public class UploadAdmin {
	@Resource(name="userService")
	UserService userService;
	/**
	 * @throws Exception 
	 * 上传文件
	* @Title: login 
	* @Description: 上传用户头像
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("admin/user/uploadImage")
	public void uploadUserImage(HttpServletRequest request,HttpServletResponse response,MultipartFile file) throws Exception{
		try {
			if(null==file||file.getBytes().length<=0||StringUtils.isEmpty(file.getOriginalFilename())){
				JsonResponseUtil.codeResponse(ErrorUtil.FILE_NOT_EXISTS, response, request);
				return;
			}
			String relativePath = PathUtil.getDatePath(Global.SYS_FILE_MANAGER_PATH);
			String fullPath = PathUtil.getFullPath(relativePath);
			String fileName = FileUtil.getRandomFileName(file.getOriginalFilename());
			boolean flag = FileUtil.upload(file.getInputStream(),fullPath +"/"+fileName);
			if(!flag){
				JsonResponseUtil.codeResponse(ErrorUtil.FILE_UPLOAD_ERROR, response, request);
			}else{
				FileUtil.getFileExtension(fileName);
				JsonResponseUtil.successBodyResponse(relativePath+"/"+fileName,response, request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @throws Exception 
	 * 上传文件
	* @Title: login 
	* @Description: 上传广告图片
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("admin/banner/uploadImage")
	public void uploadBannerImage(HttpServletRequest request,HttpServletResponse response,MultipartFile file) throws Exception{
		try {
			if(null==file||file.getBytes().length<=0||StringUtils.isEmpty(file.getOriginalFilename())){
				JsonResponseUtil.codeResponse(ErrorUtil.FILE_NOT_EXISTS, response, request);
				return;
			}
			String relativePath = PathUtil.getDatePath(Global.SYS_FILE_BANNER_PATH);
			String fullPath = PathUtil.getFullPath(relativePath);
			String fileName = FileUtil.getRandomFileName(file.getOriginalFilename());
			boolean flag = FileUtil.upload(file.getInputStream(),fullPath +"/"+fileName);
			if(!flag){
				JsonResponseUtil.codeResponse(ErrorUtil.FILE_UPLOAD_ERROR, response, request);
			}else{
				JsonResponseUtil.successBodyResponse(relativePath+"/"+fileName,response, request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @throws Exception 
	 * 上传文件
	* @Title: login 
	* @Description: 上传广告图片
	* @param @param request
	* @param @param response    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("admin/menu/uploadImage")
	public void uploadMenuImage(HttpServletRequest request,HttpServletResponse response,MultipartFile file) throws Exception{
		try {
			if(null==file||file.getBytes().length<=0||StringUtils.isEmpty(file.getOriginalFilename())){
				JsonResponseUtil.codeResponse(ErrorUtil.FILE_NOT_EXISTS, response, request);
				return;
			}
			String relativePath = PathUtil.getDatePath(Global.SYS_FILE_MENU_PATH);
			String fullPath = PathUtil.getFullPath(relativePath);
			String fileName = FileUtil.getRandomFileName(file.getOriginalFilename());
			boolean flag = FileUtil.upload(file.getInputStream(),fullPath +"/"+fileName);
			if(!flag){
				JsonResponseUtil.codeResponse(ErrorUtil.FILE_UPLOAD_ERROR, response, request);
			}else{
				JsonResponseUtil.successBodyResponse(relativePath+"/"+fileName,response, request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
