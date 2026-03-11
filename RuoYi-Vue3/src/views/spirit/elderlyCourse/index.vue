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
      <el-form-item label="关联课程ID" prop="courseId">
        <el-input
          v-model="queryParams.courseId"
          placeholder="请输入关联课程ID"
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
          v-hasPermi="['spirit:elderlyCourse:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['spirit:elderlyCourse:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['spirit:elderlyCourse:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['spirit:elderlyCourse:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="elderlyCourseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="唯一标识" align="center" prop="id" />
      <el-table-column label="关联老人ID" align="center" prop="elderlyId" />
      <el-table-column label="关联课程ID" align="center" prop="courseId" />
      <el-table-column label="报名状态" align="center" prop="enrollmentStatus">
        <template #default="scope">
          <dict-tag :options="activity_enrollment_status" :value="scope.row.enrollmentStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="学习进度" align="center" prop="learningProgress" />
      <el-table-column label="报名时间" align="center" prop="enrollmentTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.enrollmentTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="completionTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.completionTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['spirit:elderlyCourse:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['spirit:elderlyCourse:remove']">删除</el-button>
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

    <!-- 添加或修改老人-课程报名对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="elderlyCourseRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联老人ID" prop="elderlyId">
          <el-input v-model="form.elderlyId" placeholder="请输入关联老人ID" />
        </el-form-item>
        <el-form-item label="关联课程ID" prop="courseId">
          <el-input v-model="form.courseId" placeholder="请输入关联课程ID" />
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
        <el-form-item label="学习进度" prop="learningProgress">
          <el-input v-model="form.learningProgress" placeholder="请输入学习进度" />
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

<script setup name="ElderlyCourse">
import { listElderlyCourse, getElderlyCourse, delElderlyCourse, addElderlyCourse, updateElderlyCourse } from "@/api/spirit/elderlyCourse"

const { proxy } = getCurrentInstance()
const { activity_enrollment_status } = proxy.useDict('activity_enrollment_status')

const elderlyCourseList = ref([])
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
    courseId: null,
    enrollmentStatus: null,
  },
  rules: {
    elderlyId: [
      { required: true, message: "关联老人ID不能为空", trigger: "blur" }
    ],
    courseId: [
      { required: true, message: "关联课程ID不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询老人-课程报名列表 */
function getList() {
  loading.value = true
  listElderlyCourse(queryParams.value).then(response => {
    elderlyCourseList.value = response.rows
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
    courseId: null,
    enrollmentStatus: null,
    learningProgress: null,
    enrollmentTime: null,
    completionTime: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("elderlyCourseRef")
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
  title.value = "添加老人-课程报名"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getElderlyCourse(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改老人-课程报名"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["elderlyCourseRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateElderlyCourse(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addElderlyCourse(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除老人-课程报名编号为"' + _ids + '"的数据项？').then(function() {
    return delElderlyCourse(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('spirit/elderlyCourse/export', {
    ...queryParams.value
  }, `elderlyCourse_${new Date().getTime()}.xlsx`)
}

getList()
</script>
