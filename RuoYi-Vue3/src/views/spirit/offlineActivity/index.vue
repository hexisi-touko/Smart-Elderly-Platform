<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="活动名称" prop="activityName">
        <el-input
          v-model="queryParams.activityName"
          placeholder="请输入活动名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动类型" prop="activityType">
        <el-select v-model="queryParams.activityType" placeholder="请选择活动类型" clearable>
          <el-option
            v-for="dict in activity_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="主办方" prop="organizer">
        <el-input
          v-model="queryParams.organizer"
          placeholder="请输入主办方"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="活动开始时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeStartTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="活动结束时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeEndTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="活动状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择活动状态" clearable>
          <el-option
            v-for="dict in activity_status"
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
          v-hasPermi="['spirit:offlineActivity:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['spirit:offlineActivity:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['spirit:offlineActivity:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['spirit:offlineActivity:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="offlineActivityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="活动名称" align="center" prop="activityName" />
      <el-table-column label="活动类型" align="center" prop="activityType">
        <template #default="scope">
          <dict-tag :options="activity_type" :value="scope.row.activityType"/>
        </template>
      </el-table-column>
      <el-table-column label="主办方" align="center" prop="organizer" />
      <el-table-column label="活动地点" align="center" prop="activityLocation" />
      <el-table-column label="最大参与人数" align="center" prop="maxParticipants" />
      <el-table-column label="当前报名人数" align="center" prop="currentParticipants" />
      <el-table-column label="活动开始时间" align="center" prop="startTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="活动结束时间" align="center" prop="endTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="活动状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="activity_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['spirit:offlineActivity:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['spirit:offlineActivity:remove']">删除</el-button>
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

    <!-- 添加或修改线下活动管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="offlineActivityRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="活动名称" prop="activityName">
          <el-input v-model="form.activityName" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="活动类型" prop="activityType">
          <el-select v-model="form.activityType" placeholder="请选择活动类型">
            <el-option
              v-for="dict in activity_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动描述" prop="activityDescription">
          <el-input v-model="form.activityDescription" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="主办方" prop="organizer">
          <el-input v-model="form.organizer" placeholder="请输入主办方" />
        </el-form-item>
        <el-form-item label="活动封面图片" prop="activityCover">
          <el-input v-model="form.activityCover" placeholder="请输入活动封面图片" />
        </el-form-item>
        <el-form-item label="活动地点" prop="activityLocation">
          <el-input v-model="form.activityLocation" placeholder="请输入活动地点" />
        </el-form-item>
        <el-form-item label="活动地点经度" prop="activityLng">
          <el-input v-model="form.activityLng" placeholder="请输入活动地点经度" />
        </el-form-item>
        <el-form-item label="活动地点纬度" prop="activityLat">
          <el-input v-model="form.activityLat" placeholder="请输入活动地点纬度" />
        </el-form-item>
        <el-form-item label="最大参与人数" prop="maxParticipants">
          <el-input v-model="form.maxParticipants" placeholder="请输入最大参与人数" />
        </el-form-item>
        <el-form-item label="活动开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择活动开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择活动结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="活动状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择活动状态">
            <el-option
              v-for="dict in activity_status"
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

<script setup name="OfflineActivity">
import { listOfflineActivity, getOfflineActivity, delOfflineActivity, addOfflineActivity, updateOfflineActivity } from "@/api/spirit/offlineActivity"

const { proxy } = getCurrentInstance()
const { activity_status, activity_type } = proxy.useDict('activity_status', 'activity_type')

const offlineActivityList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeStartTime = ref([])
const daterangeEndTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    activityName: null,
    activityType: null,
    organizer: null,
    startTime: null,
    endTime: null,
    status: null,
  },
  rules: {
    activityName: [
      { required: true, message: "活动名称不能为空", trigger: "blur" }
    ],
    activityType: [
      { required: true, message: "活动类型不能为空", trigger: "change" }
    ],
    activityLocation: [
      { required: true, message: "活动地点不能为空", trigger: "blur" }
    ],
    startTime: [
      { required: true, message: "活动开始时间不能为空", trigger: "blur" }
    ],
    endTime: [
      { required: true, message: "活动结束时间不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询线下活动管理列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeStartTime.value && '' != daterangeStartTime.value) {
    queryParams.value.params["beginStartTime"] = daterangeStartTime.value[0]
    queryParams.value.params["endStartTime"] = daterangeStartTime.value[1]
  }
  if (null != daterangeEndTime.value && '' != daterangeEndTime.value) {
    queryParams.value.params["beginEndTime"] = daterangeEndTime.value[0]
    queryParams.value.params["endEndTime"] = daterangeEndTime.value[1]
  }
  listOfflineActivity(queryParams.value).then(response => {
    offlineActivityList.value = response.rows
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
    activityId: null,
    activityName: null,
    activityType: null,
    activityDescription: null,
    organizer: null,
    activityCover: null,
    activityLocation: null,
    activityLng: null,
    activityLat: null,
    maxParticipants: null,
    currentParticipants: null,
    startTime: null,
    endTime: null,
    status: null,
    isDeleted: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("offlineActivityRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeStartTime.value = []
  daterangeEndTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.activityId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加线下活动管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _activityId = row.activityId || ids.value
  getOfflineActivity(_activityId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改线下活动管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["offlineActivityRef"].validate(valid => {
    if (valid) {
      if (form.value.activityId != null) {
        updateOfflineActivity(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addOfflineActivity(form.value).then(response => {
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
  const _activityIds = row.activityId || ids.value
  proxy.$modal.confirm('是否确认删除线下活动管理编号为"' + _activityIds + '"的数据项？').then(function() {
    return delOfflineActivity(_activityIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('spirit/offlineActivity/export', {
    ...queryParams.value
  }, `offlineActivity_${new Date().getTime()}.xlsx`)
}

getList()
</script>
