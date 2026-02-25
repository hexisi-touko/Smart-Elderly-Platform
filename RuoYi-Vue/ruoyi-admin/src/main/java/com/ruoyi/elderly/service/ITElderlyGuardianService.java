package com.ruoyi.elderly.service;

import java.util.List;
import com.ruoyi.elderly.domain.TElderlyGuardian;

/**
 * 老人-监护人关联Service接口
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
public interface ITElderlyGuardianService 
{
    /**
     * 查询老人-监护人关联
     * 
     * @param id 老人-监护人关联主键
     * @return 老人-监护人关联
     */
    public TElderlyGuardian selectTElderlyGuardianById(Long id);

    /**
     * 查询老人-监护人关联列表
     * 
     * @param tElderlyGuardian 老人-监护人关联
     * @return 老人-监护人关联集合
     */
    public List<TElderlyGuardian> selectTElderlyGuardianList(TElderlyGuardian tElderlyGuardian);

    /**
     * 新增老人-监护人关联
     * 
     * @param tElderlyGuardian 老人-监护人关联
     * @return 结果
     */
    public int insertTElderlyGuardian(TElderlyGuardian tElderlyGuardian);

    /**
     * 修改老人-监护人关联
     * 
     * @param tElderlyGuardian 老人-监护人关联
     * @return 结果
     */
    public int updateTElderlyGuardian(TElderlyGuardian tElderlyGuardian);

    /**
     * 批量删除老人-监护人关联
     * 
     * @param ids 需要删除的老人-监护人关联主键集合
     * @return 结果
     */
    public int deleteTElderlyGuardianByIds(Long[] ids);

    /**
     * 删除老人-监护人关联信息
     * 
     * @param id 老人-监护人关联主键
     * @return 结果
     */
    public int deleteTElderlyGuardianById(Long id);
}
