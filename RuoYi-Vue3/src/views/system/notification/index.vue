<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="接收用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入接收用户ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户来源" prop="userType">
        <el-select v-model="queryParams.userType" placeholder="请选择用户来源" clearable>
          <el-option
            v-for="dict in app_user_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="通知类型" prop="notificationType">
        <el-select v-model="queryParams.notificationType" placeholder="请选择通知类型" clearable>
          <el-option
            v-for="dict in notification_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="通知标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入通知标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否已读" prop="isRead">
        <el-select v-model="queryParams.isRead" placeholder="请选择是否已读" clearable>
          <el-option
            v-for="dict in sys_yes_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="业务类型" prop="bizType">
        <el-select v-model="queryParams.bizType" placeholder="请选择业务类型" clearable>
          <el-option
            v-for="dict in service_item_category"
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
          v-hasPermi="['system:notification:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:notification:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:notification:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:notification:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="notificationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="通知id" align="center" prop="notificationId" />
      <el-table-column label="接收用户ID" align="center" prop="userId" />
      <el-table-column label="用户来源" align="center" prop="userType">
        <template #default="scope">
          <dict-tag :options="app_user_type" :value="scope.row.userType"/>
        </template>
      </el-table-column>
      <el-table-column label="通知类型" align="center" prop="notificationType">
        <template #default="scope">
          <dict-tag :options="notification_type" :value="scope.row.notificationType"/>
        </template>
      </el-table-column>
      <el-table-column label="通知标题" align="center" prop="title" />
      <el-table-column label="是否已读" align="center" prop="isRead">
        <template #default="scope">
          <dict-tag :options="sys_yes_no" :value="scope.row.isRead"/>
        </template>
      </el-table-column>
      <el-table-column label="业务类型" align="center" prop="bizType">
        <template #default="scope">
          <dict-tag :options="service_item_category" :value="scope.row.bizType"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:notification:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:notification:remove']">删除</el-button>
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

    <!-- 添加或修改消息通知管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="notificationRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="接收用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入接收用户ID" />
        </el-form-item>
        <el-form-item label="用户来源" prop="userType">
          <el-select v-model="form.userType" placeholder="请选择用户来源">
            <el-option
              v-for="dict in app_user_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="通知类型" prop="notificationType">
          <el-select v-model="form.notificationType" placeholder="请选择通知类型">
            <el-option
              v-for="dict in notification_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="通知标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入通知标题" />
        </el-form-item>
        <el-form-item label="通知内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="业务类型" prop="bizType">
          <el-select v-model="form.bizType" placeholder="请选择业务类型">
            <el-option
              v-for="dict in service_item_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
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

<script setup name="Notification">
import { listNotification, getNotification, delNotification, addNotification, updateNotification } from "@/api/system/notification"

const { proxy } = getCurrentInstance()
const { sys_yes_no, service_item_category, notification_type, app_user_type } = proxy.useDict('sys_yes_no', 'service_item_category', 'notification_type', 'app_user_type')

const notificationList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: null,
    userType: null,
    notificationType: null,
    title: null,
    isRead: null,
    bizType: null,
  },
  rules: {
    userId: [
      { required: true, message: "接收用户ID不能为空", trigger: "blur" }
    ],
    userType: [
      { required: true, message: "用户来源不能为空", trigger: "change" }
    ],
    notificationType: [
      { required: true, message: "通知类型不能为空", trigger: "change" }
    ],
    title: [
      { required: true, message: "通知标题不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "通知内容不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询消息通知管理列表 */
function getList() {
  loading.value = true
  listNotification(queryParams.value).then(response => {
    notificationList.value = response.rows
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
    notificationId: null,
    userId: null,
    userType: null,
    notificationType: null,
    title: null,
    content: null,
    priority: null,
    isRead: null,
    readTime: null,
    bizId: null,
    bizType: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("notificationRef")
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
  ids.value = selection.map(item => item.notificationId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加消息通知管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _notificationId = row.notificationId || ids.value
  getNotification(_notificationId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改消息通知管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["notificationRef"].validate(valid => {
    if (valid) {
      if (form.value.notificationId != null) {
        updateNotification(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addNotification(form.value).then(response => {
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
  const _notificationIds = row.notificationId || ids.value
  proxy.$modal.confirm('是否确认删除消息通知管理编号为"' + _notificationIds + '"的数据项？').then(function() {
    return delNotification(_notificationIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/notification/export', {
    ...queryParams.value
  }, `notification_${new Date().getTime()}.xlsx`)
}

getList()
</script>
