package com.ruoyi.elderly.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.elderly.mapper.TGuardianMapper;
import com.ruoyi.elderly.domain.TGuardian;
import com.ruoyi.elderly.service.ITGuardianService;

/**
 * 监护人信息Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
@Service
public class TGuardianServiceImpl implements ITGuardianService 
{
    @Autowired
    private TGuardianMapper tGuardianMapper;

    /**
     * 查询监护人信息
     * 
     * @param guardianId 监护人信息主键
     * @return 监护人信息
     */
    @Override
    public TGuardian selectTGuardianByGuardianId(Long guardianId)
    {
        return tGuardianMapper.selectTGuardianByGuardianId(guardianId);
    }

    /**
     * 查询监护人信息列表
     * 
     * @param tGuardian 监护人信息
     * @return 监护人信息
     */
    @Override
    public List<TGuardian> selectTGuardianList(TGuardian tGuardian)
    {
        return tGuardianMapper.selectTGuardianList(tGuardian);
    }

    /**
     * 新增监护人信息
     * 
     * @param tGuardian 监护人信息
     * @return 结果
     */
    @Override
    public int insertTGuardian(TGuardian tGuardian)
    {
        tGuardian.setCreateTime(DateUtils.getNowDate());
        return tGuardianMapper.insertTGuardian(tGuardian);
    }

    /**
     * 修改监护人信息
     * 
     * @param tGuardian 监护人信息
     * @return 结果
     */
    @Override
    public int updateTGuardian(TGuardian tGuardian)
    {
        tGuardian.setUpdateTime(DateUtils.getNowDate());
        return tGuardianMapper.updateTGuardian(tGuardian);
    }

    /**
     * 批量删除监护人信息
     * 
     * @param guardianIds 需要删除的监护人信息主键
     * @return 结果
     */
    @Override
    public int deleteTGuardianByGuardianIds(Long[] guardianIds)
    {
        return tGuardianMapper.deleteTGuardianByGuardianIds(guardianIds);
    }

    /**
     * 删除监护人信息信息
     * 
     * @param guardianId 监护人信息主键
     * @return 结果
     */
    @Override
    public int deleteTGuardianByGuardianId(Long guardianId)
    {
        return tGuardianMapper.deleteTGuardianByGuardianId(guardianId);
    }
}
