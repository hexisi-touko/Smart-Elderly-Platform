<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联老人ID" prop="elderlyId">
        <el-input
          v-model="queryParams.elderlyId"
          placeholder="请输入关联老人ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联触发预警的设备ID" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入关联触发预警的设备ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预警类型" prop="alertType">
        <el-select v-model="queryParams.alertType" placeholder="请选择预警类型" clearable>
          <el-option
            v-for="dict in alert_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预警触发时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeAlertTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="处理人角色" prop="handlerRole">
        <el-select v-model="queryParams.handlerRole" placeholder="请选择处理人角色" clearable>
          <el-option
            v-for="dict in handler_role"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="预警状态" prop="alertStatus">
        <el-select v-model="queryParams.alertStatus" placeholder="请选择预警状态" clearable>
          <el-option
            v-for="dict in alert_status"
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
          v-hasPermi="['safety:alert:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['safety:alert:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['safety:alert:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['safety:alert:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="alertList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="预警id" align="center" prop="alertId" />
      <el-table-column label="关联老人ID" align="center" prop="elderlyId" />
      <el-table-column label="关联触发预警的设备ID" align="center" prop="deviceId" />
      <el-table-column label="预警类型" align="center" prop="alertType">
        <template #default="scope">
          <dict-tag :options="alert_type" :value="scope.row.alertType"/>
        </template>
      </el-table-column>
      <el-table-column label="预警触发时间" align="center" prop="alertTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.alertTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="服务商接单时间" align="center" prop="responseTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.responseTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="处置完成时间" align="center" prop="completeTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.completeTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="子女一键催促次数" align="center" prop="urgeCount" />
      <el-table-column label="处理人ID" align="center" prop="handlerId" />
      <el-table-column label="处理人角色" align="center" prop="handlerRole">
        <template #default="scope">
          <dict-tag :options="handler_role" :value="scope.row.handlerRole"/>
        </template>
      </el-table-column>
      <el-table-column label="预警地点文字描述" align="center" prop="alertAddress" />
      <el-table-column label="预警状态" align="center" prop="alertStatus">
        <template #default="scope">
          <dict-tag :options="alert_status" :value="scope.row.alertStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['safety:alert:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['safety:alert:remove']">删除</el-button>
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

    <!-- 添加或修改安全预警管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="alertRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入关联老人ID" />
        </el-form-item>
        <el-form-item label="关联触发预警的设备ID" prop="deviceId">
          <el-input v-model="form.deviceId" placeholder="请输入关联触发预警的设备ID" />
        </el-form-item>
        <el-form-item label="预警类型" prop="alertType">
          <el-select v-model="form.alertType" placeholder="请选择预警类型">
            <el-option
              v-for="dict in alert_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预警触发时间" prop="alertTime">
          <el-date-picker clearable
            v-model="form.alertTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择预警触发时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预警地点经度" prop="alertLng">
          <el-input v-model="form.alertLng" placeholder="请输入预警地点经度" />
        </el-form-item>
        <el-form-item label="预警地点纬度" prop="alertLat">
          <el-input v-model="form.alertLat" placeholder="请输入预警地点纬度" />
        </el-form-item>
        <el-form-item label="预警地点文字描述" prop="alertAddress">
          <el-input v-model="form.alertAddress" placeholder="请输入预警地点文字描述" />
        </el-form-item>
        <el-form-item label="预警状态" prop="alertStatus">
          <el-select v-model="form.alertStatus" placeholder="请选择预警状态">
            <el-option
              v-for="dict in alert_status"
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

<script setup name="Alert">
import { listAlert, getAlert, delAlert, addAlert, updateAlert } from "@/api/safety/alert"

const { proxy } = getCurrentInstance()
const { alert_status, alert_type, handler_role } = proxy.useDict('alert_status', 'alert_type', 'handler_role')

const alertList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeAlertTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderlyId: null,
    deviceId: null,
    alertType: null,
    alertTime: null,
    handlerRole: null,
    alertStatus: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    deviceId: [
      { required: true, message: "关联触发预警的设备ID不能为空", trigger: "blur" }
    ],
    alertType: [
      { required: true, message: "预警类型不能为空", trigger: "change" }
    ],
    alertTime: [
      { required: true, message: "预警触发时间不能为空", trigger: "blur" }
    ],
    alertLng: [
      { required: true, message: "预警地点经度不能为空", trigger: "blur" }
    ],
    alertLat: [
      { required: true, message: "预警地点纬度不能为空", trigger: "blur" }
    ],
    alertAddress: [
      { required: true, message: "预警地点文字描述不能为空", trigger: "blur" }
    ],
    alertStatus: [
      { required: true, message: "预警状态不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询安全预警管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeAlertTime.value && '' != daterangeAlertTime.value) {
    queryParams.value.params["beginAlertTime"] = daterangeAlertTime.value[0]
    queryParams.value.params["endAlertTime"] = daterangeAlertTime.value[1]
  }
  listAlert(queryParams.value).then(response => {
    alertList.value = response.rows
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
    alertId: null,
    elderlyId: null,
    deviceId: null,
    alertType: null,
    alertTime: null,
    responseTime: null,
    completeTime: null,
    urgeCount: null,
    handlerId: null,
    handlerRole: null,
    alertLng: null,
    alertLat: null,
    alertAddress: null,
    alertStatus: null,
    handleResult: null,
    emergencyOrderId: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("alertRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeAlertTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.alertId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加安全预警管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _alertId = row.alertId || ids.value
  getAlert(_alertId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改安全预警管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["alertRef"].validate(valid => {
    if (valid) {
      if (form.value.alertId != null) {
        updateAlert(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addAlert(form.value).then(response => {
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
  const _alertIds = row.alertId || ids.value
  proxy.$modal.confirm('是否确认删除安全预警管理编号为"' + _alertIds + '"的数据项？').then(function() {
    return delAlert(_alertIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('safety/alert/export', {
    ...queryParams.value
  }, `alert_${new Date().getTime()}.xlsx`)
}

getList()
</script>
