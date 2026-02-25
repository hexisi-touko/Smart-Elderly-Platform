<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联用药提醒ID" prop="reminderId">
        <el-input
          v-model="queryParams.reminderId"
          placeholder="请输入关联用药提醒ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联老人ID" prop="elderlyId">
        <el-input
          v-model="queryParams.elderlyId"
          placeholder="请输入关联老人ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="药品名称" prop="medicationName">
        <el-input
          v-model="queryParams.medicationName"
          placeholder="请输入药品名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划服药时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeScheduledTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
          v-hasPermi="['health:medicationLog:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['health:medicationLog:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['health:medicationLog:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['health:medicationLog:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="medicationLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录id" align="center" prop="logId" />
      <el-table-column label="关联用药提醒ID" align="center" prop="reminderId" />
      <el-table-column label="关联老人ID" align="center" prop="elderlyId" />
      <el-table-column label="药品名称" align="center" prop="medicationName" />
      <el-table-column label="计划服药时间" align="center" prop="scheduledTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.scheduledTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否已服药" align="center" prop="isTaken">
        <template #default="scope">
          <dict-tag :options="medication_taken" :value="scope.row.isTaken"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['health:medicationLog:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['health:medicationLog:remove']">删除</el-button>
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

    <!-- 添加或修改用药记录管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="medicationLogRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联用药提醒ID" prop="reminderId">
          <el-input v-model="form.reminderId" placeholder="请输入关联用药提醒ID" />
        </el-form-item>
        <el-form-item label="关联老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入关联老人ID" />
        </el-form-item>
        <el-form-item label="药品名称" prop="medicationName">
          <el-input v-model="form.medicationName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="计划服药时间" prop="scheduledTime">
          <el-date-picker clearable
            v-model="form.scheduledTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择计划服药时间">
          </el-date-picker>
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

<script setup name="MedicationLog">
import { listMedicationLog, getMedicationLog, delMedicationLog, addMedicationLog, updateMedicationLog } from "@/api/health/medicationLog"

const { proxy } = getCurrentInstance()
const { medication_taken } = proxy.useDict('medication_taken')

const medicationLogList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeScheduledTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    reminderId: null,
    elderlyId: null,
    medicationName: null,
    scheduledTime: null,
  },
  rules: {
    reminderId: [
      { required: true, message: "关联用药提醒ID不能为空", trigger: "blur" }
    ],
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    medicationName: [
      { required: true, message: "药品名称不能为空", trigger: "blur" }
    ],
    scheduledTime: [
      { required: true, message: "计划服药时间不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询用药记录管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeScheduledTime.value && '' != daterangeScheduledTime.value) {
    queryParams.value.params["beginScheduledTime"] = daterangeScheduledTime.value[0]
    queryParams.value.params["endScheduledTime"] = daterangeScheduledTime.value[1]
  }
  listMedicationLog(queryParams.value).then(response => {
    medicationLogList.value = response.rows
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
    logId: null,
    reminderId: null,
    elderlyId: null,
    medicationName: null,
    scheduledTime: null,
    takenTime: null,
    isTaken: null,
    skipReason: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("medicationLogRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeScheduledTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.logId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加用药记录管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _logId = row.logId || ids.value
  getMedicationLog(_logId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改用药记录管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["medicationLogRef"].validate(valid => {
    if (valid) {
      if (form.value.logId != null) {
        updateMedicationLog(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addMedicationLog(form.value).then(response => {
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
  const _logIds = row.logId || ids.value
  proxy.$modal.confirm('是否确认删除用药记录管理编号为"' + _logIds + '"的数据项？').then(function() {
    return delMedicationLog(_logIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('health/medicationLog/export', {
    ...queryParams.value
  }, `medicationLog_${new Date().getTime()}.xlsx`)
}

getList()
</script>
