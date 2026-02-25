package com.ruoyi.elderly.mapper;

import java.util.List;
import com.ruoyi.elderly.domain.TElderly;

/**
 * 老人基础信息Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
public interface TElderlyMapper 
{
    /**
     * 查询老人基础信息
     * 
     * @param elderlyId 老人基础信息主键
     * @return 老人基础信息
     */
    public TElderly selectTElderlyByElderlyId(Long elderlyId);

    /**
     * 查询老人基础信息列表
     * 
     * @param tElderly 老人基础信息
     * @return 老人基础信息集合
     */
    public List<TElderly> selectTElderlyList(TElderly tElderly);

    /**
     * 新增老人基础信息
     * 
     * @param tElderly 老人基础信息
     * @return 结果
     */
    public int insertTElderly(TElderly tElderly);

    /**
     * 修改老人基础信息
     * 
     * @param tElderly 老人基础信息
     * @return 结果
     */
    public int updateTElderly(TElderly tElderly);

    /**
     * 删除老人基础信息
     * 
     * @param elderlyId 老人基础信息主键
     * @return 结果
     */
    public int deleteTElderlyByElderlyId(Long elderlyId);

    /**
     * 批量删除老人基础信息
     * 
     * @param elderlyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTElderlyByElderlyIds(Long[] elderlyIds);
}
