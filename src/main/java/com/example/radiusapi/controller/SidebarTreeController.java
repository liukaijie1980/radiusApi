package com.example.radiusapi.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.radiusapi.entity.SidebarTree;

import com.example.radiusapi.mapper.SidebarTreeMapper;
import com.example.radiusapi.utils.JwtUtils;
import com.example.radiusapi.utils.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
@Slf4j
@RestController
public class SidebarTreeController {
    @Autowired
    private SidebarTreeMapper treeNodeMapper;

     //递归遍历出给定List中指定id的所有子list，

    private  List<SidebarTree> getAllSubNodes(List<SidebarTree> menuList, String id, List<SidebarTree> childList)
    {
        for (int i=0;i<menuList.size();i++) {
            //遍历出父id等于参数的id,add进子节点集合
            if (menuList.get(i).getPid().equals(id) ) {
                childList.add(menuList.get(i));
                String subid=menuList.get(i).getId();
                //每次递归都要全数组遍历，如果数据量大，应该用链表实现本函数，在递归前去掉已经处理过的元素
                //LinkList.remove(menuList.get(i));
                //递归遍历下一级
                getAllSubNodes(menuList, subid,childList);

            }
        }


        return childList;
    }



    @ApiOperation("Get SidebarTree")
    @GetMapping("/SidebarTree")
    public Result GetTreeNodeList( @RequestParam("nodeid") String NodeId)
    {
        log.info("GetTreeNodeList({})", NodeId);

      // QueryWrapper<SidebarTree> wrapper= new QueryWrapper<SidebarTree>();
       // wrapper.orderByAsc("label");
        List<SidebarTree> nodeList;
        Result ret=new Result();
        try {
            //获取数据库中关于前端左侧树节点的所有记录
            nodeList = treeNodeMapper.selectList(null);

        } catch (DataAccessException e) {
            // 处理异常
            log.error(e.getMessage());
            ret.error();
            return ret;
        }
        log.debug(nodeList.toString());

        List<SidebarTree> childList=new ArrayList<>();
        if (nodeList.size()>0)
        {
           log.debug("getAllSubNodes->childList" );

            //取得NodeId节点的所有孩子节点
            childList=getAllSubNodes(nodeList,NodeId,childList);
            //加上NodeId节点自己
            for (SidebarTree mu:nodeList) {
               if ( mu.getId().equals(NodeId))
               {
                   childList.add(mu);
               }
            }
        }
        ret=new Result();
        ret.ok();

       ret.data("data",childList);
       // ret.data("data",nodeList);
        log.info("result={}",ret);
        return  ret;
    }


    @ApiOperation("set SidebarTree")
    @PostMapping("/SidebarTree")
    @Transactional
    public Result SetTreeNodeList(@RequestBody List <SidebarTree> trlist)
    {
        log.info("SetTreeNodeList({})", trlist);

       int res=treeNodeMapper.delete(null);
       if (res<=0) {
           throw new RuntimeException();
       }
       for (int i = 0; i < trlist.size(); i++)
       {
           res=treeNodeMapper.insert(trlist.get(i));
           if (res<=0) {
               throw new RuntimeException();
           }
       }
       Result ret=new Result();
        ret.ok();
       return ret  ;
    }



}

