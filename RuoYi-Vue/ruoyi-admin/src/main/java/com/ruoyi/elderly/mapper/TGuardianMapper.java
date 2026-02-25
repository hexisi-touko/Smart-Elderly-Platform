package com.ruoyi.elderly.mapper;

import java.util.List;
import com.ruoyi.elderly.domain.TGuardian;

/**
 * 监护人信息Mapper接口
 * 
 * @author zhangTing
 * @date 2026-02-24
 */
public interface TGuardianMapper 
{
    /**
     * 查询监护人信息
     * 
     * @param guardianId 监护人信息主键
     * @return 监护人信息
     */
    public TGuardian selectTGuardianByGuardianId(Long guardianId);

    /**
     * 查询监护人信息列表
     * 
     * @param tGuardian 监护人信息
     * @return 监护人信息集合
     */
    public List<TGuardian> selectTGuardianList(TGuardian tGuardian);

    /**
     * 新增监护人信息
     * 
     * @param tGuardian 监护人信息
     * @return 结果
     */
    public int insertTGuardian(TGuardian tGuardian);

    /**
     * 修改监护人信息
     * 
     * @param tGuardian 监护人信息
     * @return 结果
     */
    public int updateTGuardian(TGuardian tGuardian);

    /**
     * 删除监护人信息
     * 
     * @param guardianId 监护人信息主键
     * @return 结果
     */
    public int deleteTGuardianByGuardianId(Long guardianId);

    /**
     * 批量删除监护人信息
     * 
     * @param guardianIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTGuardianByGuardianIds(Long[] guardianIds);
}
