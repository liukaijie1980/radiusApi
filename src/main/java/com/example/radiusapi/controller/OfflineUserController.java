package com.example.radiusapi.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.radiusapi.entity.OfflineUser;
import com.example.radiusapi.mapper.OfflineUserMapper;
import com.example.radiusapi.utils.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.util.Date;


@Slf4j
@RestController
public class OfflineUserController {

    @Autowired
    private OfflineUserMapper Mapper;
    @ApiOperation("Get Offline User List")
    @GetMapping("/OfflineUser")
    public Result GetOfflineUser(@RequestParam("name") String UserName,
                                @RequestParam("realm") String realm,
                                 @RequestParam("framedipaddress") String FramedIpAddress,
                                 @RequestParam("callingstationid") String CallingStationid,
                                 @RequestParam("from") String DateFrom,
                                 @RequestParam("to") String DateTo,

                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize

    )
    {
        log.info("GetOfflineUser({},{},{},{},{}，{})",UserName,realm,FramedIpAddress,CallingStationid, DateFrom,DateTo);

        QueryWrapper<OfflineUser> wrapper= new QueryWrapper<OfflineUser>();
        Page<OfflineUser> page=new Page<>(pageNo,pageSize);
        if (UserName.length()!=0)
        {
            wrapper.eq("username",UserName);
        }
        wrapper.eq("realm",realm);
        if (FramedIpAddress.length()!=0)
        {
            wrapper.eq("framedipaddress",FramedIpAddress);
        }
        if (CallingStationid.length()!=0)
        {
            wrapper.eq("callingstationid",CallingStationid);
        }
        if (DateFrom.length()!=0 && DateTo.length()!=0)
        {/*
           我们的目的是选出在指定时间段（from，to），在线的用户
                            from ------ to         ||        from ------ to             ||         from ------ to
                                 start------stop         start-----stop                      start------------------stop
           &&(  (DateFrom<= acctstarttime <=DateTo )||(DateFrom<= acctstoptime <=DateTo)||(acctstarttime<=DateFrom && acctstoptime >=DateTo) )
          */
            wrapper.and( e->e
                            .and( QueryWrapper->QueryWrapper.ge("acctstarttime",DateFrom).le("acctstarttime",DateTo))

            .or( QueryWrapper->QueryWrapper
                            .ge("acctstoptime",DateFrom).le("acctstoptime",DateTo))
                            .or(QueryWrapper->QueryWrapper.le("acctstarttime",DateFrom).ge ("acctstoptime",DateTo)        )

            );
        }

        IPage<OfflineUser> iPage;
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


