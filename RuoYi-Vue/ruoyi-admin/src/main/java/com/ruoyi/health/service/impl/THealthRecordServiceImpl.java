package com.ruoyi.health.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.health.mapper.THealthRecordMapper;
import com.ruoyi.health.domain.THealthRecord;
import com.ruoyi.health.service.ITHealthRecordService;

/**
 * 健康记录管理Service业务层处理
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
@Service
public class THealthRecordServiceImpl implements ITHealthRecordService 
{
    @Autowired
    private THealthRecordMapper tHealthRecordMapper;

    /**
     * 查询健康记录管理
     * 
     * @param recordId 健康记录管理主键
     * @return 健康记录管理
     */
    @Override
    public THealthRecord selectTHealthRecordByRecordId(Long recordId)
    {
        return tHealthRecordMapper.selectTHealthRecordByRecordId(recordId);
    }

    /**
     * 查询健康记录管理列表
     * 
     * @param tHealthRecord 健康记录管理
     * @return 健康记录管理
     */
    @Override
    public List<THealthRecord> selectTHealthRecordList(THealthRecord tHealthRecord)
    {
        return tHealthRecordMapper.selectTHealthRecordList(tHealthRecord);
    }

    /**
     * 新增健康记录管理
     * 
     * @param tHealthRecord 健康记录管理
     * @return 结果
     */
    @Override
    public int insertTHealthRecord(THealthRecord tHealthRecord)
    {
        tHealthRecord.setCreateTime(DateUtils.getNowDate());
        return tHealthRecordMapper.insertTHealthRecord(tHealthRecord);
    }

    /**
     * 修改健康记录管理
     * 
     * @param tHealthRecord 健康记录管理
     * @return 结果
     */
    @Override
    public int updateTHealthRecord(THealthRecord tHealthRecord)
    {
        tHealthRecord.setUpdateTime(DateUtils.getNowDate());
        return tHealthRecordMapper.updateTHealthRecord(tHealthRecord);
    }

    /**
     * 批量删除健康记录管理
     * 
     * @param recordIds 需要删除的健康记录管理主键
     * @return 结果
     */
    @Override
    public int deleteTHealthRecordByRecordIds(Long[] recordIds)
    {
        return tHealthRecordMapper.deleteTHealthRecordByRecordIds(recordIds);
    }

    /**
     * 删除健康记录管理信息
     * 
     * @param recordId 健康记录管理主键
     * @return 结果
     */
    @Override
    public int deleteTHealthRecordByRecordId(Long recordId)
    {
        return tHealthRecordMapper.deleteTHealthRecordByRecordId(recordId);
    }
}
