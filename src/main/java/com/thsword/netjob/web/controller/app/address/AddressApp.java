package com.thsword.netjob.web.controller.app.address;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thsword.netjob.dao.IAddressDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Address;
import com.thsword.netjob.pojo.resp.address.AddressListResp;
import com.thsword.netjob.service.IBaseService;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.utils.object.UUIDUtil;

@RestController
@Api(tags = "NETJOB-ADDRESS", description = "地址接口")
public class AddressApp {
	@Resource(name = "baseService")
	private IBaseService baseService;

	@RequestMapping("app/member/address/list")
	@ApiOperation(value = "地址列表", httpMethod = "POST")
	public AddressListResp list(HttpServletRequest request,
			HttpServletResponse response) {
		String memberId = (String) request.getAttribute("memberId");
		Address address = new Address();
		address.setMemberId(memberId);
		address.setIsDel(false);
		@SuppressWarnings("unchecked")
		List<Address> addresses = (List<Address>) baseService.queryAllEntity(
				IAddressDao.class, address);
		return AddressListResp.builder().list(addresses).build();
	}

	@RequestMapping("app/member/address/add")
	@ApiOperation(value = "添加地址", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "province", value = "省代码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "citycode", value = "城市代码", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "provinceName", value = "省名", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "cityName", value = "市名", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "area", value = "区县名", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "detailAddress", value = "详细地址", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "receiver", value = "收件人", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "phone", value = "收件电话", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "isDefault", value = "是否默认地址（0-否，1-是）", dataType = "string", paramType = "query", required = true) })
	public void add(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String province, @RequestParam String citycode,
			@RequestParam String provinceName, @RequestParam String cityName,
			@RequestParam String area, @RequestParam String detailAddress,
			@RequestParam String receiver, @RequestParam String phone,
			@RequestParam int isDefault) {
		String memberId = (String) request.getAttribute("memberId");
		Address address = new Address();
		if (isDefault == Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_YES) {
			Address temp = new Address();
			temp.setMemberId(memberId);
			temp.setIsDefault(isDefault);
			temp = (Address) baseService
					.queryEntity(IAddressDao.class, address);
			if (null != temp) {
				temp.setIsDefault(Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_NO);
				baseService.updateEntity(IAddressDao.class, temp);
			}
		}
		address.setId(UUIDUtil.get32UUID());
		address.setArea(area);
		address.setProvince(provinceName);
		address.setCitycode(citycode);
		address.setProvinceName(provinceName);
		address.setCityName(cityName);
		address.setArea(area);
		address.setReceiver(receiver);
		address.setPhone(phone);
		address.setDetailAddress(detailAddress);
		address.setMemberId(memberId);
		address.setCreateBy(memberId);
		address.setUpdateBy(memberId);
		baseService.addEntity(IAddressDao.class, address);
		JsonResponseUtil.successCodeResponse(response, request);
	}

	@RequestMapping("app/member/address/edit")
	@ApiOperation(value = "编辑地址", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "province", value = "省代码", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "citycode", value = "城市代码", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "provinceName", value = "省名", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "cityName", value = "市名", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "area", value = "区县名", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "detailAddress", value = "详细地址", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "receiver", value = "收件人", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "phone", value = "收件电话", dataType = "string", paramType = "query"),
			@ApiImplicitParam(name = "isDefault", value = "是否默认地址（0-否，1-是）", dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "id", value = "id", dataType = "String", paramType = "query", required = true) })
	public BaseResponse edit(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String province,
			@RequestParam(required = false) String citycode,
			@RequestParam(required = false) String provinceName,
			@RequestParam(required = false) String cityName,
			@RequestParam(required = false) String area,
			@RequestParam(required = false) String detailAddress,
			@RequestParam(required = false) String receiver,
			@RequestParam(required = false) String phone,
			@RequestParam(required = false) Integer isDefault,
			@RequestParam String id) {
		String memberId = (String) request.getAttribute("memberId");
		if (null != isDefault
				&& isDefault == Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_YES) {
			Address temp = new Address();
			temp.setMemberId(memberId);
			temp.setIsDefault(Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_YES);
			temp = (Address) baseService.queryEntity(IAddressDao.class, temp);
			if (null != temp) {
				temp.setIsDefault(Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_NO);
				baseService.updateEntity(IAddressDao.class, temp);
			}
		}
		Address address = new Address();
		address.setId(id);
		address.setArea(area);
		address.setProvince(provinceName);
		address.setCitycode(citycode);
		address.setProvinceName(provinceName);
		address.setCityName(cityName);
		address.setArea(area);
		address.setReceiver(receiver);
		address.setPhone(phone);
		address.setDetailAddress(detailAddress);
		address.setMemberId(memberId);
		address.setCreateBy(memberId);
		address.setUpdateBy(memberId);
		baseService.updateEntity(IAddressDao.class, address);
		return BaseResponse.success();
	}

	@RequestMapping("app/member/address/delete")
	@ApiOperation(value = "删除地址", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "地址ID", dataType = "String", paramType = "query", required = true) })
	public BaseResponse delete(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String id) {
		baseService.deleteEntityById(IAddressDao.class, id);
		return BaseResponse.success();
	}
}
