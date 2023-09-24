package com.example.radiusapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.radiusapi.entity.Radpostauth;

import com.example.radiusapi.mapper.RadpostauthMapper;
import com.example.radiusapi.utils.Result;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@Slf4j
@RestController
public class RadpostauthController {

    @Autowired
    private RadpostauthMapper Mapper;
    @Operation(summary ="Get Radpostauth List")
    @GetMapping("/radpostauth")
    public Result GetRadpostauth(@RequestParam("name") String UserName,
                                 @RequestParam("realm") String realm,
                                 @RequestParam("reply") String reply,
                                 @RequestParam("callingstationid") String CallingStationid,
                                 @RequestParam("nasidentifier") String NasIdentifier,
                                 @RequestParam("from") String DateFrom,
                                 @RequestParam("to") String DateTo,

                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                 @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize

    )
    {
        log.info("GetRadpostauth({},{},{},{},{}，{}，{})",
                UserName,realm,reply,CallingStationid,NasIdentifier, DateFrom,DateTo);

        QueryWrapper<Radpostauth> wrapper= new QueryWrapper<Radpostauth>();
        Page<Radpostauth> page=new Page<>(pageNo,pageSize);
        if (UserName.length()!=0)
        {
            wrapper.eq("username",UserName);
        }
        wrapper.eq("realm",realm);
        if (reply.length()!=0)
        {
            wrapper.eq("reply",reply);
        }
        if (CallingStationid.length()!=0)
        {
            wrapper.eq("callingstationid",CallingStationid);
        }
        if (NasIdentifier.length()!=0)
        {
            wrapper.eq("nasidentifier",NasIdentifier);
        }
        if (DateFrom.length()!=0 && DateTo.length()!=0)
        {
            wrapper.ge("authdate",DateFrom);
            wrapper.le("authdate",DateTo);
        }

        IPage<Radpostauth> iPage;
        Result ret=new Result();

        try {
            iPage=Mapper.selectPage(page,wrapper);
        } catch (DataAccessException e) {
            // 处理异常
            log.error(e.getMessage());
            ret.error();
            return ret;
        }
        ret.ok();
        ret.data("data",iPage);
        log.info("result={}",ret);
        return ret;
    }
}
