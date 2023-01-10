package com.newverse.yama.live.domain.respository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.newverse.yama.live.domain.entity.FileItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author asus
 * @description 针对表【user_Info】的数据库操作Mapper
 * @createDate 2022-06-11 16:06:28
 * @Entity generator.domain.UserSupport
 */
@Mapper
public interface FileItemMapper extends BaseMapper<FileItem> {

}
