package com.nan.jvm.mapperDao;

import com.nan.jvm.entitys.exportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExportTest {

    List<exportEntity> selectList();

    @Select("select * from export_test")
    List<exportEntity> selectPage();

    int PageCount();
}
