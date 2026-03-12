<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input
          v-model="queryParams.courseName"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程类型" prop="courseType">
        <el-select v-model="queryParams.courseType" placeholder="请选择课程类型" clearable>
          <el-option
            v-for="dict in course_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="讲师姓名" prop="instructor">
        <el-input
          v-model="queryParams.instructor"
          placeholder="请输入讲师姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开课时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeStartTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="课程状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择课程状态" clearable>
          <el-option
            v-for="dict in course_status"
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
          v-hasPermi="['spirit:onlineCourse:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['spirit:onlineCourse:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['spirit:onlineCourse:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['spirit:onlineCourse:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="onlineCourseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课程名称" align="center" prop="courseName" />
      <el-table-column label="课程类型" align="center" prop="courseType">
        <template #default="scope">
          <dict-tag :options="course_type" :value="scope.row.courseType"/>
        </template>
      </el-table-column>
      <el-table-column label="讲师姓名" align="center" prop="instructor" />
      <el-table-column label="课程时长" align="center" prop="duration" />
      <el-table-column label="最大报名人数" align="center" prop="maxEnrollment" />
      <el-table-column label="当前报名人数" align="center" prop="currentEnrollment" />
      <el-table-column label="开课时间" align="center" prop="startTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结课时间" align="center" prop="endTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="课程状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="course_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['spirit:onlineCourse:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['spirit:onlineCourse:remove']">删除</el-button>
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

    <!-- 添加或修改线上课程管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="onlineCourseRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="课程类型" prop="courseType">
          <el-select v-model="form.courseType" placeholder="请选择课程类型">
            <el-option
              v-for="dict in course_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课程描述" prop="courseDescription">
          <el-input v-model="form.courseDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="讲师姓名" prop="instructor">
          <el-input v-model="form.instructor" placeholder="请输入讲师姓名" />
        </el-form-item>
        <el-form-item label="课程封面图片URL" prop="courseCover">
          <el-input v-model="form.courseCover" placeholder="请输入课程封面图片URL" />
        </el-form-item>
        <el-form-item label="课程视频URL" prop="videoUrl">
          <el-input v-model="form.videoUrl" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="课程时长" prop="duration">
          <el-input v-model="form.duration" placeholder="请输入课程时长" />
        </el-form-item>
        <el-form-item label="最大报名人数" prop="maxEnrollment">
          <el-input v-model="form.maxEnrollment" placeholder="请输入最大报名人数" />
        </el-form-item>
        <el-form-item label="开课时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择开课时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结课时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择结课时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="课程状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in course_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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

<script setup name="OnlineCourse">
import { listOnlineCourse, getOnlineCourse, delOnlineCourse, addOnlineCourse, updateOnlineCourse } from "@/api/spirit/onlineCourse"

const { proxy } = getCurrentInstance()
const { course_type, course_status } = proxy.useDict('course_type', 'course_status')

const onlineCourseList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeStartTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    courseName: null,
    courseType: null,
    instructor: null,
    startTime: null,
    status: null,
  },
  rules: {
    courseName: [
      { required: true, message: "课程名称不能为空", trigger: "blur" }
    ],
    courseType: [
      { required: true, message: "课程类型不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询线上课程管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeStartTime.value && '' != daterangeStartTime.value) {
    queryParams.value.params["beginStartTime"] = daterangeStartTime.value[0]
    queryParams.value.params["endStartTime"] = daterangeStartTime.value[1]
  }
  listOnlineCourse(queryParams.value).then(response => {
    onlineCourseList.value = response.rows
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
    courseId: null,
    courseName: null,
    courseType: null,
    courseDescription: null,
    instructor: null,
    courseCover: null,
    videoUrl: null,
    duration: null,
    maxEnrollment: null,
    currentEnrollment: null,
    startTime: null,
    endTime: null,
    status: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("onlineCourseRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeStartTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.courseId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加线上课程管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _courseId = row.courseId || ids.value
  getOnlineCourse(_courseId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改线上课程管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["onlineCourseRef"].validate(valid => {
    if (valid) {
      if (form.value.courseId != null) {
        updateOnlineCourse(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addOnlineCourse(form.value).then(response => {
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
  const _courseIds = row.courseId || ids.value
  proxy.$modal.confirm('是否确认删除线上课程管理编号为"' + _courseIds + '"的数据项？').then(function() {
    return delOnlineCourse(_courseIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('spirit/onlineCourse/export', {
    ...queryParams.value
  }, `onlineCourse_${new Date().getTime()}.xlsx`)
}

getList()
</script>
