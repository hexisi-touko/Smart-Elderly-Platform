package com.ruoyi.elderly.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.elderly.mapper.TElderlyGuardianMapper;
import com.ruoyi.elderly.domain.TElderlyGuardian;
import com.ruoyi.elderly.service.ITElderlyGuardianService;

/**
 * 老人-监护人关联Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@Service
public class TElderlyGuardianServiceImpl implements ITElderlyGuardianService 
{
    @Autowired
    private TElderlyGuardianMapper tElderlyGuardianMapper;

    /**
     * 查询老人-监护人关联
     * 
     * @param id 老人-监护人关联主键
     * @return 老人-监护人关联
     */
    @Override
    public TElderlyGuardian selectTElderlyGuardianById(Long id)
    {
        return tElderlyGuardianMapper.selectTElderlyGuardianById(id);
    }

    /**
     * 查询老人-监护人关联列表
     * 
     * @param tElderlyGuardian 老人-监护人关联
     * @return 老人-监护人关联
     */
    @Override
    public List<TElderlyGuardian> selectTElderlyGuardianList(TElderlyGuardian tElderlyGuardian)
    {
        return tElderlyGuardianMapper.selectTElderlyGuardianList(tElderlyGuardian);
    }

    /**
     * 新增老人-监护人关联
     * 
     * @param tElderlyGuardian 老人-监护人关联
     * @return 结果
     */
    @Override
    public int insertTElderlyGuardian(TElderlyGuardian tElderlyGuardian)
    {
        tElderlyGuardian.setCreateTime(DateUtils.getNowDate());
        return tElderlyGuardianMapper.insertTElderlyGuardian(tElderlyGuardian);
    }

    /**
     * 修改老人-监护人关联
     * 
     * @param tElderlyGuardian 老人-监护人关联
     * @return 结果
     */
    @Override
    public int updateTElderlyGuardian(TElderlyGuardian tElderlyGuardian)
    {
        tElderlyGuardian.setUpdateTime(DateUtils.getNowDate());
        return tElderlyGuardianMapper.updateTElderlyGuardian(tElderlyGuardian);
    }

    /**
     * 批量删除老人-监护人关联
     * 
     * @param ids 需要删除的老人-监护人关联主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyGuardianByIds(Long[] ids)
    {
        return tElderlyGuardianMapper.deleteTElderlyGuardianByIds(ids);
    }

    /**
     * 删除老人-监护人关联信息
     * 
     * @param id 老人-监护人关联主键
     * @return 结果
     */
    @Override
    public int deleteTElderlyGuardianById(Long id)
    {
        return tElderlyGuardianMapper.deleteTElderlyGuardianById(id);
    }
}
