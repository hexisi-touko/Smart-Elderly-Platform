<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="老人姓名" prop="elderlyName">
        <el-input
          v-model="queryParams.elderlyName"
          placeholder="请输入老人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据采集时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeCollectTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="记录类型" prop="recordType">
        <el-select v-model="queryParams.recordType" placeholder="请选择记录类型" clearable>
          <el-option
            v-for="dict in health_record_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="采集方式" prop="collectMethod">
        <el-select v-model="queryParams.collectMethod" placeholder="请选择采集方式" clearable>
          <el-option
            v-for="dict in collect_method"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="数据状态" prop="dataStatus">
        <el-select v-model="queryParams.dataStatus" placeholder="请选择数据状态" clearable>
          <el-option
            v-for="dict in health_data_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['health:healthRecord:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['health:healthRecord:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['health:healthRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['health:healthRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="healthRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="设备编码" align="center" prop="deviceCode" />
      <el-table-column label="数据采集时间" align="center" prop="collectTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.collectTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="记录类型" align="center" prop="recordType">
        <template #default="scope">
          <dict-tag :options="health_record_type" :value="scope.row.recordType"/>
        </template>
      </el-table-column>
      <el-table-column label="采集方式" align="center" prop="collectMethod">
        <template #default="scope">
          <dict-tag :options="collect_method" :value="scope.row.collectMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="收缩压" align="center" prop="systolicBp" />
      <el-table-column label="舒张压" align="center" prop="diastolicBp" />
      <el-table-column label="血糖值" align="center" prop="bloodSugar" />
      <el-table-column label="心率" align="center" prop="heartRate" />
      <el-table-column label="体温" align="center" prop="temperature" />
      <el-table-column label="血氧饱和度" align="center" prop="bloodOxygen" />
      <el-table-column label="数据状态" align="center" prop="dataStatus">
        <template #default="scope">
          <dict-tag :options="health_data_status" :value="scope.row.dataStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['health:healthRecord:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['health:healthRecord:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改健康记录管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="healthRecordRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人" prop="elderlyId">
          <el-select
            v-model="form.elderlyId"
            filterable
            remote
            reserve-keyword
            placeholder="请输入老人姓名搜索"
            :remote-method="remoteSearchElderly"
            :loading="elderlyLoading"
            style="width: 100%"
          >
            <el-option
              v-for="item in elderlyOptions"
              :key="item.elderlyId"
              :label="item.name + ' (' + (item.phone || '无手机号') + ')'"
              :value="item.elderlyId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="关联采集设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入关联采集设备ID" />
        </el-form-item>
        <el-form-item label="数据采集时间" prop="collectTime">
          <el-date-picker clearable
            v-model="form.collectTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择数据采集时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="记录类型" prop="recordType">
          <el-select v-model="form.recordType" placeholder="请选择记录类型">
            <el-option
              v-for="dict in health_record_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="采集方式" prop="collectMethod">
          <el-select v-model="form.collectMethod" placeholder="请选择采集方式">
            <el-option
              v-for="dict in collect_method"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收缩压" prop="systolicBp">
          <el-input v-model="form.systolicBp" placeholder="请输入收缩压" />
        </el-form-item>
        <el-form-item label="舒张压" prop="diastolicBp">
          <el-input v-model="form.diastolicBp" placeholder="请输入舒张压" />
        </el-form-item>
        <el-form-item label="血糖值" prop="bloodSugar">
          <el-input v-model="form.bloodSugar" placeholder="请输入血糖值" />
        </el-form-item>
        <el-form-item label="心率" prop="heartRate">
          <el-input v-model="form.heartRate" placeholder="请输入心率" />
        </el-form-item>
        <el-form-item label="体温" prop="temperature">
          <el-input v-model="form.temperature" placeholder="请输入体温" />
        </el-form-item>
        <el-form-item label="血氧饱和度" prop="bloodOxygen">
          <el-input v-model="form.bloodOxygen" placeholder="请输入血氧饱和度" />
        </el-form-item>
        <el-form-item label="数据状态" prop="dataStatus">
          <el-select v-model="form.dataStatus" placeholder="请选择数据状态">
            <el-option
              v-for="dict in health_data_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="HealthRecord">
import { listHealthRecord, getHealthRecord, delHealthRecord, addHealthRecord, updateHealthRecord } from "@/api/health/healthRecord"
import { listElderly } from "@/api/elderly/elderly"

const { proxy } = getCurrentInstance()
const { health_record_type, collect_method, health_data_status } = proxy.useDict('health_record_type', 'collect_method', 'health_data_status')

const healthRecordList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeCollectTime = ref([])
const elderlyOptions = ref([])
const elderlyLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    elderlyName: null,
    deviceId: null,
    collectTime: null,
    recordType: null,
    collectMethod: null,
    dataStatus: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    collectTime: [
      { required: true, message: "数据采集时间不能为空", trigger: "blur" }
    ],
    recordType: [
      { required: true, message: "记录类型不能为空", trigger: "change" }
    ],
    collectMethod: [
      { required: true, message: "采集方式不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询健康记录管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeCollectTime.value && '' != daterangeCollectTime.value) {
    queryParams.value.params["beginCollectTime"] = daterangeCollectTime.value[0]
    queryParams.value.params["endCollectTime"] = daterangeCollectTime.value[1]
  }
  listHealthRecord(queryParams.value).then(response => {
    healthRecordList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    recordId: null,
    elderlyId: null,
    deviceId: null,
    collectTime: null,
    recordType: null,
    collectMethod: null,
    systolicBp: null,
    diastolicBp: null,
    bloodSugar: null,
    heartRate: null,
    temperature: null,
    bloodOxygen: null,
    dataStatus: null,
    thirdPartySyncId: null,
    backupFlag: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("healthRecordRef")
}

/** 远程搜索老人 */
function remoteSearchElderly(query) {
  if (query) {
    elderlyLoading.value = true
    listElderly({ name: query, pageNum: 1, pageSize: 20 }).then(res => {
      elderlyOptions.value = res.rows
      elderlyLoading.value = false
    })
  } else {
    elderlyOptions.value = []
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCollectTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.recordId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加健康记录管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _recordId = row.recordId || ids.value
  getHealthRecord(_recordId).then(response => {
    form.value = response.data
    if (form.value.elderlyId) {
      listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => {
        elderlyOptions.value = res.rows
      })
    }
    open.value = true
    title.value = "修改健康记录管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["healthRecordRef"].validate(valid => {
    if (valid) {
      if (form.value.recordId != null) {
        updateHealthRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addHealthRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _recordIds = row.recordId || ids.value
  proxy.$modal.confirm('是否确认删除健康记录管理编号为"' + _recordIds + '"的数据项？').then(function() {
    return delHealthRecord(_recordIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('health/healthRecord/export', {
    ...queryParams.value
  }, `healthRecord_${new Date().getTime()}.xlsx`)
}

getList()
</script>
