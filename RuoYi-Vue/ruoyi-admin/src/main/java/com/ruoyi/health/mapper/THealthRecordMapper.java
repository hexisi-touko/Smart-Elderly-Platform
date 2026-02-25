package com.ruoyi.health.mapper;

import java.util.List;
import com.ruoyi.health.domain.THealthRecord;

/**
 * 健康记录管理Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-25
 */
public interface THealthRecordMapper 
{
    /**
     * 查询健康记录管理
     * 
     * @param recordId 健康记录管理主键
     * @return 健康记录管理
     */
    public THealthRecord selectTHealthRecordByRecordId(Long recordId);

    /**
     * 查询健康记录管理列表
     * 
     * @param tHealthRecord 健康记录管理
     * @return 健康记录管理集合
     */
    public List<THealthRecord> selectTHealthRecordList(THealthRecord tHealthRecord);

    /**
     * 新增健康记录管理
     * 
     * @param tHealthRecord 健康记录管理
     * @return 结果
     */
    public int insertTHealthRecord(THealthRecord tHealthRecord);

    /**
     * 修改健康记录管理
     * 
     * @param tHealthRecord 健康记录管理
     * @return 结果
     */
    public int updateTHealthRecord(THealthRecord tHealthRecord);

    /**
     * 删除健康记录管理
     * 
     * @param recordId 健康记录管理主键
     * @return 结果
     */
    public int deleteTHealthRecordByRecordId(Long recordId);

    /**
     * 批量删除健康记录管理
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTHealthRecordByRecordIds(Long[] recordIds);
}
