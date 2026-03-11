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
      <el-form-item label="关联活动ID" prop="activityId">
        <el-input
          v-model="queryParams.activityId"
          placeholder="请输入关联活动ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报名状态" prop="enrollmentStatus">
        <el-select v-model="queryParams.enrollmentStatus" placeholder="请选择报名状态" clearable>
          <el-option
            v-for="dict in activity_enrollment_status"
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
          v-hasPermi="['spirit:activityEnrollment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['spirit:activityEnrollment:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['spirit:activityEnrollment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['spirit:activityEnrollment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="activityEnrollmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一标识" align="center" prop="id" />
      <el-table-column label="关联老人ID" align="center" prop="elderlyId" />
      <el-table-column label="关联活动ID" align="center" prop="activityId" />
      <el-table-column label="报名状态" align="center" prop="enrollmentStatus">
        <template #default="scope">
          <dict-tag :options="activity_enrollment_status" :value="scope.row.enrollmentStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="签到时间" align="center" prop="checkInTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.checkInTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['spirit:activityEnrollment:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['spirit:activityEnrollment:remove']">删除</el-button>
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

    <!-- 添加或修改活动报名管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="activityEnrollmentRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入关联老人ID" />
        </el-form-item>
        <el-form-item label="关联活动ID" prop="activityId">
          <el-input v-model="form.activityId" placeholder="请输入关联活动ID" />
        </el-form-item>
        <el-form-item label="报名状态" prop="enrollmentStatus">
          <el-select v-model="form.enrollmentStatus" placeholder="请选择报名状态">
            <el-option
              v-for="dict in activity_enrollment_status"
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

<script setup name="ActivityEnrollment">
import { listActivityEnrollment, getActivityEnrollment, delActivityEnrollment, addActivityEnrollment, updateActivityEnrollment } from "@/api/spirit/activityEnrollment"

const { proxy } = getCurrentInstance()
const { activity_enrollment_status } = proxy.useDict('activity_enrollment_status')

const activityEnrollmentList = ref([])
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
    elderlyId: null,
    activityId: null,
    enrollmentStatus: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    activityId: [
      { required: true, message: "关联活动ID不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询活动报名管理列表 */
function getList() {
  loading.value = true
  listActivityEnrollment(queryParams.value).then(response => {
    activityEnrollmentList.value = response.rows
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
    id: null,
    elderlyId: null,
    activityId: null,
    enrollmentStatus: null,
    checkInTime: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("activityEnrollmentRef")
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
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加活动报名管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getActivityEnrollment(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改活动报名管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["activityEnrollmentRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateActivityEnrollment(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addActivityEnrollment(form.value).then(response => {
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
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除活动报名管理编号为"' + _ids + '"的数据项？').then(function() {
    return delActivityEnrollment(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('spirit/activityEnrollment/export', {
    ...queryParams.value
  }, `activityEnrollment_${new Date().getTime()}.xlsx`)
}

getList()
</script>
