package com.javaee.accountbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaee.accountbook.mapper.TypeMapper;
import com.javaee.accountbook.pojo.Type;
import com.javaee.accountbook.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;


@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {
    @Autowired
    TypeMapper typeMapper;

    public String[] getType(){
        List<String> list = typeMapper.selectTypeName();
        String[] types = new String[list.size()];
        for(int i =0;i< list.size();i++){
            types[i] = list.get(i);
        }
        return types;
    }

    public void saveType(String name){
        Type type = new Type();
        type.setTypeName(name);
        typeMapper.insert(type);
    }

    public void updateType(String name,String newName){
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.eq("type_name",name);
        Type type = new Type();
        type.setTypeName(newName);
        update(type,typeQueryWrapper);
    }

    public void deleteType(String name){
        QueryWrapper<Type> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.eq("id",name);
        typeMapper.delete(typeQueryWrapper);
    }

    public Vector<Vector> getAllType(){
        Vector<Vector> data = new Vector<>();
        List<Type> list = this.list();
        for (Type type : list) {
            if(type.getId() <= 6) continue;
            Vector t = new Vector<>();
            t.add(type.getId().toString());
            t.add(type.getTypeName());
            data.add(t);
        }
        return data;
    }
}
