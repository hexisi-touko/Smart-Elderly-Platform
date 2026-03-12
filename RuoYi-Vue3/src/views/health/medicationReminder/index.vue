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
      <el-form-item label="药品名称" prop="medicationName">
        <el-input
          v-model="queryParams.medicationName"
          placeholder="请输入药品名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提醒状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择提醒状态" clearable>
          <el-option
            v-for="dict in medication_status"
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
          v-hasPermi="['health:medicationReminder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['health:medicationReminder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['health:medicationReminder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['health:medicationReminder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="medicationReminderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="老人姓名" align="center" prop="elderlyName" />
      <el-table-column label="药品名称" align="center" prop="medicationName" />
      <el-table-column label="药品类型" align="center" prop="medicationType" />
      <el-table-column label="用药剂量" align="center" prop="dosage" />
      <el-table-column label="用药频率" align="center" prop="frequency" />
      <el-table-column label="提醒时间" align="center" prop="reminderTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.reminderTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始日期" align="center" prop="startDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" align="center" prop="endDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="提醒状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="medication_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['health:medicationReminder:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['health:medicationReminder:remove']">删除</el-button>
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

    <!-- 添加或修改用药提醒管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="medicationReminderRef" :model="form" :rules="rules" label-width="80px">
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
        <el-form-item label="药品名称" prop="medicationName">
          <el-input v-model="form.medicationName" placeholder="请输入药品名称" />
        </el-form-item>
        <el-form-item label="用药剂量" prop="dosage">
          <el-input v-model="form.dosage" placeholder="请输入用药剂量" />
        </el-form-item>
        <el-form-item label="用药频率" prop="frequency">
          <el-input v-model="form.frequency" placeholder="请输入用药频率" />
        </el-form-item>
        <el-form-item label="提醒时间" prop="reminderTime">
          <el-date-picker clearable
            v-model="form.reminderTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择提醒时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker clearable
            v-model="form.startDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择开始日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker clearable
            v-model="form.endDate"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注说明" prop="note">
          <el-input v-model="form.note" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="提醒状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择提醒状态">
            <el-option
              v-for="dict in medication_status"
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

<script setup name="MedicationReminder">
import { listMedicationReminder, getMedicationReminder, delMedicationReminder, addMedicationReminder, updateMedicationReminder } from "@/api/health/medicationReminder"
import { listElderly } from "@/api/elderly/elderly"

const { proxy } = getCurrentInstance()
const { medication_status } = proxy.useDict('medication_status')

const medicationReminderList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const elderlyOptions = ref([])
const elderlyLoading = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    elderlyName: null,
    medicationName: null,
    medicationType: null,
    status: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    medicationName: [
      { required: true, message: "药品名称不能为空", trigger: "blur" }
    ],
    frequency: [
      { required: true, message: "用药频率不能为空", trigger: "blur" }
    ],
    reminderTime: [
      { required: true, message: "提醒时间不能为空", trigger: "blur" }
    ],
    startDate: [
      { required: true, message: "开始日期不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询用药提醒管理列表 */
function getList() {
  loading.value = true
  listMedicationReminder(queryParams.value).then(response => {
    medicationReminderList.value = response.rows
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
    reminderId: null,
    elderlyId: null,
    medicationName: null,
    medicationType: null,
    dosage: null,
    frequency: null,
    reminderTime: null,
    startDate: null,
    endDate: null,
    note: null,
    status: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("medicationReminderRef")
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
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.reminderId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加用药提醒管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _reminderId = row.reminderId || ids.value
  getMedicationReminder(_reminderId).then(response => {
    form.value = response.data
    if (form.value.elderlyId) {
      listElderly({ elderlyId: form.value.elderlyId, pageNum: 1, pageSize: 10 }).then(res => {
        elderlyOptions.value = res.rows
      })
    }
    open.value = true
    title.value = "修改用药提醒管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["medicationReminderRef"].validate(valid => {
    if (valid) {
      if (form.value.reminderId != null) {
        updateMedicationReminder(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addMedicationReminder(form.value).then(response => {
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
  const _reminderIds = row.reminderId || ids.value
  proxy.$modal.confirm('是否确认删除用药提醒管理编号为"' + _reminderIds + '"的数据项？').then(function() {
    return delMedicationReminder(_reminderIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('health/medicationReminder/export', {
    ...queryParams.value
  }, `medicationReminder_${new Date().getTime()}.xlsx`)
}

getList()
</script>
